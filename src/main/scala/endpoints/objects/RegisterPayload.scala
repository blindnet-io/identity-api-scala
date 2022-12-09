package io.blindnet.identity
package endpoints.objects

import io.circe.Decoder
import io.circe.generic.semiauto.*

case class RegisterPayload(email: String, password: String)

object RegisterPayload {
  given Decoder[RegisterPayload] = deriveDecoder[RegisterPayload]
}
