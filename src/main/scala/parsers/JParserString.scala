package parsers

import entries.streamContainers.JData
import entries.{JEntry, JPrimString}
import exceptions.JParseException
import scanners.ListScanner

/**
 * Parser stringu. Ma pouze start (rozparsuje se cely string). Nema nasledujici kroky.
 */
class JParserString(data : JData) extends JParser(data) {

  private var _strValue : String = ""

  private def createEntry() : Unit = {
    this._entry = new JPrimString(this._strValue)
  }

  override def parseStart(): JEntry = {

    var ch : Char = 0: Char
    var isFinish : Boolean = false

    while (this._it.nonEmpty) {
      ch = this._it.next()

      if (ch == '\\') {
        if (this._it.hasNext) {
          this._strValue += this._it.next()

        } else {
          this._it.saveIndex()
          throw JParseException("Error at string parsing", this._it)
        }

      } else if (ch.equals('"')) {

        this.createEntry()

        if (ListScanner.isWhitespacesOnly(this._it)) {
          isFinish = true

        } else {
          throw JParseException("Error at string parsing", this._it)
        }

      } else {
        this._strValue += ch
      }
    }

    if (!isFinish) {
      this._it.saveIndex()
      throw JParseException("Unclosed string section", this._it)
    }

    this._isParsed = true
    this._entry
  }
}