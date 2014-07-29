package sbt

import org.scalatest.FlatSpec

trait SplitExpressionsBehavior { this: FlatSpec =>
  
  def split(s: String)(implicit splitter: SplitExpressions) = splitter.splitExpressions(s.split("\n").toSeq)
  
  def oldExpressionsSplitter(implicit splitter: SplitExpressions) {

    it should "parse a simple setting" in {
      val (imports, settings) = split("""version := "1.0"""")
      assert(settings.head._1 === """version := "1.0"""")
    }
    
  }
  
  def newExpressionsSplitter(implicit splitter: SplitExpressions) {
    
  }
  
}