package io.blindnet.identity
package endpoints.objects

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class CreateAppGroupPayload(
  name: String,
  key: String,
)

object CreateAppGroupPayload {
  given Decoder[CreateAppGroupPayload] = deriveDecoder[CreateAppGroupPayload]
}
