/**
  * @author phuong_ds
  */
package models

import java.util.Date
import anorm._
import anorm.{ Macro, RowParser}
import play.api.db.DBApi
import play.api.libs.json.{JsValue, Json, Writes}
import javax.inject.Inject


case class Account (
                     var id: Int,
                     var name: String,
                     var jp_name: String,
                     var username: String,
                     var password: String,
                     var email: String,
                     var website: String,
                     var age: Int,
                     var gender: Int,
                     var is_deleted: Int,
                     var updated_on: Date
                   )

/**
  * in order to return data as json for the list screen we have to add those lines for converting List to Json type
  */
object Account {
  implicit val implicitAccountWrites = new Writes[Account] {
    def writes(account: Account): JsValue = {
      Json.obj(
        "id" -> account.id,
        "name" -> account.name,
        "jp_name" -> account.jp_name,
        "username" -> account.username,
        "password" -> account.password,
        "email" -> account.email,
        "website" -> account.website,
        "age" -> account.age,
        "gender" -> account.gender,
        "is_deleted" -> account.is_deleted,
        "updated_on" -> account.updated_on
      )
    }
  }
}

class AccountRepository @Inject()(dbapi: DBApi){
  var strTable = "account"
  var strIdCol = "id"
  //get database contection
  private val db = dbapi.database("default")

  val parser: RowParser[Account] = Macro.namedParser[Account]
  /* much shorter than play2.3 in play2.3 Generated as:
  get[String]("name") ~ get[Option[Int]]("year") map {
    case name ~ year => Info(name, year)
  }
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

}
