package io.blindnet.identity

import db.*

import cats.effect.*
import doobie.*
import doobie.hikari.*
import doobie.util.*

class Repositories(xa: Transactor[IO]) {
  val accounts: AccountRepository         = AccountRepository(xa)
  val appGroups: AppGroupRepository       = AppGroupRepository(xa)
  val applications: ApplicationRepository = ApplicationRepository(xa)
}

object Repositories {
  def apply(env: Env): Resource[IO, Repositories] =
    for {
      ec <- ExecutionContexts.fixedThreadPool[IO](32)
      xa <- HikariTransactor.newHikariTransactor[IO](
        "org.postgresql.Driver",
        env.dbUri,
        env.dbUsername,
        env.dbPassword,
        ec
      )
    } yield new Repositories(xa)
}
