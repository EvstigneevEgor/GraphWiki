package db

import db.models.{PageEdge, WikiPage}
import slick.jdbc.PostgresProfile
import slick.sql.SqlProfile.ColumnOption.SqlType

class TableWikiGraph(val profile: PostgresProfile) {

  import profile.api._

  lazy val wikiPageTable = TableQuery[WikiPageTable]
  lazy val pageEdgeTable = TableQuery[PageEdgeTable]

  class WikiPageTable(tag: Tag) extends Table[WikiPage](tag, "wikiPage") {
    def id = column[Long]("id", SqlType("SERIAL"), O.PrimaryKey, O.AutoInc, O.Unique) //(3 часа поиска 🤤)В постгесе должна быть такая настройка авто увеличение

    def description = column[String]("description") //todo unique

    def executed = column[Boolean]("executed")
    def inWork = column[Boolean]("in_work")

    override def * = {
      (id, description,inWork , executed) <> (WikiPage.tupled, WikiPage.unapply)
    }
  }

  class PageEdgeTable(tag: Tag) extends Table[PageEdge](tag, "page_edge") {
    def fromId = column[Long]("from")

    def toId = column[Long]("to")

    override def * = {
      (fromId, toId) <> (PageEdge.tupled, PageEdge.unapply)
    }
    def from_fk = foreignKey("from_fk", fromId, TableQuery[WikiPageTable])(_.id, ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)
    def to_fk = foreignKey("to_fk", fromId, TableQuery[WikiPageTable])(_.id, ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)
  }
}
object TableWikiGraph extends TableWikiGraph(PostgresProfile)
