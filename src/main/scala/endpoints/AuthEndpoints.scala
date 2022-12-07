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

class AuthEndpoints(service: AuthService, authenticator: AccountAuthenticator) {
  private val publicBase = endpoint.tag("Auth").in("auth")
  private val authedBase = authenticator.withBaseEndpoint(publicBase).secureEndpoint

  val login: ApiEndpoint =
    publicBase.summary("Login using an email address and password")
      .post
      .in("login")
      .in(jsonBody[LoginPayload])
      .out(jsonBody[LoginResponsePayload])
      .serverLogicSuccess(service.login)

  val register: ApiEndpoint =
    publicBase.summary("Register using an email address and password")
      .post
      .in("register")
      .in(jsonBody[RegisterPayload])
      .out(jsonBody[LoginResponsePayload])
      .serverLogicSuccess(service.register)

  val status: ApiEndpoint =
    authedBase.summary("Get account status")
      .get
      .in("status")
      .out(jsonBody[AccountStatusPayload])
      .serverLogicSuccess(service.status)

  val verify: ApiEndpoint =
    authedBase.summary("Verify email")
      .post
      .in("verify")
      .in(jsonBody[VerifyEmailPayload])
      .serverLogicSuccess(service.verify)

  val resendVerification: ApiEndpoint =
    authedBase.summary("Resend verification email")
      .post
      .in("resend-verification")
      .serverLogicSuccess(service.resendVerification)

  val updateEmail: ApiEndpoint =
    authedBase.summary("Update email")
      .post
      .in("update-email")
      .in(jsonBody[UpdateEmailPayload])
      .serverLogicSuccess(service.updateEmail)

  val changePassword: ApiEndpoint =
    authedBase.summary("Change password")
      .post
      .in("change-password")
      .in(jsonBody[ChangePasswordPayload])
      .out(jsonBody[LoginResponsePayload])
      .serverLogicSuccess(service.changePassword)

  val list: List[ApiEndpoint] = List(
    login,
    register,
    status,
    verify,
    resendVerification,
    updateEmail,
    changePassword,
  )
}
