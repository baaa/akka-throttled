package com.mehmetyucel.akka.throttled

import akka.actor.Actor
import com.github.bucket4j.{Bandwidth, Bucket, Bucket4j}
import akka.dispatch.RequiresMessageQueue
import com.mehmetyucel.akka.throttled.messages.ThrottledMessage

import scala.language.postfixOps


abstract class ThrottledActor(tc: ThrottleConfig)
  extends Actor
    with RequiresMessageQueue[akka.dispatch.UnboundedMessageQueueSemantics] {

  private val bucket: Bucket = if (tc.limits.isEmpty) {
    throw new Exception("No limits created")
  } else {
    {
      tc.limits.foldLeft(Bucket4j.builder.withNanosecondPrecision()) { (o, i) =>
        if(tc.startEmpty) {
          o.addLimit(0, Bandwidth.simple(i._1, i._2))
        } else {
          o.addLimit(Bandwidth.simple(i._1, i._2))
        }
      }
    }.build()
  }

  override def aroundReceive(receive: Receive, msg: Any): Unit = {
    msg match {
      case ThrottledMessage(m) =>
        bucket.consumeSingleToken()
        super.aroundReceive(receive, m)
      case _ =>
        super.aroundReceive(receive, msg)
    }
  }

}

