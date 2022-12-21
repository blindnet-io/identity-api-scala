package io.blindnet.identity
package util

import cats.data.OptionT
import cats.effect.*

object Resources {
  def getAsStream(resource: String): IO[Option[fs2.Stream[IO, Byte]]] =
    IO(Option(getClass.getResourceAsStream(resource)))
      .map(_.map(s => fs2.io.readInputStream(IO.pure(s), 1000)))

  def readAsString(resource: String): IO[Option[String]] =
    OptionT(getAsStream(resource))
      .semiflatMap(_.through(fs2.text.utf8.decode).compile.string)
      .value

}
