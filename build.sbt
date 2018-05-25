resolvers ++= Seq(
  Resolver.jcenterRepo
)

name := "akka-throttled"
organization := "com.mehmetyucel"
version := "0.0.1"
scalaVersion := "2.12.2"

val compileDependencies = Seq(
  "com.typesafe" % "config" % "1.3.1",
  "com.github" % "bucket4j" % "1.3.1",
  "com.typesafe.akka" %% "akka-actor" % "2.5.12"
).map(_.excludeAll(ExclusionRule("org.scalatest")))

val testDependencies = Seq(
  "com.typesafe.akka" %% "akka-testkit" % "2.5.12" % Test,
  "org.scalactic" %% "scalactic" % "3.0.0" % Test,
  "org.scalatest" %% "scalatest" % "3.0.0" % Test
)

libraryDependencies ++= compileDependencies ++ testDependencies

javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled")
scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

parallelExecution in Test := false

parallelExecution in IntegrationTest := false