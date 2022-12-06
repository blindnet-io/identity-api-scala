package io.blindnet.identity
package db

import models.Account

import cats.effect.*
import doobie.*
import doobie.implicits.*
import doobie.postgres.*
import doobie.postgres.implicits.*
import io.blindnet.identityclient.auth.StRepository

import java.util.UUID

class AccountRepository(xa: Transactor[IO]) extends StRepository[Account, IO] {
  def findByEmail(email: String): IO[Option[Account]] =
    sql"select id, email, password, token from accounts where email=$email"
      .query[Account].option.transact(xa)

  override def findByToken(token: String): IO[Option[Account]] =
    sql"select id, email, password, token from accounts where token=$token"
      .query[Account].option.transact(xa)

  def insert(id: UUID, email: String, password: String, token: String): IO[Unit] =
    sql"insert into accounts (id, email, password, token) values ($id, $email, $password, $token)"
      .update.run.transact(xa).void
}
