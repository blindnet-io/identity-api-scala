package io.blindnet.identity
package mail

import cats.effect.IO

case class MailTemplates(
    verify: MailTemplate
)

object MailTemplates {
  def load(): IO[MailTemplates] =
    for {
      verify <- MailTemplate.load("Account verification", "verify")
    } yield new MailTemplates(verify)

}
