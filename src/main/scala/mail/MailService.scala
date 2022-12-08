package io.blindnet.identity
package mail

import cats.data.EitherT
import cats.effect.*
import org.apache.commons.mail.*

import scala.util.Try

class MailService(config: MailConfig) {
  def send(mail: Mail): IO[Unit] = IO {
    val commons: Email = mail.richMessage match
      case Some(value) => new HtmlEmail().setHtmlMsg(value).setTextMsg(mail.message)
      case None => new SimpleEmail().setMsg(mail.message)
    commons.setSubject(mail.subject)

    mail.from.name match
      case Some(name) => commons.setFrom(mail.from.address, name)
      case None => commons.setFrom(mail.from.address)
    mail.to.foreach(commons.addTo)

    commons.setAuthentication(config.username, config.password)
    commons.setHostName(config.host)
    commons.setSmtpPort(config.port)
    commons.setStartTLSEnabled(true)

    commons.send()
  }
}
