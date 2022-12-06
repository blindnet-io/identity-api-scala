package io.blindnet.identity
package services

import endpoints.objects.*
import errors.*
import models.Account

import cats.effect.*
import cats.effect.std.UUIDGen

import java.util.UUID
import scala.util.Random

class AuthService(repos: Repositories) {
  given UUIDGen[IO] = UUIDGen.fromSync

  private def generateStaticToken(): IO[String] =
    IO(Random.alphanumeric.take(128).mkString)

  def login(payload: LoginPayload): IO[LoginResponsePayload] =
    for {
      account <- repos.accounts.findByEmail(payload.email).orForbidden
      _ <- account.verifyPassword(payload.password).flatMap(_.orForbidden)
    } yield LoginResponsePayload(account.token)
  
  def register(payload: RegisterPayload): IO[LoginResponsePayload] =
    for {
      _ <- repos.accounts.findByEmail(payload.email).thenBadRequest("Account already exists")
      id <- UUIDGen.randomUUID
      token <- generateStaticToken()
      hashedPassword <- Account.hashPassword(payload.password)
      _ <- repos.accounts.insert(id, payload.email, hashedPassword, token)
    } yield LoginResponsePayload(token)
}
