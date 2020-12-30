package entries

class JPrimNumber(value : Integer) extends JEntry {

  private val _value : Integer = value;

  override def getValue[T >: Any]: Integer = {
    this._value
  }

  override def isPrimitive : Boolean = true

  override def toJLON: String =
    this._value.toString

  override def toJLONpretty(ident : Int): String =
    this.toJLON

  override def toStruct(ident : Int): String =
    "JPrimNumber(" + this._value.toString + ")"
}