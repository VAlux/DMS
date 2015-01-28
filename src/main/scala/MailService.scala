import java.util.Properties
import javax.activation.{DataHandler, DataSource, FileDataSource}
import javax.mail._
import javax.mail.internet.{InternetAddress, MimeBodyPart, MimeMessage, MimeMultipart}

/**
* Created by alexander on 26.12.14.
*/
package object MailService {

  object sender {

    def send() {

      val properties = new Properties()
      for((property, value) <- Config.get.properties){
        properties.put(property, value)
      }

      val session = Session.getInstance(properties, new Authenticator {
        override def getPasswordAuthentication: PasswordAuthentication =
          new PasswordAuthentication(Config.get.username, Config.get.password)
      })

      try {
        val message: Message = new MimeMessage(session)
        message.setFrom(new InternetAddress("olex3000sp@gmail.com"))

        for(recipient <- Config.get.recipients) {
          message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient))
        }

        message.setSubject(Config.get.subject)
        var bodyPart: BodyPart = new MimeBodyPart
        bodyPart.setText("this is a message body part")

        val multipart: Multipart = new MimeMultipart

        multipart.addBodyPart(bodyPart)

        for(attachment <- Config.get.attachments) {
          bodyPart = new MimeBodyPart
          val filename: String = attachment
          val source: DataSource = new FileDataSource(filename)
          bodyPart.setDataHandler(new DataHandler(source))
          bodyPart.setFileName(filename)
          multipart.addBodyPart(bodyPart)
        }

        message.setContent(multipart)
        Transport.send(message)
      } catch {
        case ex: MessagingException => println(ex.getLocalizedMessage)
        case _: NullPointerException => println("Error: probably malformed config")
      }
    }
  }
}