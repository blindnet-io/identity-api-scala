package io.blindnet.identity
package mail

case class Mail(
  from: MailSender,
  to: List[String],
  subject: String,
  message: String,
  richMessage: Option[String] = None,
)

object Mail {
  def fromTemplate(template: MailTemplate)(from: MailSender, to: List[String]) =
    new Mail(from, to, template.subject, template.plain, template.rich)
}
