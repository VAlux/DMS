object PostOffice extends App {
  try {
    new MailService(args(0)).send()
  } catch {
    case ex: ExceptionInInitializerError => println("Error: Not-Existing or malformed config.")
  }
}
