import spray.json._

import scala.io.Source

/**
 * Created by alexander on 16.01.15.
 */

case class Config(
                 username: String,
                 password: String,
                 subject: String,
                 text: String,
                 recipients: Seq[String],
                 properties: Map[String, String],
                 attachments: Seq[String])

object ConfigJSONParsingProtocol extends DefaultJsonProtocol {
  implicit val configFormat = jsonFormat7(Config.apply)
}

object Config {

  import ConfigJSONParsingProtocol._

  def source: String = Source.fromFile("/home/alexander/Projects/DMS/out/artifacts/dms_jar/config.json").mkString

  val jsonAST = JsonParser(source)

  val get = jsonAST.convertTo[Config]

  override def toString: String = {
      s"From: ${get.username}\n" +
      s"Recipients: ${get.recipients}\n" +
      s"Subject: ${get.subject}\n" +
      s"Text: ${get.text}\n" +
      s"Attachments: ${get.attachments}"
  }
}