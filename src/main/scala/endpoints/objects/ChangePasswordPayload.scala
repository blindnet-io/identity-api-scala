package io.blindnet.identity
package endpoints.objects

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class ChangePasswordPayload(password: String)

object ChangePasswordPayload {
  given Decoder[ChangePasswordPayload] = deriveDecoder
}
