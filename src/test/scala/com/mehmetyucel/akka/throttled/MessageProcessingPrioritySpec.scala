package com.mehmetyucel.akka.throttled

import akka.actor.Props
import com.mehmetyucel.akka.throttled.messages.ThrottledMessage
import scala.concurrent.duration._
import scala.language.postfixOps


class MessageProcessingPrioritySpec extends BaseActorSpec {

  "throttled actor" should {
    val act = system.actorOf(Props(
      new ThrottledActor(
        ThrottleConfig()
          .perSecond(1, 1) // allow 1 message every second
          .perSecond(3, 10) // allow 3 messages every 10 seconds
      ) {
        def receive = {
          case s: String =>
            sender ! s
          case _ => println("something else happened")
        }
      }
    ).withDispatcher("throttled-dispatcher"))
    act ! ThrottledMessage("1")
    act ! "2"
    act ! "3"
    act ! ThrottledMessage("4")
    act ! "5"
    act ! ThrottledMessage("6")
    act ! "7"
    act ! ThrottledMessage("8")

    "prioritise non throttled messages" in {
      within(1 seconds) {
        receiveN(4) should contain allOf("2", "3", "5", "7")
      }
    }

    "have delays releasing throttled messages" in {
      expectMsg("1")
      expectNoMessage(800 millis)
      expectMsg("4")
      expectNoMessage(800 millis)
      expectMsg("6")
      expectNoMessage(3000 millis)
      expectMsg("8")
    }
  }

}



