package io.blindnet.identity
package endpoints.objects

import models.AppGroup

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

import java.util.UUID

case class AppGroupInfoPayload(
    id: UUID,
    name: String,
    key: String
)

object AppGroupInfoPayload {
  given Encoder[AppGroupInfoPayload] = deriveEncoder[AppGroupInfoPayload]

  def apply(appGroup: AppGroup): AppGroupInfoPayload =
    new AppGroupInfoPayload(appGroup.id, appGroup.name, appGroup.key)

}
