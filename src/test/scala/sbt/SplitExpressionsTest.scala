package sbt

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec

@RunWith(classOf[JUnitRunner])
class SplitExpressionsTest extends FlatSpec with SplitExpressionsBehavior {

  "EvaluateConfigurationsOriginal" should behave like oldExpressionsSplitter(new EvaluateConfigurationsOriginal)
  
  "EvaluateConfigurationsScalania" should behave like oldExpressionsSplitter(new EvaluateConfigurationsScalania)
  
  "EvaluateConfigurationsScalania" should behave like newExpressionsSplitter(new EvaluateConfigurationsScalania)
  
}