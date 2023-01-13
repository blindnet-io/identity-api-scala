package io.blindnet.identity
package services

import endpoints.objects.*
import errors.*
import models.Account

import cats.effect.*
import cats.effect.std.UUIDGen

import java.util.UUID
import io.blindnet.identity.clients.PceClient
import io.blindnet.jwt.{ TokenPrivateKey, TokenBuilder }

class ApplicationService(env: Env, repos: Repositories, pceClient: PceClient) {
  given UUIDGen[IO] = UUIDGen.fromSync

  def get(id: UUID): IO[ApplicationInfoPayload] =
    for {
      app <- repos.applications.findById(id).orNotFound
      key          = TokenPrivateKey.fromString(env.tokenSigningKey.value)
      tokenBuilder = new TokenBuilder(app.id, key)
      token        = tokenBuilder.app()
      resp         = ApplicationInfoPayload(app, token, token)
    } yield resp

  def create(acc: Account)(payload: CreateApplicationPayload): IO[UUID] =
    for {
      _  <- repos.appGroups.findById(acc.id, payload.group_id).orNotFound
      id <- UUIDGen.randomUUID
      _  <- repos.applications.insert(id, payload.group_id, payload.name)
      _  <- pceClient.createApp(id)
    } yield id

  def update(acc: Account)(id: UUID, payload: UpdateApplicationPayload): IO[Unit] =
    for {
      _ <- repos.applications.findByIdAndAccount(id, acc.id).orNotFound
      _ <- payload.name match
        case Some(name) => repos.applications.updateName(id, name)
        case None       => IO.unit
    } yield ()

  def delete(acc: Account)(id: UUID): IO[Unit] =
    for {
      _ <- repos.applications.findByIdAndAccount(id, acc.id).orNotFound
      _ <- repos.applications.delete(id)
    } yield ()

}
