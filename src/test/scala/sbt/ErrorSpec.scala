package sbt

class ErrorSpec extends AbstractSpec {

  def split(s: String)(implicit splitter: SplitExpressions) = splitter.splitExpressions(s.split("\n").toSeq)

  "Errors " should {
    "Show error line number " in {
      val orgSplitter = new EvaluateConfigurationsOriginal
      val buildSbt = """import sbt._
                       |import aaa._
                       |
                       |import scala._
                       |
                       |scalaVersion in Global := "2.11.2"
                       |
                       |ala
                       |
                       |libraryDependencies in Global ++= Seq(
                       |ss
                       |  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
                       |  "ch.qos.logback" % "logback-classic" % "1.1.2"
                       |)""".stripMargin
      val orgSplitted = split(buildSbt)(orgSplitter)

      printSeq(orgSplitted._1)
      printSeq(orgSplitted._2)

      val scalaniaSplitter = new EvaluateConfigurationsScalania

      val scalaniaSplitted = split(buildSbt)(scalaniaSplitter)
      printSeq(scalaniaSplitted._1)
      printSeq(scalaniaSplitted._2)

    }
  }


  private def printSeq[A](seq: Seq[A]) = println(seq.mkString("\n", "\n", "\n"))

}
