package sbt

import org.scalatest.FlatSpec
import scala.io.Source

trait WholeFilesSplittingBehavior {
   this: FlatSpec =>

   import java.io.File

   def recursiveListFiles(f: File): Array[File] = {
      val these = f.listFiles
      these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
   }

   val oldSplitter = new EvaluateConfigurationsOriginal
   val newSplitter = new EvaluateConfigurationsScalania

   def oldExpressionsFiles(implicit splitter: SplitExpressions) {

      it should "splitFiles" in {
           val rootPath = getClass.getResource("").getPath + "../old-format/"
           println(s"Reading files from: $rootPath")
           val allFiles = recursiveListFiles(new File(rootPath)).map(_.getAbsolutePath).toList

           for (path <- allFiles) {
              println(s"Procesing file: $path")
              val lines = Source.fromFile(path).getLines().toList
              val newResult = newSplitter.splitExpressions(lines)
              val oldResult = oldSplitter.splitExpressions(lines)
              println(s"New splitter found: ${newResult._1.length} imports and ${newResult._2.length} settings.")
              println(s"Old splitter found: ${oldResult._1.length} imports and ${oldResult._2.length} settings.")
              assert(newResult._1.length === oldResult._1.length)
              assert(newResult._2.length === oldResult._2.length)
              //TODO: compare actual contents (after making both implementations return CharRanges instead of LineRanges)
           }
        }
   }
}
