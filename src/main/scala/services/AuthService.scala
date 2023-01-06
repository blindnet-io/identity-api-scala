package io.blindnet.identity
package services

import endpoints.objects.*
import errors.*
import mail.*
import models.Account
import util.Resources

import cats.data.OptionT
import cats.effect.*
import cats.effect.std.{ Random, UUIDGen }

import java.util.UUID

class AuthService(
    env: Env,
    repos: Repositories,
    mailService: MailService,
    templates: MailTemplates
) {
  given UUIDGen[IO] = UUIDGen.fromSync

  private def generateStaticToken(): IO[String] =
    for {
      random <- Random.javaSecuritySecureRandom[IO]
      token  <- random.nextAlphaNumeric.replicateA(128).map(_.mkString)
    } yield token

  private def sendVerificationEmail(email: String, emailToken: String): IO[Unit] =
    val template = templates.verify
      .map(_.replace("$verification_link$", s"${env.baseUrl}/verify?token=$emailToken"))
    val mail     = Mail.fromTemplate(template)(env.mailSender, List(email))
    mailService.send(mail)

  def login(payload: LoginPayload): IO[LoginResponsePayload] =
    for {
      account <- repos.accounts.findByEmail(payload.email).orForbidden
      _       <- account.verifyPassword(payload.password).flatMap(_.orForbidden)
    } yield LoginResponsePayload(account.token, AccountStatusPayload(account.verified))

  def register(payload: RegisterPayload): IO[LoginResponsePayload] =
    for {
      _     <- repos.accounts.findByEmail(payload.email).thenBadRequest("Account already exists")
      id    <- UUIDGen.randomUUID
      token <- generateStaticToken()
      emailToken     <- generateStaticToken()
      hashedPassword <- Account.hashPassword(payload.password)
      _              <- repos.accounts.insert(id, payload.email, hashedPassword, token, emailToken)
      _              <- sendVerificationEmail(payload.email, emailToken)
    } yield LoginResponsePayload(token, AccountStatusPayload(false))

  def status(acc: Account)(x: Unit): IO[AccountStatusPayload] =
    IO.pure(AccountStatusPayload(acc.verified))

  def verify(payload: VerifyEmailPayload): IO[Unit] =
    for {
      acc <- repos.accounts.findByEmailToken(payload.token).orBadRequest("Invalid token")
      _   <- (!acc.verified).orBadRequest("Already verified")
      _   <- repos.accounts.setVerified(acc.id)
    } yield ()

  def resendVerification(acc: Account)(x: Unit): IO[Unit] =
    for {
      _ <- (!acc.verified).orBadRequest("Already verified")
      _ <- sendVerificationEmail(acc.email, acc.emailToken)
    } yield ()

  def updateEmail(acc: Account)(payload: UpdateEmailPayload): IO[Unit] =
    for {
      _          <- acc.verifyPassword(payload.current_password).map(_.orForbidden)
      emailToken <- generateStaticToken()
      _          <- repos.accounts.updateEmail(acc.id, payload.email, emailToken)
      _          <- sendVerificationEmail(payload.email, emailToken)
    } yield ()

  def changePassword(acc: Account)(payload: ChangePasswordPayload): IO[LoginResponsePayload] =
    for {
      _              <- acc.verifyPassword(payload.current_password).map(_.orForbidden)
      token          <- generateStaticToken()
      hashedPassword <- Account.hashPassword(payload.new_password)
      _              <- repos.accounts.updatePassword(acc.id, hashedPassword, token)
    } yield LoginResponsePayload(token, AccountStatusPayload(acc.verified))

}
