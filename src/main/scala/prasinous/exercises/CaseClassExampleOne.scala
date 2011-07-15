package prasinous.exercises

case class Alpha(a: Double, b: Long)
case class Bravo(x: String, y: Int, z: Option[Alpha])


object CaseClassExampleOne extends App {

  val alpha = Alpha(a = 1.4, b = 99L)
  val bravo = Bravo(x = "baker", y = 2, z = Some(alpha))

  println("""

  %s

  %s

  """.format(
    CaseClassPrinter(alpha),
    CaseClassPrinter(bravo)
  ))


}

object CaseClassPrinter {
  def apply(x: AnyRef with Product) = {

    val sb = StringBuilder.newBuilder

    sb ++= "class: '%s' \n".format(x.getClass.getName)
    sb ++= "arity: %d \n".format(x.productArity)
    sb ++= "prefix: '%s' \n".format(x.productPrefix)
    for (i <- 0 until x.productArity) {
      val elem = x.productElement(i)
      sb ++= "[%d]\t%s\t\t%s\n".format(i,
        elem.asInstanceOf[AnyRef].getClass.getName,
        elem)
    }
    sb ++= "toString: %s".format(x.toString)
    sb.result()
  }
}
