package dateBase.models

final case class WikiPage(id: Long, description: String, executed: Boolean) {}

object WikiPage {
  def apply(description: String, executed: Boolean): WikiPage = new WikiPage(0L, description, executed)

  def tupled: ((Long, String, Boolean)) => WikiPage = {
    case ((x1, x2, x3)) => apply(x1, x2, x3)
  }
}
