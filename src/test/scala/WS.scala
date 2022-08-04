import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.socket.client.{Ack, IO, Socket}
import io.socket.emitter.Emitter
import org.json.JSONObject

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
class WS extends Simulation {
  val port = Integer.getInteger("port", 8888)
  val host = System.getProperty("host", "localhost")
  val token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IkQ4cVQzRHA5dlNvX0w5d2l4YmF1QlFkNHFLbk5neFhqWHJHakhlTGxVaDAifQ.eyJ1c2VyIjp7InJlZnJlc2hUb2tlbiI6IjZhNmU3NTc2ZGY3YmZiOTBlMjVjOTdhODY4OTU2MTEyYTdkOGJjYjExOWQ3NDBkNzcyNWVmMDcxYTIxNDIyMTgyMzk0NzVjNDFjODNiYWRjIiwidXNlcklkIjoiNjJlMGRmMGExMmI0NDAwMGRhOTBkNmEzIiwicHVibGljVXNlcklkIjoic29meWEiLCJuZXR3b3JrSWQiOiI1YjAyOGU0YTY3M2Y4MTAwMGZiMDQwZTcifSwic3ViIjoiNjJlMGRmMGExMmI0NDAwMGRhOTBkNmEzIiwiaXNzIjoiaHR0cHM6Ly9hcGkuc3RhZ2luZy5hbWl0eS5jbyIsImlhdCI6MTY1OTA3NTg0OSwiZXhwIjoxNjkwNjMzNDQ5fQ.Ycva0yBS6Z4G82EuYbSWrtinAK8rsDnfJVTS88OCWfuY6n2nAdf9R1osj1_lfCzZkeRZ-L4Ytk0XNb5_vf-9-M6SkNrMDvOGnq19G-G7DSHJ5veyy7xL-7tNoQjMY50rYaFdqZhzm76j7WNx03VZVidB3trFgw5t1trGONbHBH0uSwasdwMHabMsCkFFCDtQUruDSdzuILcvTYo9UBEfniWxO153MxwRF_gQI9d9JRFqWRTzQ_nHkhD0CcF-I5hiZqNcU65Q7m1_khrpnN0J3l1GZ05Z3UnOMLH89MedBUlYWvzadrEVuMDKsSMpTfFmwxoj2NXt2Ho1gi-ZgMOgCg"
  val wsUrl = "wss://wss://api.staging.amity.co/socket.io/?EIO=3&transport=websocket&token=" + token
  val socketUrl = "https://api.staging.amity.co"
  val optConnect = new IO.Options()

  val httpProtocol: HttpProtocolBuilder = http
    .proxy(
      Proxy("127.0.0.1", 8888)
        .httpsPort(8888)
    ).acceptHeader("application/json;q=0.9,*/*;q=0.8")
    .baseUrl("https://api.staging.amity.co").wsBaseUrl("wss://api.staging.amity.co")
    .contentTypeHeader("application/json")

  //root is HTTP protocol and webSocket url is being appended to it

  val scene = scenario("testWebSocket")
    .exec(ws("openSocket").connect("/socket.io/?EIO=3&transport=websocket&Content-Type=application/json&token="+token).subprotocol("JSON")
    )

    .pause(2  seconds)
    .exec(session => {
      val obj = new JSONObject()
      val aknowledge = null
      optConnect.forceNew = false
      optConnect.timeout = 10000
      val socket = IO.socket( socketUrl, optConnect )
      val printListener: Emitter.Listener = new Emitter.Listener {
        override def call(args: AnyRef*): Unit = {
        }
      }
      socket.on(Socket.EVENT_CONNECT, printListener)
      socket.connect()
    socket.emit("channel.getInactiveChannelIds","{}",new Ack {
      override def call(args: AnyRef*): Unit = {

        print( args )
      })
      println("---")
//println(aknowledge)
      session
    }
    ).exec(ws("closeSocket").close)


  setUp(scene.inject(atOnceUsers(1)).protocols(httpProtocol))
}