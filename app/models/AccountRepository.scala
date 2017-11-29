/**
  * @author phuong_ds
  */
package models

import anorm._
import anorm.{ Macro, RowParser}
import play.api.db.DBApi
import play.api.libs.json.{JsValue, Json, Writes}
import javax.inject.Inject
import controllers.AccountData

case class Account (
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
  * in order to return data as json for the list screen we have to add those lines for converting List to Json type
  */
object Account {
  implicit val implicitAccountWrites = new Writes[Account] {
    def writes(account: Account): JsValue = {
      Json.obj(
        "name" -> account.name,
        "jp_name" -> account.jp_name,
        "username" -> account.username,
        "password" -> account.password,
        "email" -> account.email,
        "website" -> account.website,
        "age" -> account.age,
        "gender" -> account.gender
      )
    }
  }
}

/**
  * since play2.6 play.api.db.DB => play.api.db.DBApi
  * for play version < 2.6, once we import play.api.db.DB we can use DB directly but for 2.6 we have to inject DBApi
  * @param dbapi
  */
class AccountRepository @Inject()(dbapi: DBApi){
  var strTable = "account"
  var strIdCol = "id"
  //set db connection from "default" dbsource
  private val db = dbapi.database("default")

  val parser: RowParser[Account] = Macro.namedParser[Account]
  /* much shorter than play2.3 in play2.3 Generated as:
  get[String]("name") ~ get[Option[Int]]("year") map {case name ~ year => Info(name, year)}
  */

  /**
    *
    * @return
    */
  def findAll() = {
    db.withConnection{ implicit connection =>
      SQL(s"SELECT * FROM $strTable ORDER BY updated_on DESC").as(parser.*)
    }

  }

  /**
    *
    */
  def insert(account:AccountData) = {
    db.withConnection{ implicit connection =>
      val sql =
        s"""INSERT INTO $strTable (name,jp_name,username,password,email,website,age,gender,created_on,updated_on)
           |VALUES (
           |${account.name},
           |${account.jp_name},
           |${account.username},
           |${account.password},
           |${account.email},
           |${account.website},
           |${account.age},
           |${account.gender},
           |NOW(),
           |NOW(),
           |)""".stripMargin
      SQL(sql).execute()
    }

  }

}
