package com.mehmetyucel.akka.throttled

import java.time.{Duration => JDuration}

case class ThrottleConfig(limits: Seq[(Int, JDuration)] = Seq.empty, startEmpty: Boolean = false) {

  def startWithZeroQuota(): ThrottleConfig = {
    ThrottleConfig(limits, true)
  }

  def perSecond(limit:Int, perNumberOfSeconds:Int = 1): ThrottleConfig = {
    ThrottleConfig(
      limits :+ (limit, JDuration.ofSeconds(perNumberOfSeconds)), startEmpty
    )
  }

  def perMinute(limit:Int, perNumberOfMinutes:Int = 1): ThrottleConfig = {
    ThrottleConfig(
      limits :+ (limit, JDuration.ofMinutes(perNumberOfMinutes)), startEmpty
    )
  }

  def perHour(limit:Int, perNumberOfHours:Int = 1): ThrottleConfig = {
    ThrottleConfig(
      limits :+ (limit, JDuration.ofHours(perNumberOfHours)), startEmpty
    )
  }

  def perDay(limit:Int, perNumberOfDays:Int = 1): ThrottleConfig = {
    ThrottleConfig(
      limits :+ (limit, JDuration.ofDays(perNumberOfDays)), startEmpty
    )
  }

}


