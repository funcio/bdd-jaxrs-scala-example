package bdd

import cucumber.api.scala.{ScalaDsl, EN}
import java.net.URL
import java.net.HttpURLConnection

class RestSteps extends ScalaDsl with EN {

  val baseUrl = "http://localhost:8080"

  When( """^I POST (.+)$""") {
    (path: String, body: String) =>
      val url = new URL(baseUrl + path)
      val conn = url.openConnection.asInstanceOf[HttpURLConnection]
      conn.setDoOutput(true)
      val os = conn.getOutputStream
      os.write(body.getBytes)
      os.flush()
      os.close()
      val result = scala.io.Source.fromInputStream(conn.getInputStream).mkString

  }

  Then( """^the response status is (\d+)$""") {
    (status: Int) =>
  }

  Then( """^the response header (.+) matches (.*)$""") {
    (headerName: String, headerPattern: String) =>
  }

  Then( """^when I GET /tasks/@id$""") {
    () =>
  }

  Then( """^response\["([^"]*)"\] is (.+)$""") {
    (field: String, value: String) =>
  }

}