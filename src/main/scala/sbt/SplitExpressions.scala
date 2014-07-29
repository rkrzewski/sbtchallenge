package sbt

trait SplitExpressions {
  def splitExpressions(lines: Seq[String]): (Seq[(String, Int)], Seq[(String, LineRange)])
}