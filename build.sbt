name := "sbtchallenge"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.10.4"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.2" % "test"

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.11.5" % "test"

libraryDependencies += "junit" % "junit" % "4.10" % "test"

EclipseKeys.withSource := true

EclipseKeys.withBundledScalaContainers := false
