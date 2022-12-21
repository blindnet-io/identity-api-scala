package io.blindnet.identity
package services

import endpoints.objects.*
import errors.*
import models.Account

import cats.effect.*
import cats.effect.std.UUIDGen

import java.util.UUID
import io.blindnet.identity.clients.PceClient

class ApplicationService(repos: Repositories, pceClient: PceClient) {
  given UUIDGen[IO] = UUIDGen.fromSync

  def get(id: UUID): IO[ApplicationInfoPayload] =
    repos.applications
      .findById(id)
      .orNotFound
      .map(ApplicationInfoPayload.apply)

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
