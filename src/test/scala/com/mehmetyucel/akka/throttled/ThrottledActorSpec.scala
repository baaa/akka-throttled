package com.mehmetyucel.akka.throttled

import akka.actor.{Actor, Kill, Props}
import akka.testkit.{TestKit, TestProbe}
import com.mehmetyucel.akka.throttled.messages.ThrottledMessage
import org.scalactic.source.Position

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.control.Breaks._

class ThrottledActorSpec extends BaseActorSpec {
  val tp1 = TestProbe()

  "when started with 0 quota" should {
    "limit the number of messages replied" in {
      val act2 = system.actorOf(Props(
        new ThrottledActor(
          ThrottleConfig()
            .startWithZeroQuota()
            .perSecond(3, 5) // allow 3 messages every 5 seconds
        ) {
          def receive = {
            case s: String =>
              sender ! s
            case _ =>
              sender ! ""
          }
        }
      ).withDispatcher("throttled-dispatcher"))

      // send 200 messages 100 throttled 100 normal
      var sent = 0
      breakable {
        while (true) {
          act2 ! tp1.send(act2, ThrottledMessage("some throttled message"))
          act2 ! tp1.send(act2, "some normal message")
          sent = sent + 1
          if (sent >= 100) {
            break
          }
        }
      }

      var throttled = 0
      var normal = 0

      tp1.receiveWhile(20 seconds, 20 seconds, 200) {
        case "some normal message" => normal = normal + 1
        case "some throttled message" => throttled = throttled + 1
      }

      withClue("should get a reply for all non-throttled messages, got:") {
        normal shouldBe 100
      }

      withClue("should get 'almost' 12 throttled replies, since we started with zero initial quota") {
        throttled shouldBe 11
      }

    }
  }


}

