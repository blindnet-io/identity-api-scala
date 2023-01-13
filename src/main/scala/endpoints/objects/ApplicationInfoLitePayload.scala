package io.blindnet.identity
package endpoints.objects

import models.Application

import io.circe.*
import io.circe.generic.semiauto.*

import java.util.UUID

case class ApplicationInfoLitePayload(
    id: UUID,
    name: String
)

object ApplicationInfoLitePayload {
  def apply(app: Application): ApplicationInfoLitePayload =
    new ApplicationInfoLitePayload(app.id, app.name)

  implicit val encoder: Encoder[ApplicationInfoLitePayload] =
    deriveEncoder[ApplicationInfoLitePayload]

}
