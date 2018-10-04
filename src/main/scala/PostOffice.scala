object PostOffice extends App {
  try {
    new MailService(args(0)).send()
  } catch {
    case _: ExceptionInInitializerError => println("Error: Not-Existing or malformed config.")
  }
}
