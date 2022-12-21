package io.blindnet.identity
package endpoints.objects

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

import java.util.UUID

case class CreateApplicationPayload(
    group_id: UUID,
    name: String
)

object CreateApplicationPayload {
  given Decoder[CreateApplicationPayload] = deriveDecoder[CreateApplicationPayload]
}
