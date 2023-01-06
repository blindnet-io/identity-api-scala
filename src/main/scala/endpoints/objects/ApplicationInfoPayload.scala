package io.blindnet.identity
package endpoints.objects

import models.Application

import io.circe.*
import io.circe.generic.semiauto.*

import java.util.UUID

case class ApplicationInfoPayload(
    id: UUID,
    name: String,
    key: String
)

object ApplicationInfoPayload {
  def apply(app: Application): ApplicationInfoPayload =
    new ApplicationInfoPayload(app.id, app.name, app.key)

  implicit val encoder: Encoder[ApplicationInfoPayload] = deriveEncoder[ApplicationInfoPayload]
}
