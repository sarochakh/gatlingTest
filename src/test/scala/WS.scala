import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.socket.client.{Ack, IO, Socket}
import io.socket.emitter.Emitter
import org.json.JSONObject
import scala.language.postfixOps
import org.json.JSONArray

class WS extends Simulation {

  val API_KEY = "b0edea5e3c8ef5611d66841f505d1eded80e88e4bc643d2e"
  val token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IkQ4cVQzRHA5dlNvX0w5d2l4YmF1QlFkNHFLbk5neFhqWHJHakhlTGxVaDAifQ.eyJ1c2VyIjp7InJlZnJlc2hUb2tlbiI6IjZhNmU3NTc2ZGY3YmZiOTBlMjVjOTdhODY4OTU2MTEyYTdkOGJjYjExOWQ3NDBkNzcyNWVmMDcxYTIxNDIyMTgyMzk0NzVjNDFjODNiYWRjIiwidXNlcklkIjoiNjJlMGRmMGExMmI0NDAwMGRhOTBkNmEzIiwicHVibGljVXNlcklkIjoic29meWEiLCJuZXR3b3JrSWQiOiI1YjAyOGU0YTY3M2Y4MTAwMGZiMDQwZTcifSwic3ViIjoiNjJlMGRmMGExMmI0NDAwMGRhOTBkNmEzIiwiaXNzIjoiaHR0cHM6Ly9hcGkuc3RhZ2luZy5hbWl0eS5jbyIsImlhdCI6MTY1OTA3NTg0OSwiZXhwIjoxNjkwNjMzNDQ5fQ.Ycva0yBS6Z4G82EuYbSWrtinAK8rsDnfJVTS88OCWfuY6n2nAdf9R1osj1_lfCzZkeRZ-L4Ytk0XNb5_vf-9-M6SkNrMDvOGnq19G-G7DSHJ5veyy7xL-7tNoQjMY50rYaFdqZhzm76j7WNx03VZVidB3trFgw5t1trGONbHBH0uSwasdwMHabMsCkFFCDtQUruDSdzuILcvTYo9UBEfniWxO153MxwRF_gQI9d9JRFqWRTzQ_nHkhD0CcF-I5hiZqNcU65Q7m1_khrpnN0J3l1GZ05Z3UnOMLH89MedBUlYWvzadrEVuMDKsSMpTfFmwxoj2NXt2Ho1gi-ZgMOgCg"
  val socketUrl = "https://api.staging.amity.co"
  val optConnect = new IO.Options()

  val httpProtocol: HttpProtocolBuilder = http
    /*.proxy(
     Proxy("127.0.0.1", 8888)
       .httpsPort(8888)
   )*/
    .baseUrl("https://api.staging.amity.co").wsBaseUrl("wss://api.staging.amity.co")

  val scene = scenario("testWebSocket")
    .exec(EmitEvents.start, GetToken.start)

  setUp(scene.inject(atOnceUsers(1)).protocols(httpProtocol))

 object OpenSocket{ val start = exec(ws("openSocket").connect("/socket.io/?EIO=3&transport=websocket&token="+token))}
object CloseSocket {
  val start = exec(ws("closeSocket").close)
  .exec(session =>{
  println("=======")
  session
  }
)}

  object EmitEvents{val start =

    exec(
    session => {
    val obj = new JSONObject()
    obj.put("since", "2021-08-14T12:15:42.759Z")
    optConnect.transports = Array[String]("websocket")
    optConnect.query = "token=eyJhbGciOiJSUzI1NiIsImtpZCI6IkQ4cVQzRHA5dlNvX0w5d2l4YmF1QlFkNHFLbk5neFhqWHJHakhlTGxVaDAifQ.eyJ1c2VyIjp7InJlZnJlc2hUb2tlbiI6IjZhNmU3NTc2ZGY3YmZiOTBlMjVjOTdhODY4OTU2MTEyYTdkOGJjYjExOWQ3NDBkNzcyNWVmMDcxYTIxNDIyMTgyMzk0NzVjNDFjODNiYWRjIiwidXNlcklkIjoiNjJlMGRmMGExMmI0NDAwMGRhOTBkNmEzIiwicHVibGljVXNlcklkIjoic29meWEiLCJuZXR3b3JrSWQiOiI1YjAyOGU0YTY3M2Y4MTAwMGZiMDQwZTcifSwic3ViIjoiNjJlMGRmMGExMmI0NDAwMGRhOTBkNmEzIiwiaXNzIjoiaHR0cHM6Ly9hcGkuc3RhZ2luZy5hbWl0eS5jbyIsImlhdCI6MTY1OTA3NTg0OSwiZXhwIjoxNjkwNjMzNDQ5fQ.Ycva0yBS6Z4G82EuYbSWrtinAK8rsDnfJVTS88OCWfuY6n2nAdf9R1osj1_lfCzZkeRZ-L4Ytk0XNb5_vf-9-M6SkNrMDvOGnq19G-G7DSHJ5veyy7xL-7tNoQjMY50rYaFdqZhzm76j7WNx03VZVidB3trFgw5t1trGONbHBH0uSwasdwMHabMsCkFFCDtQUruDSdzuILcvTYo9UBEfniWxO153MxwRF_gQI9d9JRFqWRTzQ_nHkhD0CcF-I5hiZqNcU65Q7m1_khrpnN0J3l1GZ05Z3UnOMLH89MedBUlYWvzadrEVuMDKsSMpTfFmwxoj2NXt2Ho1gi-ZgMOgCg"
    val socket = IO.socket(socketUrl, optConnect)
    val printListener: Emitter.Listener = new Emitter.Listener {
      override def call(args: AnyRef*): Unit = {
        println("CONNECTED" + args)
      }
    }
    val printListenerError: Emitter.Listener = new Emitter.Listener {
      override def call(args: AnyRef*): Unit = {
        println("ERROR" + args)
      }
    }
    val printListenerMessage: Emitter.Listener = new Emitter.Listener {
      override def call(args: AnyRef*): Unit = {
        println("Message" + args)
      }
    }
    socket.on(Socket.EVENT_CONNECT, printListener)
    socket.on(Socket.EVENT_ERROR, printListenerError)
    socket.on(Socket.EVENT_MESSAGE, printListenerMessage)
    socket.connect()
    //Thread.sleep(8000)
    socket.emit("channel.getInactiveChannelIds", obj, new Ack {
      override def call(args: AnyRef*): Unit = {

        println("CHANNELS" + args)
      }
    })
    println("------------------")
    val array = new JSONArray
    array.put(" CleoC2")
    val obj1 = new JSONObject()
    obj1.put("userIds", array)
    socket.emit("v3/user.getList", obj1, new Ack {
      override def call(args: AnyRef*): Unit = {

        println("========UserID" + args)
      }
    })

      println(session.attributes)
      Thread.sleep(8000)
    session
  })}
  object GetToken{
    val start = exec(
      exec(http("GetToken")
        .post("/v1/device")
        .headers(Map(
          "content-type" -> "application/json",
          "x-api-key " -> API_KEY
        ))
        .body(StringBody("{\n  \"userId\": \"string\",\n  \"deviceId\": \"string\",\n  \"deviceInfo\": {\n    \"kind\": \"ios\",\n    \"model\": \"string\",\n    \"sdkVersion\": \"string\"\n  },\n  \"displayName\": \"string\",\n  \"authToken\": \"string\"}"))
        .check(status.is(200), bodyString.saveAs("token")))
    )
  }
}





