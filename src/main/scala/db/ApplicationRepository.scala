package io.blindnet.identity
package db

import models.*

import cats.effect.*
import doobie.*
import doobie.implicits.*
import doobie.postgres.*
import doobie.postgres.implicits.*

import java.util.UUID

class ApplicationRepository(xa: Transactor[IO]) {
  def findById(id: UUID): IO[Option[Application]] =
    sql"""
      select a.id, a.gid, a.name, g.key
      from applications a
        join application_groups g on g.id = a.gid
      where a.id=$id
    """
      .query[Application]
      .option
      .transact(xa)

  def findByIdAndAccount(id: UUID, accId: UUID): IO[Option[Application]] =
    sql"""
      select a.id, a.gid, a.name, g.key
      from applications a
        join application_groups g on g.id = a.gid
      where a.id=$id and g.accid=$accId
    """
      .query[Application]
      .option
      .transact(xa)

  def findAllByGroup(groupId: UUID): IO[List[Application]] =
    sql"""
      select a.id, a.gid, a.name, g.key
      from applications a
        join application_groups g on g.id = a.gid
      where g.id=$groupId
    """
      .query[Application]
      .to[List]
      .transact(xa)

  def insert(id: UUID, groupId: UUID, name: String): IO[Unit] =
    sql"""
      insert into applications (id, gid, name)
      values ($id, $groupId, $name)
    """.update.run.transact(xa).void

  def updateName(id: UUID, name: String): IO[Unit] =
    sql"update applications set name=$name where id=$id".update.run.transact(xa).void

  def delete(id: UUID): IO[Unit] =
    sql"delete from applications where id=$id".update.run.transact(xa).void

}
