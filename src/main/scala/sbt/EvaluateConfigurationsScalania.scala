package sbt

class EvaluateConfigurationsScalania extends SplitExpressions {
  def splitExpressions(lines: Seq[String]): (Seq[(String, Int)], Seq[(String, LineRange)]) =
  {
    import scala.reflect.runtime._
    import scala.reflect.runtime.universe._
    import scala.tools.reflect.ToolBox
    import scala.tools.reflect.ToolBoxError
    import scala.compat.Platform.EOL

    val mirror = universe.runtimeMirror(this.getClass.getClassLoader)
    val toolbox = mirror.mkToolBox(options = "-Yrangepos")
    val merged = lines.mkString("\n")
    // block try-catch should be added in private[sbt] def evaluateSbtFile.
    // Here we do not have information about input file
    val parsed =
      try {
        toolbox.parse(merged)
      } catch {
        case e: ToolBoxError =>
          val seq = toolbox.frontEnd.infos.map(i =>
            s"""${i.msg} line: ${i.pos.line}"""
          )
          throw new MessageOnlyException(seq.mkString(EOL))
      }
    val parsedTrees = parsed match {
      case Block(stmt, expr) =>
        stmt :+ expr
      case t: Tree =>
        Seq(t)
    }

    val (imports, statements) = parsedTrees partition {
      case _: Import => true
      case _ => false
    }

    def convertImport(t: Tree): (String, Int) =
      (merged.substring(t.pos.start, t.pos.end), t.pos.start)
    def convertStatement(t: Tree): (String, LineRange) =
      (merged.substring(t.pos.start, t.pos.end), LineRange(t.pos.start, t.pos.end))

    (imports map convertImport, statements map convertStatement)
  }
}