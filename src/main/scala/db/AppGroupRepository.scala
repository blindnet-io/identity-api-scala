package io.blindnet.identity
package db

import models.*

import cats.effect.*
import doobie.*
import doobie.implicits.*
import doobie.postgres.*
import doobie.postgres.implicits.*

import java.util.UUID

class AppGroupRepository(xa: Transactor[IO]) {
  def findById(accId: UUID, id: UUID): IO[Option[AppGroup]] =
    sql"select id, accid, name, key from application_groups where accid=$accId and id=$id"
      .query[AppGroup].option.transact(xa)

  def findAllByAccount(accId: UUID): IO[List[AppGroup]] =
    sql"select id, accid, name, key from application_groups where accid=$accId"
      .query[AppGroup].to[List].transact(xa)

  def insert(id: UUID, accId: UUID, name: String, key: String): IO[Unit] =
    sql"insert into application_groups (id, accid, name, key) values ($id, $accId, $name, $key)"
      .update.run.transact(xa).void

  def updateName(accId: UUID, id: UUID, name: String): IO[Unit] =
    sql"update application_groups set name=$name where accid=$accId and id=$id"
      .update.run.transact(xa).void

  def updateKey(accId: UUID, id: UUID, key: String): IO[Unit] =
    sql"update application_groups set key=$key where accid=$accId and id=$id"
      .update.run.transact(xa).void

  def delete(accId: UUID, id: UUID): IO[Unit] =
    sql"delete from application_groups where accid=$accId and id=$id"
      .update.run.transact(xa).void
}
