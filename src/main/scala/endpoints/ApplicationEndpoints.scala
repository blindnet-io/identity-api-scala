package io.blindnet.identity
package endpoints

import endpoints.objects.*
import services.ApplicationService

import cats.effect.*
import io.circe.generic.auto.*
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*

import java.util.UUID

class ApplicationEndpoints(service: ApplicationService, authenticator: AccountAuthenticator) {
  private val publicBase = endpoint.tag("Applications").in("applications")
  private val authedBase = authenticator.secureEndpoint(publicBase)

  val get: ApiEndpoint =
    publicBase
      .summary("Get application info")
      .get
      .in(path[UUID]("id"))
      .out(jsonBody[ApplicationInfoPayload])
      .serverLogicSuccess(service.get)

  val create: ApiEndpoint =
    authedBase
      .summary("Create an application")
      .post
      .in(jsonBody[CreateApplicationPayload])
      .out(jsonBody[UUID])
      .serverLogicSuccess(service.create)

  val update: ApiEndpoint =
    authedBase
      .summary("Update an application")
      .post
      .in(path[UUID]("id"))
      .in(jsonBody[UpdateApplicationPayload])
      .serverLogicSuccess(service.update)

  val delete: ApiEndpoint =
    authedBase
      .summary("Delete an application")
      .delete
      .in(path[UUID]("id"))
      .serverLogicSuccess(service.delete)

  val list: List[ApiEndpoint] = List(
    get,
    create,
    update,
    delete
  )

}
