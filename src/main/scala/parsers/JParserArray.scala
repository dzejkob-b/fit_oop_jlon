package parsers

import entries.JEntry
import entries.containers.JArray
import entries.streamContainers.JData
import exceptions.JParseException
import iterators.IndexedIterator
import scanners.ListScanner

import scala.collection.mutable.ListBuffer

/**
 * Parser pole. Ma kroky start a step.
 */
class JParserArray(data : JData) extends JParser(data) {

  private var _innerReadList : ListBuffer[Char] = ListBuffer.empty[Char]

  override def isIntPropQueryable : Boolean =
    true

  override def parseStart(): JEntry = {
    this._entry = new JArray
    this._entry
  }

  override def parseStep(): Boolean = {

    if (this._isParsed) {
      return false
    }

    var isFinish : Boolean = false

    while (this._it.nonEmpty && !isFinish) {
      this._it.saveIndex()

      if (!ListScanner.readInnerContentToBufferTill(this._it, List.empty[Char].appended(',').appended(']'), this._innerReadList)) {
        throw JParseException("Syntax error in array next item", this._it)
      }

      val lastChar : Char = this._innerReadList.last;

      this._innerReadList.remove(this._innerReadList.size - 1)

      if (ListScanner.isWhitespacesOnly(new IndexedIterator[Char](LazyList.empty.appendedAll(this._innerReadList)))) {
        // pouze prazdne znaky

        if (lastChar.equals(']')) {
          isFinish = true
        }

      } else {
        //println("Array item: " + this._innerReadList + ", Last: |" + lastChar + "|")

        // zde to vklada jako entry nerozparsovane data
        this._entry.asInstanceOf[JArray].addItem(-1, this._data.createDataObject(Some(LazyList.empty.appendedAll(this._innerReadList))))

        this._innerReadList = ListBuffer.empty[Char]

        if (lastChar.equals(']')) {
          isFinish = true

        } else {
          // proveden krok parsovani
          return true
        }
      }
    }

    if (!isFinish) {
      this._it.saveIndex()
      throw JParseException("Unclosed array section", this._it)
    }

    this._isParsed = true

    true
  }
}