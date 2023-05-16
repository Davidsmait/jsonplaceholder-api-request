//#full-example
package com.example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt


object AkkaQuickstart {
  implicit val system: ActorSystem = ActorSystem() //Akka actors
  implicit val materializer: ActorMaterializer = ActorMaterializer() // Akka stream
  import system.dispatcher //"thread pool"

  val source: String =
    """
      |{
      |  "id": 101,
      |  "title": 'foo',
      |  "body": 'bar',
      |  "userId": 1
      |}
      |""".stripMargin


  val request = HttpRequest(
    method = HttpMethods.POST,
    uri = "https://jsonplaceholder.typicode.com/posts",
    entity = HttpEntity(ContentTypes.`application/x-www-form-urlencoded`, source)
  )



  def sendRequest(): Future[String] ={
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)
    val entityFuture: Future[HttpEntity.Strict] = responseFuture.flatMap(response => response.entity.toStrict(2.seconds))
    entityFuture.map(entity => entity.data.utf8String)
  }

  def main(args: Array[String]): Unit = {
    sendRequest().foreach(println)

  }

}
