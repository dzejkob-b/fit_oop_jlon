package parsers

import entries.streamContainers.JData
import entries.{JEntry, JPrimNumber}
import exceptions.JParseException
import scanners.ListScanner

/**
 * Parser cisla. Ma pouze start (rozparsuje se cele cislo). Nema nasledujici kroky.
 */
class JParserNumber(data : JData) extends JParser(data) {

  private var _numValue : String = ""

  def appendChar(ch : Char) : JParserNumber = {
    this._numValue += ch
    this
  }

  private def createEntry() : Unit = {
    try {
      this._entry = new JPrimNumber(this._numValue.toDouble.toInt)

    } catch {
      case e: NumberFormatException => {
        this._it.saveIndex()
        throw JParseException("Invalid number format `" + this._numValue + "`", this._it)
      }
    }
  }

  override def parseStart(): JEntry = {

    var ch : Char = 0: Char
    var isFinish : Boolean = false

    while (this._it.nonEmpty) {
      ch = this._it.next()

      if (ch.isDigit) {
        this._numValue += ch

      } else if (ch.equals('-') && this._numValue.isBlank) {
        this._numValue += ch

      } else if (ch.equals('.') && this._numValue.indexOf('.') == -1) {
        this._numValue += ch

      } else if (ch.isWhitespace && this._numValue.isBlank) {
        // ok ...

      } else if (ch.isWhitespace) {

        this.createEntry()

        if (ListScanner.isWhitespacesOnly(this._it)) {
          isFinish = true

        } else {
          this._it.saveIndex()
          throw JParseException("Error at number parsing", this._it)
        }

      } else {
        this._it.saveIndex()
        throw JParseException("Error at number parsing", this._it)
      }

    }

    if (!isFinish) {
      // v digitu muze byt jen validni digit
      this.createEntry()
    }

    this._isParsed = true
    this._entry
  }
}