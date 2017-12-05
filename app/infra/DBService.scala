/**
  * @author phuong_ds
  */
package infra

import javax.inject.Inject
import java.security.MessageDigest
import anorm.{Macro, RowParser, _}
import play.api.db.DBApi
import java.time.Instant
import scala.util.Random


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
  def md5(s: String) = {
    MessageDigest.getInstance("MD5").digest(s.getBytes)
  }

  def genData() = {
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
  def gen1mRecord() = {
    db.withConnection{implicit connection =>
      var sql = s"""INSERT INTO account (name,jp_name,email,username,password,website,age,gender) VALUES """
      var i = 1
      val alpha = "abcdefghijklmnopqrstuvwxyz"
      val size = alpha.size
      def randStr(n:Int) = (1 to n).map(x => alpha(Random.nextInt.abs % size)).mkString

      while(i<=1000000){
        val time : String = Instant.now.getEpochSecond.toString
        val name = randStr(10)+time
        val jp_name = "デモ"+name
        val age = 25
        val gender = 1
        val username = name.toLowerCase()
        val email = username+"@gmail.com"
        val password = md5("demo")
        val website = s"http://www.$username.com"
        sql +=s""" ('$name', '$jp_name','$email','$username','$password','$website','$age','$gender'), """.stripMargin
        i = i+1
      }
      //one more without comma at the end
      val time : String = Instant.now.getEpochSecond.toString
      sql +=s""" ('demo$time', 'デモ$time','demo$time@hotmail.com','demo$time','${md5("demo")}','http://www.demo$time.com','30','2') """.stripMargin
      SQL(sql).execute()
    }
  }
}
