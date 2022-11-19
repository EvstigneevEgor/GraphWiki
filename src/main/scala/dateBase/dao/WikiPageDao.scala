package dateBase.dao

import dateBase.Connection.ConnectionUtils
import dateBase.models.WikiPage
import dateBase.{Connection, TableWikiGraph}
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

object WikiPageDao {
  object Table extends TableWikiGraph(PostgresProfile)

  implicit val exc =
    ExecutionContext.fromExecutor(Executors.newWorkStealingPool(12))

  import Table.profile.api._

  def insert(wikiPage: WikiPage) = {
    val table: TableQuery[WikiPageDao.Table.WikiPageTable] = Table.wikiPageTable

    val insertQuery = table.returning(table) += wikiPage
    println(table + " " + insertQuery)

    Connection.db.run(insertQuery)
  }

  def selectByDescription(description: String): Future[Seq[WikiPage]] = {
    val table: TableQuery[WikiPageDao.Table.WikiPageTable] = Table.wikiPageTable
    Connection.db.run(table.filter(_.description === description).result)
  }

  def selectById(id: Long): Future[Seq[WikiPage]] = {
    val table: TableQuery[WikiPageDao.Table.WikiPageTable] = Table.wikiPageTable
    Connection.db.run(table.filter(_.id === id).result)
  }

  def selectPage(page: Long): Future[Seq[WikiPage]] = {
    println("hee")
    val table: TableQuery[WikiPageDao.Table.WikiPageTable] = Table.wikiPageTable
    println("heeq2")
    Connection.db.run(table.sortBy(_.id).getPage(page).result)
  }


}


