package bdd

import cucumber.api.scala.{ScalaDsl, EN}
import java.net.URL
import java.net.HttpURLConnection
import org.scalatest.matchers.ShouldMatchers
import collection.JavaConversions._
import util.matching.Regex

case class Response(status:Int, headers:Map[String,Seq[String]],body:String)

class RestSteps extends ScalaDsl with EN with ShouldMatchers{

  val baseUrl = "http://localhost:8080"

  var lastResponse:Response = Response(200,Map(),"")

  When( """^I (GET|POST|PUT|DELETE) ([^\s]+) with$""") {
    (verb:String, path: String, headersAndBody: String) =>
      sendRequest(verb, replaceVars(path), headersAndBody)
  }

  When( """^I (GET|POST|PUT|DELETE) ([^\s]+)$""") {
    (verb:String, path: String) =>
      sendRequest(verb, replaceVars(path))
  }


  val httpHeaderRegexp:Regex = """^([A-Za-z\-]+):(.+)$""".r

  private def sendRequest(verb: String, path: String, headersAndBody: String = "\n\n") {
    val url = new URL(baseUrl + path)
    val conn = url.openConnection.asInstanceOf[HttpURLConnection]
    val headersAndBodyParts = headersAndBody.split("\n")
    val headers = headersAndBodyParts.takeWhile(httpHeaderRegexp.pattern.matcher(_).matches())
      .map{case httpHeaderRegexp(headerName,headerValue) => headerName->headerValue}.toMap
    val requestBody = headersAndBodyParts.dropWhile(httpHeaderRegexp.pattern.matcher(_).matches()).mkString("\n")
    headers.foreach(h => conn.setRequestProperty(h._1, h._2))
    conn.setRequestMethod(verb)
    conn.setDoOutput(true)
    val os = conn.getOutputStream
    os.write(requestBody.getBytes)
    os.flush()
    os.close()
    val responseBody = scala.io.Source.fromInputStream(conn.getInputStream).mkString
    this.lastResponse = Response(conn.getResponseCode, conn.getHeaderFields.mapValues(_.toSeq).toMap, responseBody)
  }

  private def replaceVars(s:String) = {
    val link = lastResponse.headers.getOrElse("Link",Seq()).headOption.getOrElse("???")
    s.replaceAll("@Link",link)
  }

  Then( """^the response status is (\d+)$""") {
    (status: Int) =>
      assert(lastResponse.status === status)
  }

  Then( """^the response header (.+) matches (.*)$""") {
    (headerName: String, headerPattern: String) =>
  }

  Then( """^response\["([^"]*)"\] is (.+)$""") {
    (field: String, value: String) =>
  }

}