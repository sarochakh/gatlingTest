import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration.DurationInt

class DefaultTest extends Simulation {

  val API_KEY = "b0edea5e3c8ef5611d66841f505d1eded80e88e4bc643d2e"

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("https://api.staging.amity.co")//here hac also be added headers if they are the same for all requests

  //creating scenario object
  val scene = scenario("DefaultTest")
    .during(2 minutes){
      exec(GetToken.start) //executing GetToken for 2 minutes without any pauses
    }

  //here we have users behaviour
  setUp(scene.inject(atOnceUsers(1))//it means 1 user starts doing test case without rump up
    .protocols(httpProtocol))//adding base url and other options, if needed

  //this is our test case/script with it's logic
  object GetToken{
    val start = exec(
      exec(http("GetToken")//here we have simple http post request
        .post("/v1/device")//as we have defined base url, here we add only short api path
        .headers(Map(
          "content-type" -> "application/json",
          "x-api-key " -> API_KEY
        ))
        .body(StringBody("{\n  \"userId\": \"string\",\n  \"deviceId\": \"string\",\n  \"deviceInfo\": {\n    \"kind\": \"ios\",\n    \"model\": \"string\",\n    \"sdkVersion\": \"string\"\n  },\n  \"displayName\": \"string\",\n  \"authToken\": \"string\"}"))
        //checking status of response is 200 and saving whole response body to variable to use it somewhere else in test
        .check(status.is(200), bodyString.saveAs("token")))
    )
  }
}





