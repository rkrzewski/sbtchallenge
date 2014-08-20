package sbt

import scala.reflect.internal.util.BatchSourceFile
import scala.tools.nsc.ast.parser.Tokens._
import scala.tools.reflect.{FrontEnd, ReflectGlobal, ToolBoxError}

case class ParserError(infos:Set[FrontEnd#Info],th:Throwable=null) extends Exception(th)

class EvaluateConfigurationsScalania extends SplitExpressions {

  import scala.reflect.runtime._
  import scala.reflect.runtime.universe._
  import scala.tools.reflect.ToolBox
  val EOL = scala.util.Properties.lineSeparator
  def parse(toolbox: ToolBox[universe.type],code: String): Tree = {
    //    type ToolBoxImpl = ToolBox[universe.type]{
    //      val compiler : ReflectGlobal
    //    }

    val toolboxImpl = toolbox.asInstanceOf[ToolBox[universe.type]{
      val compiler : ReflectGlobal
    }]
    val run = new toolboxImpl.compiler.Run
    import toolboxImpl.compiler._
    toolboxImpl.compiler.reporter.reset()
    val file = new BatchSourceFile("<toolbox>", code)
    val unit = new CompilationUnit(file)
    phase = run.parserPhase
    val parser = new syntaxAnalyzer.UnitParser(unit)

    val parsed = parser.templateStats()
    parser.accept(EOF)
    throwIfErrors(toolbox)

    parsed match {
      case expr :: Nil => expr.asInstanceOf[universe.Tree]
      case stats :+ expr => Block(stats, expr).asInstanceOf[universe.Tree]
    }
  }

  def throwIfErrors(toolbox: ToolBox[universe.type]) = {
    if (toolbox.frontEnd.hasErrors) {
      var msg = "reflective compilation has failed: " + EOL + EOL
      msg += toolbox.frontEnd.infos
      throw ToolBoxError(msg)
    }
  }



  def splitExpressions(lines: Seq[String]): (Seq[(String, Int)], Seq[(String, LineRange)]) =
    {
      import scala.reflect.runtime._
      import scala.reflect.runtime.universe._
      import scala.tools.reflect.ToolBox
      val mirror = universe.runtimeMirror(this.getClass.getClassLoader)
      val toolbox = mirror.mkToolBox(options = "-Yrangepos")
      val merged = lines.mkString("\n")
      val parsed = parse(toolbox,merged)
      val parsedTrees = parsed match {
        case Block(stmt, expr) =>
          stmt :+ expr
        case t: Tree =>
          Seq(t)
      }

      val (imports, statements) = parsedTrees partition (_ match {
        case _: Import => true
        case _ => false
      })

      def convertImport(t: Tree): (String, Int) =
        (merged.substring(t.pos.start, t.pos.end), t.pos.line)
      def convertStatement(t: Tree): (String, LineRange) =
        (merged.substring(t.pos.start, t.pos.end), LineRange(t.pos.line, t.pos.end))

      (imports map convertImport, statements map convertStatement)
    }
}