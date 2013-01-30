package bdd

import cucumber.api.scala.{ScalaDsl, EN}
import java.net.URL
import java.net.HttpURLConnection
import cucumber.api.DataTable
import collection.JavaConversions._

class RestSteps extends ScalaDsl with EN {

  val baseUrl = "http://localhost:8080"

  When( """^I POST (.+)$""") {
    (path: String, headersAndBody: String) =>
      val url = new URL(baseUrl + path)
      val conn = url.openConnection.asInstanceOf[HttpURLConnection]
      val headersAndBodyParts = headersAndBody.split("\n\n")
      headersAndBodyParts(0).split("\n").foreach { line =>
        val nameAndValue = line.split(":")
        conn.setRequestProperty(nameAndValue(0),nameAndValue(1))
      }
      conn.setRequestMethod("POST")
      conn.setDoOutput(true)
      val os = conn.getOutputStream
      os.write(headersAndBodyParts(1).getBytes)
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