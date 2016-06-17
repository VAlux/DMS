import spray.json._

import scala.io.Source

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

  private def loadParserInput(path: String): ParserInput = {
    println(s"reading config form: $path")
    ParserInput(Source.fromFile(path).mkString)
  }

  def parseConfig(path: String): ConfigContent = {
    parsed = JsonParser(loadParserInput(path)).convertTo[ConfigContent]
    parsed
  }
  
  private var parsed: ConfigContent = _

  override def toString: String = {
      s"From: ${parsed.username}\n" +
      s"Recipients: ${parsed.recipients}\n" +
      s"Subject: ${parsed.subject}\n" +
      s"Text: ${parsed.text}\n" +
      s"Attachments: ${parsed.attachments}"
  }
}