package sbt

import org.scalatest.FlatSpec

trait SplitExpression {
  def split(s: String)(implicit splitter: SplitExpressions) = splitter.splitExpressions(s.split("\n").toSeq)
}

trait SplitExpressionsBehavior extends SplitExpression { this: FlatSpec =>

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

     it should "parse a config containing two imports and a setting" in {
        val (imports, settingsAndDefs) = split(
           """import foo.Bar
              import foo.Bar

             version := "1.0"
           """.stripMargin)
        assert(imports.size === 2)
        assert(settingsAndDefs.size === 1)
     }

    it should "parse a config containgn a def" in {
      val (imports, settingsAndDefs) = split("""def foo(x: Int) = {
  x + 1
}""")
      assert(imports.isEmpty)
      assert(!settingsAndDefs.isEmpty)
    }

    it should "parse a config containgn a val" in {
      val (imports, settingsAndDefs) = split("""val answer = 42""")
      assert(imports.isEmpty)
      assert(!settingsAndDefs.isEmpty)
    }

    it should "parse a config containgn a lazy val" in {
      val (imports, settingsAndDefs) = split("""lazy val root = (project in file(".")).enablePlugins­(PlayScala)""")
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

     it should "parse a setting and val without intervening blank line" in {
        val (imports, settings) = split("""version := "1.0"
lazy val root = (project in file(".")).enablePlugins­(PlayScala)""")

        assert(imports.isEmpty)
        assert(settings.size === 2)
     }


     it should "parse a config containing two imports and a setting with no blank line" in {
        val (imports, settingsAndDefs) = split(
           """import foo.Bar
              import foo.Bar
             version := "1.0"
           """.stripMargin)
        assert(imports.size === 2)
        assert(settingsAndDefs.size === 1)
     }

  }

}