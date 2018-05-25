package com.mehmetyucel.akka.throttled

import akka.actor.{ActorSystem, PoisonPill}
import akka.dispatch.{PriorityGenerator, UnboundedStablePriorityMailbox}
import com.mehmetyucel.akka.throttled.messages.ThrottledMessage
import com.typesafe.config.Config


class ThrottledMailbox(settings: ActorSystem.Settings, config: Config)
  extends UnboundedStablePriorityMailbox(
    PriorityGenerator {
      case a:ThrottledMessage[_] => 2
      case PoisonPill => 3
      case other => 1
    })

