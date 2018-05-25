# akka-throttled

Vanilla akka actor for limiting the rate an actor processes messages.


### example 

```
class Foo extends ThrottledActor(
  ThrottleConfig().perSecond(1, 5) // rate limit to 1 message per 5 seconds
) {
  override def receive: Receive = {
    case Bar(m) => println(s"${LocalDateTime.now()} processing a bar with $m")
    case other => println(s"${LocalDateTime.now()} unknown message $other")
  }
}

object RunMe extends App {
  implicit lazy val system       = ActorSystem("demo-actor-system")
  implicit      val ex           = ExecutionContext.Implicits.global

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

case class Bar(m:String)
```

will produce:
```
actually sent everything
2018-05-25T12:56:50.696 unknown message 3
2018-05-25T12:56:50.698 processing a bar with 5
2018-05-25T12:56:50.698 processing a bar with 6
2018-05-25T12:56:50.698 processing a bar with 8
2018-05-25T12:56:50.698 processing a bar with 9
2018-05-25T12:56:50.698 processing a bar with 10
2018-05-25T12:56:50.699 unknown message 11
2018-05-25T12:56:50.699 processing a bar with 1
2018-05-25T12:56:55.701 processing a bar with 2
2018-05-25T12:57:00.704 processing a bar with 4
2018-05-25T12:57:05.709 processing a bar with 7
```

It is possible to create multiple limits. If any of the created quotas for these limits are reached, actor won't process any further messages until new quota becomes available.

For example
```
  ThrottleConfig()
    .perSecond(100)
    .perHour(1000)  
```

above config will create two limits `100 messages per second` and `1000 messages per hour` 
which means if you process 100 messages for 10 consecutive seconds, no messages will be processed for the next 59 minutes 50 seconds because the hourly quota is reached.

message not wrapped with `ThrottledMessage` are not throttled and do not effect quotas.