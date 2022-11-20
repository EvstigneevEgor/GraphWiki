import db.Connection
import db.dao.WikiPageDao
import db.models.WikiPage
import slick.driver.PostgresDriver

import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicLong
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.io.Source.fromFile
import scala.util.{Failure, Success}

object Main extends App {

  implicit val exc =
    ExecutionContext.fromExecutor(Executors.newWorkStealingPool(12))
  val wikiPageDao = WikiPageDao
  feelDateBaseFromFile

  private def feelDateBaseFromFile = {
    val source = fromFile("/home/evstigneev/Документы/GraphWiki/SAVEINWORK.txt")
    val counter = new AtomicLong(0L)
    source.getLines().distinct.grouped(1000).foldLeft(0) { (acc, descriptions) =>
      val future: Future[Seq[WikiPage]] = Future.traverse(descriptions) { description =>
        val page = WikiPage(description, inWork = false, executed = false)
        wikiPageDao.insertIfNotExists(page)
      }
      Await.result(future, Duration.Inf)
      println(acc * 1000)
      acc + 1
    }
  }



}