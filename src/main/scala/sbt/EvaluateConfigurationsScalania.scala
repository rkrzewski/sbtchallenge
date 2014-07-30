package sbt

class EvaluateConfigurationsScalania extends SplitExpressions {
  def splitExpressions(lines: Seq[String]): (Seq[(String, Int)], Seq[(String, LineRange)]) =
    {
      import scala.reflect.runtime._
      import scala.reflect.runtime.universe._
      import scala.tools.reflect.ToolBox
      val mirror = universe.runtimeMirror(this.getClass.getClassLoader)
      val toolbox = mirror.mkToolBox(options = "-Yrangepos")
      val merged = lines.mkString("\n")
      val parsed = toolbox.parse(merged)
      val parsedTrees = parsed match {
        case imp: Import =>
          Seq(imp)
        case apply: Apply =>
          Seq(apply)
        case Block(stmt, expr) =>
          stmt :+ expr
      }

      val (imports, statements) = parsedTrees partition (_ match {
        case _: Import => true
        case _ => false
      })

      def convertImport(t: Tree): (String, Int) =
        (merged.substring(t.pos.start, t.pos.end), t.pos.start)
      def convertStatement(t: Tree): (String, LineRange) =
        (merged.substring(t.pos.start, t.pos.end), LineRange(t.pos.start, t.pos.end))

      (imports map convertImport, statements map convertStatement)
    }
}