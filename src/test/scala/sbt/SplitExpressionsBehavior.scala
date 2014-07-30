package sbt

import org.scalatest.FlatSpec

trait SplitExpressionsBehavior { this: FlatSpec =>
  
  def split(s: String)(implicit splitter: SplitExpressions) = splitter.splitExpressions(s.split("\n").toSeq)
  
  def oldExpressionsSplitter(implicit splitter: SplitExpressions) {

    it should "parse a simple setting" in {
      val (imports, settings) = split("""version := "1.0"""")
      assert(settings.head._1 === """version := "1.0"""")
      
      assert(imports.isEmpty)
      assert(!settings.isEmpty)
    }
    
    it should "parse a config containing a single import" in {
      val (imports, settings) = split("""import foo.Bar""")
      assert(!imports.isEmpty)
      assert(settings.isEmpty)
    }
    
  }
  
  def newExpressionsSplitter(implicit splitter: SplitExpressions) {
    
    it should "parse a two settings without intervening blank line" in {
      val (imports, settings) = split("""version := "1.0"
scalaVersion := "2.10.4"""")
      
      assert(imports.isEmpty)
      assert(settings.size === 2)
    }
    
  }
  
}