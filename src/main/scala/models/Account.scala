package io.blindnet.identity
package models

import cats.effect.*
import com.password4j.Password
import io.blindnet.identityclient.auth.St

import java.util.UUID

case class Account(id: UUID, email: String, password: String, token: String, verified: Boolean, emailToken: String) extends St {
  def verifyPassword(in: String): IO[Boolean] = IO {
    Password.check(in, password).withArgon2()
  }
}

object Account {
  def hashPassword(password: String): IO[String] = IO {
    Password.hash(password).addRandomSalt(12).withArgon2().getResult;
  }
}
