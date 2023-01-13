package io.blindnet.identity
package endpoints.objects

import models.Application

import io.circe.*
import io.circe.generic.semiauto.*

import java.util.UUID

case class ApplicationInfoPayload(
    id: UUID,
    name: String,
    key: String,
    pceToken: String,
    dacToken: String
)

object ApplicationInfoPayload {
  def apply(app: Application, pceToken: String, dacToken: String): ApplicationInfoPayload =
    new ApplicationInfoPayload(app.id, app.name, app.key, pceToken, dacToken)

  implicit val encoder: Encoder[ApplicationInfoPayload] = deriveEncoder[ApplicationInfoPayload]
}
