package io.blindnet.identity
package models

import io.circe.*
import io.circe.generic.semiauto.*

import java.util.UUID

case class NewApplication(
    id: UUID,
    groupId: UUID,
    name: String
)

object NewApplication {
  implicit val encoder: Encoder[NewApplication] = deriveEncoder[NewApplication]
  implicit val decoder: Decoder[NewApplication] = deriveDecoder[NewApplication]
}
