

/**
 * Created by alexander on 17.01.15.
 */
object PostOffice extends App {
  try {
    MailService send args(0)
  } catch {
    case ex: ExceptionInInitializerError => println("Error: Not-Existing or malformed config.")
  }
}
