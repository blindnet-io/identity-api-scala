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

class ApplicationEndpoints(service: ApplicationService) {
  private val base = endpoint.tag("Applications").in("applications")

  val get: ApiEndpoint =
    base.summary("Get application info")
      .get
      .in(path[UUID]("id"))
      .out(jsonBody[ApplicationInfoPayload])
      .serverLogicSuccess(service.get)

  val list: List[ApiEndpoint] = List(
    get
  )
}
