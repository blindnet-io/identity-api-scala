package io.blindnet.identity
package models

import io.circe.*
import io.circe.generic.semiauto.*

import java.util.UUID

case class Application(
    id: UUID,
    groupId: UUID,
    name: String,
    key: String
)

object Application {
  implicit val encoder: Encoder[Application] = deriveEncoder[Application]
  implicit val decoder: Decoder[Application] = deriveDecoder[Application]
}
