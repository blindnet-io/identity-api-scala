package io.blindnet.identity

import cats.effect.*
import cats.implicits.*
import io.blindnet.identity.mail.MailTemplates
import org.http4s.HttpApp
import org.http4s.blaze.server.*
import org.http4s.implicits.*
import org.http4s.server.*
import org.http4s.server.websocket.WebSocketBuilder2
import org.http4s.syntax.*

class ServerApp(env: Env) {
  def app(services: Services): HttpApp[IO] =
    Router(
      "/v1" -> services.routes
    ).orNotFound

  val server: Resource[IO, Server] =
    for {
      repos <- Repositories(env)
      templates <- Resource.eval(MailTemplates.load())
      services = Services(repos, env, templates)
      server <- BlazeServerBuilder[IO]
        .bindHttp(env.port, env.host)
        .withHttpApp(app(services))
        .resource
    } yield server

}
