package sbt

import scala.tools.reflect.ToolBoxError


class EvaluateConfigurationsScalania extends SplitExpressions {


  def splitExpressions(lines: Seq[String]): (Seq[(String, Int)], Seq[(String, LineRange)]) = {
    import scala.reflect.runtime._
    import scala.reflect.runtime.universe._
    import scala.tools.reflect.ToolBox
    val mirror = universe.runtimeMirror(this.getClass.getClassLoader)
    val toolbox = mirror.mkToolBox(options = "-Yrangepos")
    val merged = lines.mkString("\n")
    val parsed =
      try {
        toolbox.parse(merged)
      } catch {
        case e:ToolBoxError =>
          println(s"${toolbox.frontEnd.infos}")
          throw e
      }
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
