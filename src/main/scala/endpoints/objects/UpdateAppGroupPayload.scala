package io.blindnet.identity
package endpoints.objects

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class UpdateAppGroupPayload(
  name: Option[String],
  key: Option[String],
)

object UpdateAppGroupPayload {
  given Decoder[UpdateAppGroupPayload] = deriveDecoder[UpdateAppGroupPayload]
}
