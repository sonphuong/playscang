package controllers

import javax.inject.Inject

import play.api._
import play.api.mvc._
import views.html

class Home @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action {
    val content = views.html.home.list()
      Ok(views.html.main(content))
  }


}
            
