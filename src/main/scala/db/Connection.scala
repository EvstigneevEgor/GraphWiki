package db
import slick.jdbc.PostgresProfile.profile.api._

object Connection {
    val db = Database.forConfig("postgres")
    val PageSize = 100

    implicit class ConnectionUtils[T, U](que: Query[T, U, Seq]) {
        def getPage(page: Long): Query[T, U, Seq] = {
            que.drop(page * PageSize).take(PageSize)
        }
    }
}
