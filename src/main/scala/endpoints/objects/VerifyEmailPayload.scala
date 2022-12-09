package io.blindnet.identity
package endpoints.objects

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class VerifyEmailPayload(token: String)

object VerifyEmailPayload {
  given Decoder[VerifyEmailPayload] = deriveDecoder[VerifyEmailPayload]
}
