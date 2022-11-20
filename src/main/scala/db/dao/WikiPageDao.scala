package db.dao

import db.Connection.ConnectionUtils
import db.models.{PageEdge, PageWithEdge, WikiPage}
import db.{Connection, TableWikiGraph}
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

object WikiPageDao {
  implicit val exc =
    ExecutionContext.fromExecutor(Executors.newWorkStealingPool(12))

  import TableWikiGraph.profile.api._

  val table: TableQuery[TableWikiGraph.WikiPageTable] = TableWikiGraph.wikiPageTable

  /**
   * save Page if they doesnt have id
   *
   * @param wikiPage
   * @return
   */
  def insertIfNotExists(wikiPage: WikiPage) = {
    val insertQuery = table.filter(_.description===wikiPage.description).exists.result.flatMap{exists=>
      if(!exists)
        table.returning(table) += wikiPage.copy(id = 0L)
      else
        DBIO.successful(wikiPage) // no-op
    }
    Connection.db.run(insertQuery)
  }
//  def insert(wikiPage: WikiPage) = {
//    Option(wikiPage).filter(_.id == 0L).map { wp =>
//      val insertQuery = table.returning(table) += wp
//      Connection.db.run(insertQuery)
//    } getOrElse
//      Future.successful(wikiPage)
//  }

  def insertPageWithEdge(pageWithEdge: PageWithEdge) = {
    val fromPage = pageWithEdge.wikiPage
    val edges = pageWithEdge.edges


    for {
      savedFromPade <- insertIfNotExists(fromPage)
      savedToPages <- Future.sequence(edges.map(insertIfNotExists))
      savedEdge <- PageEdgeDao.insertAll(savedToPages.map(savedToPage => PageEdge(savedFromPade.id, savedToPage.id)))
    } yield (savedEdge)

  }

  def selectByDescription(description: String): Future[Seq[WikiPage]] = {
    Connection.db.run(table.filter(_.description === description).result)
  }

  def selectById(id: Long): Future[Seq[WikiPage]] = {
    Connection.db.run(table.filter(_.id === id).result)
  }

  def selectPage(page: Long): Future[Seq[WikiPage]] = {
    Connection.db.run(table.sortBy(_.id).getPage(page).result)
  }


}


