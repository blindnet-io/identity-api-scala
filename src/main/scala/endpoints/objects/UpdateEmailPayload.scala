package io.blindnet.identity
package endpoints.objects

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class UpdateEmailPayload(current_password: String, email: String)

object UpdateEmailPayload {
  given Decoder[UpdateEmailPayload] = deriveDecoder
}
