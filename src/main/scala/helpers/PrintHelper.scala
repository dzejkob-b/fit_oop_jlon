package helpers

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object PrintHelper {

  def escapedString(input : String) : String =
    "([\"\\\\])".r.replaceAllIn(input, "\\\\$1")

  def escapedStringWithQuotes(input : String) : String =
    "\"" + PrintHelper.escapedString(input) + "\""

  def identLevel(ident : Int) : String =
    "\t".repeat(ident)

  def identLevelAppend(ident : Int, append : String) : String =
    "\t".repeat(ident) + append

  def toJLONstructure[T](openChar : Char, closeChar : Char, it : Iterator[T], fn : (T) => String) : String = {
    val sb : mutable.StringBuilder = new mutable.StringBuilder()

    sb.append(openChar)

    var begin = true

    while (it.hasNext) {
      if (begin) begin = false
      else sb.append(",")

      sb.append(fn(it.next()))
    }

    sb.append(closeChar.toString)

    sb.toString()
  }

  def toJLONstructurePretty[T](openChar : Char, closeChar : Char, ident : Int, it : Iterator[T], fn : (T, Int) => String) : String = {
    val sb : mutable.StringBuilder = new mutable.StringBuilder()

    sb.append(openChar)
    sb.append("\n")

    var begin = true

    while (it.hasNext) {
      if (begin) begin = false
      else sb.append(",\n")

      sb.append(PrintHelper.identLevel(ident + 1))
      sb.append(fn(it.next(), ident))
    }

    sb.append("\n")
    sb.append(PrintHelper.identLevelAppend(ident, closeChar.toString))

    sb.toString()
  }

  def lazyListToString(list : LazyList[Char]) : String =
    this.iteratorToString(list.iterator)

  def listBufferToString(buff : ListBuffer[Char]) : String =
    this.iteratorToString(buff.iterator)

  private def iteratorToString(it : Iterator[Char]) : String = {
    val sb : StringBuilder = new StringBuilder

    while (it.hasNext) {
      sb.append(it.next())
    }

    sb.toString()
  }

}
