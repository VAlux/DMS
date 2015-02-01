import java.nio.file.FileSystemNotFoundException

import spray.json._

import scala.io.Source

/**
 * Created by alexander on 16.01.15.
 */

case class ConfigContent(
                 username: String,
                 password: String,
                 subject: String,
                 text: String,
                 recipients: Seq[String],
                 properties: Map[String, String],
                 attachments: Seq[String])

object ConfigJSONParsingProtocol extends DefaultJsonProtocol {
  implicit val configFormat = jsonFormat7(ConfigContent)
}

object Config {

  import ConfigJSONParsingProtocol._

  private var source: String = null
  
  def get = JsonParser(source).convertTo[ConfigContent]

  def apply(path: String) = {
    try {
      println(s"reading config form: $path")
      source = Source.fromFile(path).mkString
    } catch {
      case ex: FileSystemNotFoundException => println(ex.getLocalizedMessage)
    }
  }

  override def toString: String = {
      s"From: ${get.username}\n" +
      s"Recipients: ${get.recipients}\n" +
      s"Subject: ${get.subject}\n" +
      s"Text: ${get.text}\n" +
      s"Attachments: ${get.attachments}"
  }
}