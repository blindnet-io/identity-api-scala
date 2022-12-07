package io.blindnet.identity
package mail

import cats.data.EitherT
import cats.effect.*
import org.apache.commons.mail.*

import scala.util.Try

case class Mail(
  from: Sender,
  to: List[String],
  subject: String,
  message: String,
  richMessage: Option[String] = None,
) {
  def send(config: MailConfig): IO[Unit] = IO {
    val commons: Email = richMessage match
      case Some(value) => new HtmlEmail().setHtmlMsg(value).setTextMsg(message)
      case None => new SimpleEmail().setMsg(message)
    commons.setSubject(subject)

    from.name match
      case Some(name) => commons.setFrom(from.address, name)
      case None => commons.setFrom(from.address)
    to.foreach(commons.addTo)

    commons.setAuthentication(config.username, config.password)
    commons.setHostName(config.host)
    commons.setSmtpPort(config.port)
    commons.setStartTLSEnabled(true)

    commons.send()
  }
}
