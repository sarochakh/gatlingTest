


import io.gatling.core.Predef._
import io.gatling.http.Predef._

class MegaBot extends Simulation {

  val httpProtocol = http
    .baseUrl("http://")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")

  val scn = scenario("BasicSimulation")
    .exec(
      Conversation.start
    )


  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
}
object Conversation{
  val start = exec(
    http("request_1")
      .get("/")
  ).tryMax(100) {
    pause(1)
      .exec(http("GET /api/registration/{registrationID}")
        .get("/api/registration/${registrationID(0)}")
        .check(
          jsonPath("$..purchaseId").findAll.saveAs("purchaseID")
        )
      )
  }
}