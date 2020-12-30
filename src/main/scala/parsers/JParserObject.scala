package parsers

import entries.JEntry
import entries.containers.JObject
import entries.streamContainers.JData
import exceptions.JParseException
import scanners.ListScanner

import scala.collection.mutable.ListBuffer

/**
 * Parser objektu s properties. Ma kroky start a step.
 */
class JParserObject(data : JData) extends JParser(data) {

  private var _innerReadList : ListBuffer[Char] = ListBuffer.empty[Char]
  private var _parsedPropNum : Int = _

  override def isStringPropQueryable : Boolean =
    true

  override def parseStart(): JEntry = {
    this._entry = new JObject
    this._entry
  }

  override def parseStep(): Boolean = {

    if (this._isParsed) {
      return false
    }

    var isFinish : Boolean = false
    var inProp : Boolean = false
    var propName : String = ""
    var ch : Char = 0: Char

    while (this._it.nonEmpty && !isFinish) {
      ch = this._it.next()

      if (inProp) {

        if (ch == '\\') {
          if (this._it.hasNext) {
            propName += this._it.next()

          } else {
            this._it.saveIndex()
            throw JParseException("Syntax error", this._it)
          }

        } else if (ch == '"') {

          this._it.saveIndex()

          if (!ListScanner.readWhitespacesTill(this._it, ':')) {
            throw JParseException("Syntax error at property `" + propName + "`", this._it)
          }

          this._it.saveIndex()

          if (!ListScanner.readInnerContentToBufferTill(this._it, List.empty[Char].appended(',').appended('}'), this._innerReadList)) {
            throw JParseException("Syntax error at property `" + propName + "` value", this._it)
          }

          val lastChar : Char = this._innerReadList.last;

          this._innerReadList.remove(this._innerReadList.size - 1)

          //println("Property: " + propName + ", Last: |" + lastChar + "|, Cont: " + PrintHelper.lazyListToString(LazyList.empty.appendedAll(this._innerReadList)))

          // zde to vklada jako entry nerozparsovane data
          this._entry.asInstanceOf[JObject].addItem(propName, this._data.createDataObject(Some(LazyList.empty.appendedAll(this._innerReadList))))

          propName = ""
          inProp = false

          this._parsedPropNum += 1
          this._innerReadList = ListBuffer.empty[Char]

          if (lastChar.equals('}')) {
            isFinish = true

          } else {
            // proveden krok parsovani
            return true
          }

        } else {
          propName += ch
        }

      } else if (ch.isWhitespace) {
        // skip ...

      } else if (ch.equals('"')) {
        inProp = true

      } else if (ch.equals('}')) {

        if (this._parsedPropNum > 0) {
          this._it.saveIndex()
          throw JParseException("Unexpected bracket `" + ch + "`", this._it)

        } else if (!ListScanner.isWhitespacesOnly(this._it)) {
          throw JParseException("Error at string parsing", this._it)

        } else {
          isFinish = true
        }

      } else {
        this._it.saveIndex()
        throw JParseException("Unexpected character`" + ch + "`", this._it)
      }
    }

    if (!isFinish) {
      this._it.saveIndex()
      throw JParseException("Unclosed object section", this._it)
    }

    this._isParsed = true

    true
  }
}