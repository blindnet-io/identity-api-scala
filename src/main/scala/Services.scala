package io.blindnet.identity

import endpoints.*
import services.*

import cats.effect.IO
import io.blindnet.identityclient.auth.StAuthenticator
import org.http4s.HttpRoutes
import org.http4s.server.middleware.CORS
import org.http4s.server.websocket.WebSocketBuilder2
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.SwaggerUIOptions
import sttp.tapir.swagger.bundle.SwaggerInterpreter

class Services(repos: Repositories, env: Env) {
  private val authenticator = StAuthenticator(repos.accounts)
  private val verifiedAuthenticator = authenticator.flatMapSt(acc =>
    Either.cond(acc.verified, acc, "Unverified account."))

  private val appGroupService = AppGroupService(repos)
  private val applicationService = ApplicationService(repos)
  private val authService = AuthService(repos)

  private val appGroupEndpoints = AppGroupEndpoints(appGroupService, verifiedAuthenticator)
  private val applicationEndpoints = ApplicationEndpoints(applicationService, verifiedAuthenticator)
  private val authEndpoints = AuthEndpoints(authService, authenticator)

  private val apiEndpoints = List(
    appGroupEndpoints.list,
    applicationEndpoints.list,
    authEndpoints.list,
  ).flatten

  private val swaggerEndpoints =
    SwaggerInterpreter(swaggerUIOptions = SwaggerUIOptions.default.pathPrefix(List("swagger")))
      .fromServerEndpoints[IO](apiEndpoints, "Identity API", env.name)

  val routes: HttpRoutes[IO] =
    Http4sServerInterpreter[IO]().toRoutes(apiEndpoints ++ swaggerEndpoints)

}
