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

object Config {

  def source: String = Source.fromFile("/home/alexander/Projects/DMS/out/artifacts/dms_jar/config.json").mkString

  val get = parse[Config](source)

}