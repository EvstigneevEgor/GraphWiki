package db.models

final case class WikiPage(id: Long, description: String, inWork: Boolean, executed: Boolean) {}

object WikiPage {
  def apply(description: String, inWork: Boolean, executed: Boolean): WikiPage = new WikiPage(0L, description, inWork, executed)

  def tupled: ((Long, String, Boolean, Boolean)) => WikiPage = {
    case ((x1, x2, x3, x4)) => apply(x1, x2, x3, x4)
  }
}
