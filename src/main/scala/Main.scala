import dateBase.Connection
import dateBase.dao.WikiPageDao
import dateBase.models.WikiPage
import slick.driver.PostgresDriver

import java.util.concurrent.Executors
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object Main extends App {

  implicit val exc =
    ExecutionContext.fromExecutor(Executors.newWorkStealingPool(12))
  val wikiPageDao = WikiPageDao
  private val page = WikiPage("qweqwe", executed = true)
  println(page)
  private val eventualPage = wikiPageDao.insert(page)
  println(
    Await.result(eventualPage, Duration.Inf)
  )

}