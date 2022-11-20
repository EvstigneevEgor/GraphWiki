package db.dao

import db.Connection.ConnectionUtils
import db.models.{PageEdge, PageWithEdge, WikiPage}
import db.{Connection, TableWikiGraph}
import slick.jdbc.PostgresProfile

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

object PageEdgeDao {

  implicit val exc =
    ExecutionContext.fromExecutor(Executors.newWorkStealingPool(12))

  import TableWikiGraph.profile.api._

  val table = TableWikiGraph.pageEdgeTable

  /**
   * save edge Page
   * @param wikiPage
   * @return
   */
  def insert(pageEdge:PageEdge) = {
      val insertQuery = table.returning(table) += pageEdge
      Connection.db.run(insertQuery)
  }

  def insertAll(pageEdges:Seq[PageEdge])={
    Future.sequence(pageEdges.map(insert))
  }


}


