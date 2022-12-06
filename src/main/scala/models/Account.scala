package io.blindnet.identity
package models

import cats.effect.*
import org.mindrot.jbcrypt.BCrypt

import java.util.UUID

case class Account(id: UUID, email: String, password: String, token: String) {
  def verifyPassword(in: String): IO[Boolean] = IO {
    BCrypt.checkpw(in, password)
  }
}

object Account {
  def hashPassword(password: String): IO[String] = IO {
    BCrypt.hashpw(password, BCrypt.gensalt())
  }
}
