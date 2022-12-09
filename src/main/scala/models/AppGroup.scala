package io.blindnet.identity
package models

import io.circe.*
import io.circe.generic.semiauto.*

import java.util.UUID

case class AppGroup(
  id: UUID,
  accountId: UUID,
  name: String,
  key: String,
)

object AppGroup {
  implicit val encoder: Encoder[AppGroup] = deriveEncoder[AppGroup]
  implicit val decoder: Decoder[AppGroup] = deriveDecoder[AppGroup]
}
