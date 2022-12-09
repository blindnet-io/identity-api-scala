package io.blindnet.identity
package endpoints.objects

import io.circe.Decoder
import io.circe.generic.semiauto.*

case class LoginPayload(email: String, password: String)

object LoginPayload {
  given Decoder[LoginPayload] = deriveDecoder[LoginPayload]
}
