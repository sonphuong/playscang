package controllers

import javax.inject.Inject
import models.{Account, AccountRepository}
import play.api.libs.json.Json
import play.api.mvc._
import scala.concurrent.ExecutionContext


class AccountController @Inject()(AccountService: AccountRepository,
                                  cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  val model = AccountService

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
    val result: List[Account] = model.findAll()
    Ok(Json.toJson(result))
  }

}
            
