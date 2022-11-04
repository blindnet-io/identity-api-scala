package io.blindnet.identity

import db.Migrator

import cats.effect.*

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      env <- Env.get
      _   <- Migrator.migrate(env)
      _   <- ServerApp(env).server.use(_ => IO.never)
    } yield ExitCode.Success

}
