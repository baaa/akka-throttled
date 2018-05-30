import ReleaseTransformations._

resolvers ++= Seq(
  Resolver.jcenterRepo
)

pgpSecretRing := file("""C:\Users\kali\.sbt\gpg\secring.asc""")
pgpPublicRing := file("""C:\Users\kali\.sbt\gpg\pubring.asc""")
//pgpSecretRing := file("""C:\Users\kali\AppData\Local\lxss\home\kali\.gnupg\secring.gpg""")
//pgpPublicRing := file("""C:\Users\kali\AppData\Local\lxss\home\kali\.gnupg\pubring.gpg""")
usePgpKeyHex("c500a525a2efcb99")

name := "akka-throttled"
organization := "com.mehmetyucel"
version := "0.0.2"
scalaVersion := "2.12.2"
crossScalaVersions := Seq("2.11.11", "2.12.2")

lazy val akkaThrottled = project in file(".")

homepage := Some(url("https://github.com/baaa/akka-throttled"))
scmInfo := Some(
  ScmInfo(url(
    "https://github.com/baaa/akka-throttled"),
    "git@github.com:baaa/akka-throttled.git"))

developers := List(
  Developer(
    "mehmetyucel",
    "Mehmet Yucel",
    "mehmet@mehmetyucel.com",
    url("https://github.com/baaa")))

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
publishMavenStyle := true

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

releaseCrossBuild := true

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  // For non cross-build projects, use releaseStepCommand("publishSigned")
  releaseStepCommandAndRemaining("+publishSigned"),
  setNextVersion,
  commitNextVersion,
  releaseStepCommand("sonatypeReleaseAll"),
  pushChanges
)

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