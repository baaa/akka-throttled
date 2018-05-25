package com.mehmetyucel.akka.throttled

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, Matchers, WordSpecLike}

abstract class BaseActorSpec extends TestKit(ActorSystem("th"))
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll
  with BeforeAndAfter
{

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

}