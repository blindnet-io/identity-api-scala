package io.blindnet.identity
package endpoints

import endpoints.objects.*
import services.AuthService

import cats.effect.*
import io.circe.generic.auto.*
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*

import java.util.UUID

class AuthEndpoints(service: AuthService) {
  private val base = endpoint.tag("Auth").in("auth")

  val login: ApiEndpoint =
    base.summary("Login using an email address and password")
      .post
      .in("login")
      .in(jsonBody[LoginPayload])
      .out(jsonBody[LoginResponsePayload])
      .serverLogicSuccess(service.login)

  val register: ApiEndpoint =
    base.summary("Register using an email address and password")
      .post
      .in("register")
      .in(jsonBody[RegisterPayload])
      .out(jsonBody[LoginResponsePayload])
      .serverLogicSuccess(service.register)

  val list: List[ApiEndpoint] = List(
    login,
    register
  )
}
