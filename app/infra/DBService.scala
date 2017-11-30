/**
  * @author phuong_ds
  */
package infra

import javax.inject.Inject
import java.security.MessageDigest
import anorm.{Macro, RowParser, _}
import play.api.db.DBApi

case class Account (
                     var id: Int,
                     var name: Option[String],
                     var jp_name: Option[String],
                     var username: String,
                     var password: String,
                     var email: String,
                     var website: Option[String],
                     var age: Option[Int],
                     var gender: Int
                   )
/**
  * since play2.6 play.api.db.DB => play.api.db.DBApi
  * for play version < 2.6, once we import play.api.db.DB we can use DB directly but for 2.6 we have to inject DBApi
  * @param dbapi
  */
class DBService @Inject()(dbapi: DBApi){
  //set db connection from "default" dbsource
  private val db = dbapi.database("default")
  val parser: RowParser[Account] = Macro.namedParser[Account]

  def genData() = {
    def md5(s: String) = {
      MessageDigest.getInstance("MD5").digest(s.getBytes)
    }
    db.withConnection{implicit connection =>
      val rs = SQL(s"SELECT * FROM account ORDER BY updated_on DESC").as(parser.*)
      var fullSql = ""
      rs.foreach(row =>{
        val username = row.name.getOrElse("").replace(" ","").toLowerCase
        val email = username+"@gmail.com"
        val password = md5("demo")
        val website = s"http://www.$username.com"
        val id = row.id
        val sql = s"""UPDATE account SET
                     |email='$email',
                     |username='$username',
                     |password='$password',
                     |website='$website'
                     | WHERE id = '$id';""".stripMargin
        fullSql += sql
      })
      //execute sql
      SQL(fullSql).executeUpdate()
    }
  }
}
