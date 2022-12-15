package io.blindnet.identity
package db

import cats.effect.IO
import org.flywaydb.core.Flyway

object Migrator {
  def migrate(env: Env): IO[Unit] = IO {

    Flyway
      .configure()
      .dataSource(env.dbUri, env.dbUsername, env.dbPassword)
      .group(true)
      .load()
      .migrate()
  }

}
