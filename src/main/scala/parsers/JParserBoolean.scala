package parsers

import entries.streamContainers.JData
import entries.{JEntry, JPrimBoolean}
import exceptions.JParseException

/**
 * Parser booleanu. Ma pouze start (rozparsuje cely boolean). Nema nasledujici kroky.
 */
class JParserBoolean(data : JData) extends JParser(data) {

  private var _boolValue : String = ""

  def appendChar(ch : Char) : JParserBoolean = {
    this._boolValue += ch
    this
  }

  override def parseStart(): JEntry = {

    var ch : Char = 0: Char
    val allowed : String = "truefals"

    while (this._it.nonEmpty) {
      ch = this._it.next()

      if (ch.isWhitespace) {
        // skip ...

      } else if (allowed.indexOf(ch) == -1) {
        this._it.saveIndex()
        throw JParseException("Invalid character `" + ch + "` at boolean parsing", this._it)

      } else {
        this._boolValue += ch
      }
    }

    if (this._boolValue.equals("true")) {
      this._entry = new JPrimBoolean(true)

    } else if (this._boolValue.equals("false")) {
      this._entry = new JPrimBoolean(false)

    } else {
      this._it.saveIndex()
      throw JParseException("Invalid boolean value `" + this._boolValue + "`", this._it)
    }

    this._isParsed = true
    this._entry
  }
}