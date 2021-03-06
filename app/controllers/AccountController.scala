/**
  *@author dosonphuong@gmail.com
  */
package controllers

import java.io.File
import java.nio.file.Paths
import javax.inject.Inject

import infra.DBService
import models.{Account, AccountRepository}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.data._
import play.api.data.Forms._
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._
import play.Environment




//using case class in order to map data from form to model
case class AccountData (
                         name: Option[String],
                         jp_name: Option[String],
                         username: String,
                         password: String,
                         email: String,
                         website: Option[String],
                         age: Option[Int],
                         gender: Int
                       )
case class AccountField(name: String, value: String)
/**
  * from play2.6 we need to inject model to controller (not only import) in order to use it
  * @param AccountService
  * @param cc
  */
class AccountController @Inject()(AccountService: AccountRepository, dBService: DBService,
                                  cc: ControllerComponents) extends AbstractController(cc) {

  val accountForm = Form(
    mapping(
      "name" -> optional(text),
      "jp_name" -> optional(text),
      "username" -> nonEmptyText,
      "password" -> nonEmptyText,
      "email" -> email,
      "website" -> optional(text),
      "age" -> optional(number),
      "gender" -> number
    )(AccountData.apply)(AccountData.unapply)
  )


  /**
    * show the list screen
    * @return
    */
  def index = Action {
    val content = views.html.account.list()
      Ok(views.html.main(content))
  }


  def genRecords(num: Int) = Action{
    Future{
      dBService.genRecords(num)
    }
    //Redirect(routes.AccountController.index()).flashing(("success", s"Job(s) will be running in background."))
    Ok("processing...")
  }


  def getXAccounts(offset: Int, limit: Int) = Action{
    val result: List[Account] = AccountService.getXRecords(offset,limit)
    Ok(Json.toJson(result))
  }

  /**
    * get data for the list screen
    * @return
    */
  def getAllAccounts = Action{
    val result: List[Account] = AccountService.findAll()
    Ok(Json.toJson(result))
  }


  def getInserted(id: Int) = Action{
    val result: List[Account] = AccountService.getInserted(id)
    Ok(Json.toJson(result))
  }

  /**
    * fire when user click submit button from the adding form
    * @return JSON
    */
  def add = Action{ implicit request =>
    val mapResult = scala.collection.mutable.Map[String, String]()
    accountForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        mapResult.put("success", "0")
        mapResult.put("msg", "bind form fail")
        Ok(Json.toJson(mapResult.toMap))
      },
      acountData => {
        /* binding success, you get the actual value. */
        val account = AccountData(
          acountData.name,
          acountData.jp_name,
          acountData.username,
          acountData.password,
          acountData.email,
          acountData.website,
          acountData.age,
          acountData.gender
        )
        val id = AccountService.insert(account)
        if (id > 0) {
          mapResult.put("success", "1")
          mapResult.put("id", id.toString)
          mapResult.put("msg", s"insert successfully! id: ${id}")
        } else {
          mapResult.put("success", "0")
          mapResult.put("msg", s"insert failed! id: ${id}")
        }
        Ok(Json.toJson(mapResult.toMap))
      }

    )
  }


  /**
    * check if value is exist
    * @return JSON
    */
  def isExist = Action{ implicit request =>
    val frmBinding = Form(mapping("name" -> text,"value" -> text)(AccountField.apply)(AccountField.unapply))
    val mapResult = scala.collection.mutable.Map[String, String]()
    frmBinding.bindFromRequest.fold(
      formWithErrors => {
        mapResult.put("success", "0")
        mapResult.put("msg", "bind form fail")
        Ok(Json.toJson(mapResult.toMap))
      },
      bindedValue => {
        val result = AccountService.isExist(bindedValue.name,bindedValue.value)
        if (result) {
          mapResult.put("success", "1")
          mapResult.put("msg", "the record is exist!")
        } else {
          mapResult.put("success", "0")
          mapResult.put("msg", "avaiable")
        }
        Ok(Json.toJson(mapResult.toMap))
      }

    )
  }

  def getNumRows = Action{ implicit request =>
    val numRows = AccountService.getNumRows()
    val mapResult = scala.collection.mutable.Map[String, String]()
    mapResult.put("numRows", numRows.toString)
    Ok(Json.toJson(mapResult.toMap))
  }

  def upload = Action(parse.temporaryFile) { request =>
    val uploadPath = "tmp/avatar/"
    //check path existed
    val ckFile: File = new File(uploadPath)
    if (!ckFile.exists()) {
      ckFile.mkdir()
    }

    //move
    //row.ref.moveTo(Play.application.getFile(s"/${path + newName}"))
    val newName = "haha.jpg"
    //request.body.moveTo(Environment.getFile(s"${uploadPath + newName}"))
    request.body.moveTo(Paths.get(uploadPath), replace = true)
    Ok("File uploaded")
  }
}