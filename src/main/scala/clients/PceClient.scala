package io.blindnet.identity
package clients

import cats.effect.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.client.*
import org.http4s.circe.*
import java.util.UUID

case class CreateApplicationPayload(
    app_id: UUID
)

class PceClient(env: Env, client: Client[IO]) {

  def createApp(id: UUID): IO[Unit] =
    client
      .successful(
        Request[IO](
          method = Method.PUT,
          uri = env.pceUri / "v0" / "admin" / "applications",
          headers = Headers("Authorization" -> s"Bearer ${env.pceToken.value}")
        ).withEntity(CreateApplicationPayload(id).asJson)
      )
      .flatMap {
        case true  => IO.unit
        case false => IO.raiseError(new Exception(s"PCE failed creating the app $id"))
      }

}
