package io.blindnet.identity
package db

import cats.effect.*
import doobie.*
import doobie.implicits.*
import doobie.postgres.*
import doobie.postgres.implicits.*

import java.util.UUID

class AccountRepository(xa: Transactor[IO]) {

  def insert(id: UUID): IO[Unit] =
    sql"""insert into accounts values ($id)""".update.run.transact(xa).void

}
