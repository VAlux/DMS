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
  implicit val configFormat: RootJsonFormat[ConfigContent] = jsonFormat7(ConfigContent)
}

object Config {

  import ConfigJSONParsingProtocol._

  private def loadParserInput(path: String): ParserInput = {
    println(s"reading config form: $path")
    ParserInput(Source.fromFile(path).mkString)
  }

  def parseConfig(path: String): ConfigContent = {
    JsonParser(loadParserInput(path)).convertTo[ConfigContent]
  }
}