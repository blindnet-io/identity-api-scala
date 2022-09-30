package io.blindnet.identity

import db.*

import cats.effect.*
import doobie.*
import doobie.hikari.*
import doobie.util.*

class Repositories(xa: Transactor[IO]) {
  val applications: ApplicationRepository = ApplicationRepository(xa)
}

object Repositories {
  def apply(): Resource[IO, Repositories] =
    for {
      ec <- ExecutionContexts.fixedThreadPool[IO](32)
      xa <- HikariTransactor.newHikariTransactor[IO](
        "org.postgresql.Driver",
        Env.get.dbUri,
        Env.get.dbUsername,
        Env.get.dbPassword,
        ec,
      )
    } yield new Repositories(xa)
}
