package controller

import play.api.mvc._
import controllers.AccountController
import org.scalatest.concurrent.ScalaFutures
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.CSRFTokenHelper._

class AccountControllerSpec extends PlaySpec with Results with GuiceOneAppPerSuite with ScalaFutures {

  def accountController = app.injector.instanceOf(classOf[AccountController])

  "AccountController" should {

    "show the list screen" in {
      val result = accountController.index(FakeRequest())
      status(result) must equal(OK)
      contentAsString(result) must include("Play Scala Angularjs Heroku")
    }

    "return a record in a json" in {
      val result = accountController.getInserted(466)(FakeRequest () )
      status(result) must equal(OK)
      contentAsString(result) must include("ramseycummings")
    }

  }



}