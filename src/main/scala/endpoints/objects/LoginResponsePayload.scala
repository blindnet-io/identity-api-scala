package io.blindnet.identity
package endpoints.objects

import io.circe.Decoder
import io.circe.generic.semiauto.*

case class LoginResponsePayload(
    token: String,
    status: AccountStatusPayload
)

object LoginResponsePayload {
  given Decoder[LoginResponsePayload] = deriveDecoder[LoginResponsePayload]
}
