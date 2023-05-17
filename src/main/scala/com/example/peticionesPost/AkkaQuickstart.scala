//#full-example
package com.example.peticionesPost

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
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
      |  "id": 2343,
      |  "title": "foeeeo",
      |  "body": "beeear",
      |  "userId": 1000
      |}
      |""".stripMargin


  val request: HttpRequest = HttpRequest(
    method = HttpMethods.POST,
    uri = "https://jsonplaceholder.typicode.com/posts/",
    entity = HttpEntity(ContentTypes.`application/json`, source)
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
