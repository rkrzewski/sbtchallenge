package sbt

import org.scalacheck.Gen._
import org.scalacheck.Prop._
import org.scalatest.prop.Checkers

class ErrorSpec extends AbstractSpec with Checkers {

  "Errors " should {
    "Show contains line number" in {

      check(forAll(alphaStr) {
        (errorText) =>
          errorText.nonEmpty ==> {
            val buildSbt = s"""import sbt._
                       |import aaa._
                       |
                       |import scala._
                       |
                       |scalaVersion in Global := "2.11.2"
                       |
                       |ala
                       |
                       |libraryDependencies in Global ++= Seq(
                       |  $errorText
                       |  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
                       |  "ch.qos.logback" % "logback-classic" % "1.1.2"
                       |)""".stripMargin

            implicit val splitter = new EvaluateConfigurationsScalania
            val exception = intercept[MessageOnlyException] {
              split(buildSbt)
            }
            exception.getMessage.matches(".*(\\d+).*")
          }
      })
    }
  }


}
