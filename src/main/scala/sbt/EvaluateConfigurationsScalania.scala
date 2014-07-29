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
      //      println(parsed)
      val parsedTrees = parsed match {
        case apply: Apply =>
          Seq(apply)
        case Block(stmt, expr) =>
          stmt :+ expr
      }
      def convertTree(t: Tree): (String, LineRange) =
        (merged.substring(t.pos.start, t.pos.end), LineRange(t.pos.start, t.pos.end))
      val parsedExpressions = parsedTrees map (convertTree)

      (Seq(), parsedExpressions)
    }
}