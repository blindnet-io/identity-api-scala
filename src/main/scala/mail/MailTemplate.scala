package io.blindnet.identity
package mail

import util.Resources

import cats.effect.IO

case class MailTemplate(subject: String, plain: String, rich: Option[String]) {
  def map(f: String => String): MailTemplate =
    copy(subject = f(subject), plain = f(plain), rich = rich.map(f(_)))

}

object MailTemplate {
  def load(subject: String, name: String): IO[MailTemplate] =
    for {
      plain <- Resources
        .readAsString(s"/email/$name.txt")
        .map(_.getOrElse(throw new IllegalArgumentException(s"Missing template $name")))
      rich  <- Resources.readAsString(s"/email/$name.html")
    } yield new MailTemplate(subject, plain, rich)

}
