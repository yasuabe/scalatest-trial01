name := "scalatest-trial01"

version := "0.1"

scalaVersion := "2.13.1"
scalacOptions ++= Seq(
  "-encoding", "utf8", // Option and arguments on same line
  "-Xfatal-warnings",  // New lines for each options
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-language:postfixOps",
  "-Ymacro-annotations"
)
libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.0.0-RC1",
  "org.typelevel" %% "cats-effect" % "2.0.0",
  "org.typelevel" %% "simulacrum" % "1.0.0",
  "org.scalatest" %% "scalatest" % "3.0.8" % Test,
  "org.scalacheck" %% "scalacheck" % "1.14.2" % Test,
  "io.chrisdavenport" %% "cats-scalacheck" % "0.2.0" % Test
)
