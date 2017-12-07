
import controllers.AccountController
import org.scalatest.concurrent.ScalaFutures
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.CSRFTokenHelper._

class FunctionalSpec extends PlaySpec with GuiceOneAppPerSuite with ScalaFutures {/*

  def dateIs(date: java.util.Date, str: String) = {
    new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) == str
  }

  def accountController = app.injector.instanceOf(classOf[AccountController])

  "AccountController" should {

    "show the list screen" in {
      val result = accountController.index(FakeRequest())
      status(result) must equal(SEE_OTHER)
      contentAsString (result) must include ("ramseycummings")
    }

    //running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

    "create new account" in {
      val badResult = accountController.add(FakeRequest().withCSRFToken)

      status(badResult) must equal(BAD_REQUEST)

      val badDateFormat = accountController.add(
        FakeRequest().withFormUrlEncodedBody(
          "age" -> "25",
          "email" -> "demo1512618016176@yahoo.com",
          "gender" -> "1",
          "jp_name" -> "デモdemo1512618016176",
          "name" -> "demo1512618016176",
          "password" -> "demo",
          "username" -> "demo1512618016176",
          "website" -> "http://wwww.demo1512618016176.com"
          ).withCSRFToken
        )

        status (badDateFormat) must equal (BAD_REQUEST)
        contentAsString (badDateFormat) must include ("""insert fail!""")

        val result = accountController.add (
          FakeRequest().withFormUrlEncodedBody(
            "age" -> "25",
            "email" -> "demo1512618016176@yahoo.com",
            "gender" -> "1",
            "jp_name" -> "デモdemo1512618016176",
            "name" -> "demo1512618016176",
            "password" -> "demo",
            "username" -> "demo1512618016176",
            "website" -> "http://wwww.demo1512618016176.com"
          ).withCSRFToken
        )

        status (result) must equal (SEE_OTHER)
        contentAsString (result) must include ("""insert successfully!""")

        val list = accountController.getInserted (466) (FakeRequest () )

        status (list) must equal (OK)
        contentAsString (list) must include ("ramseycummings")
        }
  }
*/}
