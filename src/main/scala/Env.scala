package io.blindnet.identity

import ciris.*
import cats.implicits.*
import cats.effect.*

case class Env(
    name: String,
    port: Int,
    host: String,
    migrate: Boolean,
    dbUri: String,
    dbUsername: String,
    dbPassword: String
)

object Env {

  def get: IO[Env] = env("BN_ENV")
    .as[String]
    .flatMap {
      case "production" => ProductionEnv().load
      case "staging"    => StagingEnv().load
      case _            => DevelopmentEnv().load
    }
    .load[IO]

}

trait EnvLoader {
  val name: String

  def port: ConfigValue[Effect, Int]    = env("BN_PORT").as[Int].default(8029)
  def host: ConfigValue[Effect, String] = env("BN_HOST").as[String].default("127.0.0.1")

  def migrate: ConfigValue[Effect, Boolean]
  def dbUri: ConfigValue[Effect, String]
  def dbUsername: ConfigValue[Effect, String]
  def dbPassword: ConfigValue[Effect, String]

  def load =
    (ConfigValue.default(name), port, host, migrate, dbUri, dbUsername, dbPassword)
      .parMapN(Env.apply)

}

class ProductionEnv() extends EnvLoader {
  override val name: String = "production"

  override def migrate    = env("BN_MIGRATE").map(_.equals("yes"))
  override def dbUri      = env("BN_DB_URI")
  override def dbUsername = env("BN_DB_USER")
  override def dbPassword = env("BN_DB_PASSWORD")
}

class StagingEnv() extends ProductionEnv {
  override val name: String = "staging"
}

class DevelopmentEnv() extends StagingEnv {
  override val name: String = "development"

  override def migrate    = super.migrate.default(true)
  override def dbUri      = super.dbUri.default("jdbc:postgresql://127.0.0.1/identity")
  override def dbUsername = super.dbUsername.default("identity")
  override def dbPassword = super.dbPassword.default("identity")
}
