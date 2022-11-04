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
      select a.id, a.name, g.key
      from applications a
        join application_groups g on g.id = a.gid
      where a.id=$id
    """
      .query[Application]
      .option
      .transact(xa)

  def insertGroup(g: AppGroup) =
    sql"""
      insert into application_groups (id, accid, name, key)
      values (${g.id}, ${g.accId}, ${g.name}, ${g.key})
    """.update.run.transact(xa).void

  def insert(app: NewApplication): IO[Unit] =
    sql"""
      insert into applications (id, gid, name)
      values (${app.id}, ${app.groupId}, ${app.name})
    """.update.run.transact(xa).void

}
