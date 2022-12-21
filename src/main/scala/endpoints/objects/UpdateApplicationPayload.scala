package io.blindnet.identity
package endpoints.objects

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class UpdateApplicationPayload(
    name: Option[String]
)

object UpdateApplicationPayload {
  given Decoder[UpdateApplicationPayload] = deriveDecoder[UpdateApplicationPayload]
}
