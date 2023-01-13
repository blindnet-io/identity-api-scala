package io.blindnet.identity
package services

import endpoints.objects.*
import errors.*
import models.Account

import cats.effect.*
import cats.effect.std.UUIDGen

import java.util.UUID

class AppGroupService(repos: Repositories) {
  given UUIDGen[IO] = UUIDGen.fromSync

  def create(acc: Account)(payload: CreateAppGroupPayload): IO[UUID] =
    for {
      id <- UUIDGen.randomUUID
      _  <- repos.appGroups.insert(id, acc.id, payload.name, payload.key)
    } yield id

  def get(acc: Account)(id: UUID): IO[AppGroupInfoPayload] =
    repos.appGroups
      .findById(acc.id, id)
      .orNotFound
      .map(AppGroupInfoPayload(_))

  def getApps(acc: Account)(id: UUID): IO[List[ApplicationInfoLitePayload]] =
    for {
      _    <- repos.appGroups.findById(acc.id, id).orNotFound
      apps <- repos.applications.findAllByGroup(id)
    } yield apps.map(ApplicationInfoLitePayload(_))

  def getAll(acc: Account)(x: Unit): IO[List[AppGroupInfoPayload]] =
    repos.appGroups
      .findAllByAccount(acc.id)
      .map(_.map(AppGroupInfoPayload(_)))

  def update(acc: Account)(id: UUID, payload: UpdateAppGroupPayload): IO[Unit] =
    for {
      _ <- repos.appGroups.findById(acc.id, id).orNotFound
      _ <- payload.name match
        case Some(name) => repos.appGroups.updateName(acc.id, id, name)
        case None       => IO.unit
      _ <- payload.key match
        case Some(key) => repos.appGroups.updateKey(acc.id, id, key)
        case None      => IO.unit
    } yield ()

  def delete(acc: Account)(id: UUID): IO[Unit] =
    for {
      _ <- repos.appGroups.findById(acc.id, id).orNotFound
      _ <- repos.appGroups.delete(acc.id, id)
    } yield ()

}
