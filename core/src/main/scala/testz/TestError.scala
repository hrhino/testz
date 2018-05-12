package testz

sealed trait TestError
final case class ExceptionThrown(thrown: Throwable) extends TestError
final case class Failure(failureAsString: String) extends TestError
// TODO: use pretty-printer
