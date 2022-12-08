package io.blindnet.identity

import mail.{MailConfig, MailSender}

import ciris.*
import cats.implicits.*
import cats.effect.*

case class Env(
    name: String,
    port: Int,
    host: String,
    baseUrl: String,
    migrate: Boolean,
    dbUri: String,
    dbUsername: String,
    dbPassword: String,
    emailHost: String,
    emailUsername: String,
    emailPassword: String,
    emailFromEmail: String,
    emailFromName: String,
    sendInternalErrorMessages: Boolean,
) {
  def mailConfig: MailConfig =
    MailConfig(emailUsername, emailPassword, emailHost)

  def mailSender: MailSender =
    MailSender(emailFromEmail, Some(emailFromName))
}

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

  def port: ConfigValue[Effect, Int]       = env("BN_PORT").as[Int].default(8029)
  def host: ConfigValue[Effect, String]    = env("BN_HOST").as[String].default("127.0.0.1")
  def baseUrl: ConfigValue[Effect, String] = env("BN_BASE_URL").as[String].default("http://127.0.0.1:8029")

  def migrate: ConfigValue[Effect, Boolean]
  def dbUri: ConfigValue[Effect, String]
  def dbUsername: ConfigValue[Effect, String]
  def dbPassword: ConfigValue[Effect, String]

  def emailHost: ConfigValue[Effect, String]
  def emailUsername: ConfigValue[Effect, String]
  def emailPassword: ConfigValue[Effect, String]
  def emailFromEmail: ConfigValue[Effect, String]
  def emailFromName: ConfigValue[Effect, String]

  def sendInternalErrorMessages: ConfigValue[Effect, Boolean]

  def load =
    (ConfigValue.default(name), port, host, baseUrl,
      migrate, dbUri, dbUsername, dbPassword,
      emailHost, emailUsername, emailPassword, emailFromEmail, emailFromName,
      sendInternalErrorMessages)
      .parMapN(Env.apply)

}

class ProductionEnv() extends EnvLoader {
  override val name: String = "production"

  override def migrate    = env("BN_MIGRATE").map(_.equals("yes"))
  override def dbUri      = env("BN_DB_URI")
  override def dbUsername = env("BN_DB_USER")
  override def dbPassword = env("BN_DB_PASSWORD")

  override def emailHost: ConfigValue[Effect, String] = env("BN_EMAIL_HOST")
  override def emailUsername: ConfigValue[Effect, String] = env("BN_EMAIL_USERNAME")
  override def emailPassword: ConfigValue[Effect, String] = env("BN_EMAIL_PASSWORD")
  override def emailFromEmail: ConfigValue[Effect, String] = env("BN_EMAIL_FROM_EMAIL")
  override def emailFromName: ConfigValue[Effect, String] = env("BN_EMAIL_FROM_NAME")

  override def sendInternalErrorMessages: ConfigValue[Effect, Boolean] = ConfigValue.default(false)
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

  override def sendInternalErrorMessages: ConfigValue[Effect, Boolean] = ConfigValue.default(true)
}
