package controllers

import javax.inject.Inject

import models.{Account, AccountRepository}
import play.api.libs.json.Json
import play.api.mvc._
//for using form
import play.api.data._
import play.api.data.Forms._



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
/**
  * from play2.6 we need to inject model to controller (not only import) in order to use it
  * @param AccountService
  * @param cc
  */
class AccountController @Inject()(AccountService: AccountRepository,
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

  /**
    * get data for the list screen
    * @return
    */
  def getData = Action{
    val result: List[Account] = AccountService.findAll()
    Ok(Json.toJson(result))
  }

  def add = Action{ implicit request =>

    accountForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        Ok(Json.toJson(Map("success" -> 0)))
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
        val result = AccountService.insert(account)
        val mapResult = scala.collection.mutable.Map[String, String]()
        if (result.toString == "success") {
          mapResult.put("success", "1")
        } else {
          mapResult.put("success", "0")
          mapResult.put("msg", result.toString)
        }
        Ok(Json.toJson(mapResult.toMap))
      }
    )
  }

}