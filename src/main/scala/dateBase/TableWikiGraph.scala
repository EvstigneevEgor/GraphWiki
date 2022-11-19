package dateBase

import dateBase.models.WikiPage
import slick.jdbc.PostgresProfile
import slick.sql.SqlProfile.ColumnOption.SqlType

class TableWikiGraph(val profile: PostgresProfile) {

  import profile.api._

  val wikiPageTable = TableQuery[WikiPageTable]

  class WikiPageTable(tag: Tag) extends Table[WikiPage](tag, "wikiPage") {
    def id = column[Long]("id",SqlType("SERIAL"), O.PrimaryKey, O.AutoInc, O.Unique)//(3 часа поиска 🤤)В постгесе должна быть такая настройка авто увеличение
    def description = column[String]("description")
    def executed = column[Boolean]("executed")
    override def * = {(id, description, executed) <> (WikiPage.tupled, WikiPage.unapply)}
  }
}
