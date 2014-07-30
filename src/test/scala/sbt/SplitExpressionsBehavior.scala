package sbt

import org.scalatest.FlatSpec

trait SplitExpressionsBehavior { this: FlatSpec =>
  
  def split(s: String)(implicit splitter: SplitExpressions) = splitter.splitExpressions(s.split("\n").toSeq)
  
  def oldExpressionsSplitter(implicit splitter: SplitExpressions) {

    it should "parse a simple setting" in {
      val (imports, settingsAndDefs) = split("""version := "1.0"""")
      assert(settingsAndDefs.head._1 === """version := "1.0"""")
      
      assert(imports.isEmpty)
      assert(!settingsAndDefs.isEmpty)
    }
    
    it should "parse a config containing a single import" in {
      val (imports, settingsAndDefs) = split("""import foo.Bar""")
      assert(!imports.isEmpty)
      assert(settingsAndDefs.isEmpty)
    }

    it should "parse a config containgn a def" in {
      val (imports, settingsAndDefs) = split("""def foo(x: Int) = {
  x + 1
}""")
      assert(imports.isEmpty)
      assert(!settingsAndDefs.isEmpty)
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