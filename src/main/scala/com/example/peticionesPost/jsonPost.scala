package com.example.peticionesPost

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers._
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object Main extends App {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val url = "https://jsonplaceholder.typicode.com/posts"
  val postData = """
                   |{
                   |  "title": "foo",
                   |  "body": "bar",
                   |  "userId": 1
                   |}
    """.stripMargin

  val headers = List(
    RawHeader("Content-type", "application/json; charset=UTF-8")
  )

  val request = HttpRequest(
    method = HttpMethods.POST,
    uri = url,
    entity = HttpEntity(ContentTypes.`application/json`, postData),
    headers = headers
  )

  val responseFuture: Future[HttpResponse] = Http().singleRequest(request)
  responseFuture.onComplete{
      case Success(res) =>
        println(s"respuesta ${res}")
        val responseBodyFuture = res.entity.toStrict(5.seconds).map(_.data.utf8String)
        responseBodyFuture.onComplete {
          case scala.util.Success(body) =>
            println(s"Response body: $body")
          case scala.util.Failure(ex) =>
            println(s"Failed to parse response body: ${ex.getMessage}")
        }
      case Failure(ex) =>
        println(s"Request failed: ${ex.getMessage}")
  }
}
