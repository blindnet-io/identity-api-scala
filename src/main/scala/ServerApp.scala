package io.blindnet.identity

import cats.effect.*
import cats.implicits.*
import org.http4s.HttpApp
import org.http4s.blaze.server.*
import org.http4s.implicits.*
import org.http4s.server.*
import org.http4s.server.websocket.WebSocketBuilder2
import org.http4s.syntax.*

class ServerApp {
  def app(services: Services): HttpApp[IO] =
    Router(
      "/v1" -> services.routes
    ).orNotFound

  val server: Resource[IO, Server] =
    for {
      repos <- Repositories()
      services = Services(repos)
      server <- BlazeServerBuilder[IO]
        .bindHttp(Env.get.port, Env.get.host)
        .withHttpApp(app(services))
        .resource
    } yield server
}
