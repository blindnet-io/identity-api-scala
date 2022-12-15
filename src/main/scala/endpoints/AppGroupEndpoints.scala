package io.blindnet.identity
package endpoints

import endpoints.objects.*
import services.AppGroupService

import cats.effect.*
import io.circe.generic.auto.*
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*

import java.util.UUID

class AppGroupEndpoints(service: AppGroupService, authenticator: AccountAuthenticator) {
  private val base = authenticator.withBaseEndpoint(endpoint.tag("Application groups").in("app-groups")).secureEndpoint

  val create: ApiEndpoint =
    base.summary("Create an application group")
      .post
      .in(jsonBody[CreateAppGroupPayload])
      .out(jsonBody[UUID])
      .serverLogicSuccess(service.create)

  val get: ApiEndpoint =
    base.summary("Get an application group")
      .get
      .in(path[UUID]("id"))
      .out(jsonBody[AppGroupInfoPayload])
      .serverLogicSuccess(service.get)

  val getApps: ApiEndpoint =
    base.summary("Get all applications of a group")
      .get
      .in(path[UUID]("id"))
      .in("applications")
      .out(jsonBody[List[ApplicationInfoPayload]])
      .serverLogicSuccess(service.getApps)

  val getAll: ApiEndpoint =
    base.summary("Get all application groups")
      .get
      .out(jsonBody[List[AppGroupInfoPayload]])
      .serverLogicSuccess(service.getAll)

  val update: ApiEndpoint =
    base.summary("Update an application group")
      .post
      .in(path[UUID]("id"))
      .in(jsonBody[UpdateAppGroupPayload])
      .serverLogicSuccess(service.update)

  val delete: ApiEndpoint =
    base.summary("Delete an application group")
      .delete
      .in(path[UUID]("id"))
      .serverLogicSuccess(service.delete)

  val list: List[ApiEndpoint] = List(
    create,
    get,
    getApps,
    getAll,
    update,
    delete,
  )
}
