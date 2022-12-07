package io.blindnet.identity
package endpoints.objects

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class UpdateEmailPayload(email: String)

object UpdateEmailPayload {
  given Decoder[UpdateEmailPayload] = deriveDecoder
}
