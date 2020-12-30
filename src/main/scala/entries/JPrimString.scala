package entries

import helpers.PrintHelper

class JPrimString(value : String) extends JEntry {

  private val _value : String = value;

  override def getValue[T >: Any]: String = {
    this._value
  }

  override def isPrimitive : Boolean = true

  override def toJLON: String =
    PrintHelper.escapedStringWithQuotes(this._value)

  override def toJLONpretty(ident : Int): String =
    this.toJLON

  override def toStruct(ident : Int): String =
    "JPrimString(" + PrintHelper.escapedStringWithQuotes(this._value) + ")"
}