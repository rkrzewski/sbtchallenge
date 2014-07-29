name := "sbtchallenge"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.10.4"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test"

libraryDependencies += "junit" % "junit" % "4.10" % "test"

EclipseKeys.withSource := true

EclipseKeys.withBundledScalaContainers := false
