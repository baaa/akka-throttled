addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.8")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.0")

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

