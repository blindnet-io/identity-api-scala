package io.blindnet.identity
package db

import models.Application

import cats.effect.*
import doobie.*
import doobie.implicits.*
import doobie.postgres.*
import doobie.postgres.implicits.*

class ApplicationRepository(xa: Transactor[IO]) {
  def findById(id: String): IO[Option[Application]] =
    sql"select id, name, key from applications where id=$id"
      .query[Application].option.transact(xa)

  def insert(app: Application): IO[Unit] =
    sql"insert into applications (id, name, key) values (${app.id}, ${app.name}, ${app.key})"
      .update.run.transact(xa).void
}

