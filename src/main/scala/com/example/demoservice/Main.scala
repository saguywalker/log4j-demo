package com.example.demoservice

import cats.effect.{Concurrent, IO, IOApp}
import com.comcast.ip4s.*
import fs2.io.net.Network
import io.circe.Decoder
import org.apache.logging.log4j.LogManager
import org.http4s.*
import org.http4s.Method.*
import org.http4s.circe.*
import org.http4s.dsl.io.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*


final case class RequestBody(query: String)
object RequestBody:
  given Decoder[RequestBody] = Decoder.derived[RequestBody]
  given [F[_]: Concurrent]: EntityDecoder[F, RequestBody] = jsonOf

object Main extends IOApp.Simple:
  System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "true")
  System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase", "true")

  private val logger = LogManager.getLogger(this.getClass)

  private val httpApp = HttpRoutes.of[IO] {
    case req @ POST -> Root / "reverse" =>
      for {
        request <- req.as[RequestBody]
        _ = logger.info(s"reversing: ${request.query}")
        resp <- Ok(request.query.reverse)
      } yield resp
  }.orNotFound

  val run: IO[Unit] = {
    for {
      _ <-
        EmberServerBuilder.default[IO]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(httpApp)
          .build
    } yield ()
  }.useForever



//import org.apache.logging.log4j.Level
//  logger.printf(Level.INFO, "Test: %s", input)
