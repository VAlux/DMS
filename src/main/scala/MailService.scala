import java.util.Properties

import javax.activation.{DataHandler, DataSource, FileDataSource}
import javax.mail._
import javax.mail.internet.{InternetAddress, MimeBodyPart, MimeMessage, MimeMultipart}

import scala.util.{Failure, Success, Try}

class MailService(private[this] val configPath: String) {

  private lazy val parsedConfig: ConfigContent = Config.parseConfig(configPath)

  private def initSession: Session = {
    val properties = new Properties()

    parsedConfig.properties foreach { case (property, value) =>
      properties.put(property, value)
    }

    Session.getInstance(properties, new Authenticator {
      override def getPasswordAuthentication: PasswordAuthentication =
        new PasswordAuthentication(parsedConfig.username, parsedConfig.password)
    })
  }

  private def createMessage: Message = {
    val session = initSession
    val message: Message = new MimeMessage(session)
    val multipart: Multipart = new MimeMultipart

    parsedConfig.recipients foreach { recipient =>
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient))
    }

    parsedConfig.attachments
      .map(createBodyPart)
      .foreach(multipart.addBodyPart)

    val textPart = new MimeBodyPart()
    textPart.setText(parsedConfig.text)
    multipart.addBodyPart(textPart)

    message.setContent(multipart)
    message.setSubject(parsedConfig.subject)

    message
  }

  def createBodyPart(attachment: String): BodyPart = {
    val bodyPart = new MimeBodyPart
    val source: DataSource = new FileDataSource(attachment)
    bodyPart.setDataHandler(new DataHandler(source))
    bodyPart.setFileName(attachment)
    bodyPart
  }

  def send(): Unit = {
    println(s"Sending message with following config: \n\n${showConfig(parsedConfig)}\n")
    Try(Transport.send(createMessage)) match {
      case _: Success[_] => println("Message successfully sent!")
      case Failure(ex) => println(ex.getMessage)
    }
  }

  def showConfig(parsed: ConfigContent): String = {
    s"From: ${parsed.username}\n" +
      s"Recipients: ${parsed.recipients}\n" +
      s"Subject: ${parsed.subject}\n" +
      s"Text: ${parsed.text}\n" +
      s"Attachments: ${parsed.attachments}"
  }
}