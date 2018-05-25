package com.mehmetyucel.akka.throttled

import java.time.LocalDateTime

import akka.actor.{ActorSystem, Props}
import com.mehmetyucel.akka.throttled.messages.ThrottledMessage

import scala.concurrent.ExecutionContext


class Foo extends ThrottledActor(
  ThrottleConfig().perSecond(1, 5) // rate limit to 1 message per 5 seconds
) {
  override def receive: Receive = {
    case Bar(m) => println(s"${LocalDateTime.now()} processing a bar with $m")
    case other => println(s"${LocalDateTime.now()} unknown message $other")
  }
}

object RunMe extends App {
  implicit lazy val system = ActorSystem("demo-actor-system")
  implicit      val ex     = ExecutionContext.Implicits.global

  ThrottleConfig()
    .perSecond(100)
    .perHour(1000)

  val thr = system.actorOf(Props[Foo].withDispatcher("throttled-dispatcher"))

  thr ! ThrottledMessage(Bar("1"))
  thr ! ThrottledMessage(Bar("2"))
  thr ! "3"
  thr ! ThrottledMessage(Bar("4"))
  thr ! Bar("5")
  thr ! Bar("6")
  thr ! ThrottledMessage(Bar("7"))
  thr ! Bar("8")
  thr ! Bar("9")
  thr ! Bar("10")
  thr ! "11"

  println("actually sent everything")
}

case class Bar(m: String)
