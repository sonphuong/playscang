/**
  * @author phuong_ds
  */
package models

import anorm._
import anorm.{Macro, RowParser, SqlParser}
import play.api.db.DBApi
import play.api.libs.json.{JsValue, Json, Writes}
import javax.inject.Inject
import play.api.Logger
import controllers.AccountData
import infra.SQLDebugger

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
    * get all data
    * @return
    */
  def findAll() = {
    db.withConnection{ implicit connection =>
      SQL(s"SELECT * FROM $strTable ORDER BY updated_on DESC LIMIT 1000").as(parser.*)
    }

  }

  /**
    * get all data
    * @return
    */
  def getXRecords(offset: Int, limit: Int) = {
    db.withConnection{ implicit connection =>
      SQL(s"SELECT * FROM $strTable ORDER BY updated_on DESC OFFSET $offset LIMIT $limit").as(parser.*)
    }
  }

  def getInserted(id: Int) = {
    db.withConnection{ implicit connection =>
      SQL(s"SELECT * FROM $strTable WHERE id = $id").as(parser.*)
    }

  }

  /**
    * insert to database
    * @param account
    * @return Boolean
    */
  def insert(account:AccountData):Int = {
    var id: Int = 0
    db.withConnection{ implicit connection =>
      val sql =
        s"""INSERT INTO $strTable (name,jp_name,username,password,email,website,age,gender,updated_on)
            VALUES ({name},{jp_name},{username},{password},{email},{website},{age},{gender},NOW()::timestamp)""".stripMargin
      try {
        val nid: Option[Long] = SQL(sql).on(
          "name"->account.name.getOrElse(""),
          "jp_name"->account.jp_name.getOrElse(""),
          "username"->account.username,
          "password"->account.password,
          "email"->account.email,
          "website"->account.website.getOrElse(""),
          "age"->account.age.getOrElse(0),
          "gender"->account.gender
        ).executeInsert()
        id = nid.getOrElse(0).toString.toInt
      } catch {
        case t: Throwable => {
          Logger.error("Exception insert DB:", t)
          val sql =
            s"""INSERT INTO $strTable (name,jp_name,username,password,email,website,age,gender,updated_on)
                VALUES ('${account.name.getOrElse("")}','${account.jp_name.getOrElse("")}','${account.username}','${account.password}','${account.email}','${account.website.getOrElse("")}','${account.age.getOrElse(0)}','${account.gender}',NOW()::timestamp)"""
          SQLDebugger.log(sql)
        }
      }
      id
    }

  }



  /**
    * check if a value of a field is exist
    * @param name
    * @param value
    * @return Boolean
    */
  def isExist(name: String, value: String): Boolean = {
    try {
      db.withConnection{ implicit connection =>
        var intNumRow = 0
        // add "COLLATE utf8_bin" to sql string for japaneses char search
        val sqlCheckExist = s"SELECT COUNT(1) AS num_row FROM $strTable WHERE $name = {value} COLLATE pg_catalog.default"
        intNumRow = SQL(sqlCheckExist).on("value" -> value).as(SqlParser.int("num_row").single)
        if(intNumRow>0) true
        else false
      }
    } catch {
      case ex: Exception =>
        Logger.error(s"${ex.getMessage}")
        true
    }
  }


  def getNumRows(): Int = {
    var numRow = 0
    try{
      db.withConnection{ implicit connection =>
        val sqlCheckExist = s"SELECT COUNT(1) AS num_row FROM $strTable"
        numRow = SQL(sqlCheckExist).as(SqlParser.int("num_row").single)
      }
    }
    catch{
      case ex: Exception => Logger.error(s"${ex.getMessage}")

    }
    numRow
  }
}
