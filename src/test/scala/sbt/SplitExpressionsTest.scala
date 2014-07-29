package sbt

import org.scalatest.FlatSpec

class SplitExpressionsTest extends FlatSpec with SplitExpressionsBehavior {

  "EvaluateConfigurationsOriginal" should behave like oldExpressionsSplitter(new EvaluateConfigurationsOriginal)
  
  "EvaluateConfigurationsScalania" should behave like oldExpressionsSplitter(new EvaluateConfigurationsScalania)
  
  "EvaluateConfigurationsScalania" should behave like newExpressionsSplitter(new EvaluateConfigurationsScalania)
  
}