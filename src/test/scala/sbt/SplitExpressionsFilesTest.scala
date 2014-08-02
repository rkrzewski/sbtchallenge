package sbt

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec

@RunWith(classOf[JUnitRunner])
class SplitExpressionsFilesTest extends FlatSpec with WholeFilesSplittingBehavior {

  "EvaluateConfigurationsScalania" should behave like oldExpressionsFiles(new EvaluateConfigurationsScalania)

}