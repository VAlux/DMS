import java.util.Properties
import javax.activation.{DataHandler, DataSource, FileDataSource}
import javax.mail._
import javax.mail.internet.{InternetAddress, MimeBodyPart, MimeMessage, MimeMultipart}

import scala.util.{Failure, Success, Try}

class MailService(private[this] val configPath: String) {

  private lazy val parsedConfig = Config.parseConfig(configPath)

  private def initSession: Session = {

    val properties = new Properties()

    parsedConfig.properties foreach { case(property, value) =>
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
    var bodyPart: BodyPart = new MimeBodyPart
    val multipart: Multipart = new MimeMultipart

    for(recipient <- parsedConfig.recipients) {
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient))
    }

    parsedConfig.recipients foreach { recipient =>
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient))
    }

    message.setSubject(parsedConfig.subject)
    bodyPart.setText(parsedConfig.text)
    multipart.addBodyPart(bodyPart)

    parsedConfig.attachments foreach { attachment =>
      bodyPart = new MimeBodyPart
      val filename: String = attachment
      val source: DataSource = new FileDataSource(filename)
      bodyPart.setDataHandler(new DataHandler(source))
      bodyPart.setFileName(filename)
      multipart.addBodyPart(bodyPart)
    }

    message.setContent(multipart)

    message
  }

  def send(): Unit = {
    println(s"Sending message with following config: \n\n${Config.toString}\n")
    Try(Transport.send(createMessage)) match {
      case _: Success => println("Message successfully sent!")
      case Failure(ex) => println(ex.getMessage)
    }
  }
}