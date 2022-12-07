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
    sql"select id, email, password, token, verified, email_token from accounts where email=$email"
      .query[Account].option.transact(xa)

  override def findByToken(token: String): IO[Option[Account]] =
    sql"select id, email, password, token, verified, email_token from accounts where token=$token"
      .query[Account].option.transact(xa)

  def insert(id: UUID, email: String, password: String, token: String, emailToken: String): IO[Unit] =
    sql"insert into accounts (id, email, password, token, email_token) values ($id, $email, $password, $token, $emailToken)"
      .update.run.transact(xa).void

  def setVerified(id: UUID): IO[Unit] =
    sql"update accounts set verified=true where id=$id"
      .update.run.transact(xa).void

  def updateEmail(id: UUID, email: String, emailToken: String): IO[Unit] =
    sql"update accounts set email=$email, email_token=$emailToken, verified=false where id=$id"
      .update.run.transact(xa).void

  def updatePassword(id: UUID, password: String, token: String): IO[Unit] =
    sql"update accounts set password=$password, token=$token where id=$id"
      .update.run.transact(xa).void
}
