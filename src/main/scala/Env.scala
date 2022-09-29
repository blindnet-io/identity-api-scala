package io.blindnet.identity

import db.DbConfig

object Env {
  val get: Env = sys.env.getOrElse("BN_ENV", "") match
    case "production" => ProductionEnv()
    case "staging" => StagingEnv()
    case _ => DevelopmentEnv()
}

abstract class Env() {
  val name: String

  val port: Int = sys.env.getOrElse("BN_PORT", "8028").toInt
  val host: String = sys.env.getOrElse("BN_HOST", "127.0.0.1")

  val migrate: Boolean
  val dbUri: String
  val dbUsername: String
  val dbPassword: String
  val dbConfig: DbConfig = DbConfig(dbUri, dbUsername, dbPassword)
}

class ProductionEnv() extends Env {
  override val name: String = "production"

  override val migrate: Boolean = sys.env.get("BN_MIGRATE").contains("yes")
  override val dbUri: String = sys.env("BN_DB_URI")
  override val dbUsername: String = sys.env("BN_DB_USER")
  override val dbPassword: String = sys.env("BN_DB_PASSWORD")
}

class StagingEnv() extends ProductionEnv {
  override val name: String = "staging"
}

class DevelopmentEnv() extends StagingEnv {
  override val name: String = "development"

  override val migrate: Boolean = sys.env.get("BN_MIGRATE").forall(_ == "yes")
  override val dbUri: String = sys.env.getOrElse("BN_DB_URI", "jdbc:postgresql://127.0.0.1/identity")
  override val dbUsername: String = sys.env.getOrElse("BN_DB_USER", "identity")
  override val dbPassword: String = sys.env.getOrElse("BN_DB_PASSWORD", "identity")
}
