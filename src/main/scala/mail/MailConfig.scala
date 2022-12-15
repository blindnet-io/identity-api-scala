package io.blindnet.identity
package mail

import org.apache.commons.mail.Email

case class MailConfig(
  username: String,
  password: String,
  host: String,
  port: Integer = 587,
)
