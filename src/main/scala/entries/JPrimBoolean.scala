package entries

class JPrimBoolean(value : Boolean) extends JEntry {

  private val _value : Boolean = value;

  override def getValue[T >: Any]: Boolean = {
    this._value
  }

  override def isPrimitive : Boolean = true

  override def toJLON: String =
    if (this._value) "true" else "false"

  override def toJLONpretty(ident : Int): String =
    this.toJLON

  override def toStruct(ident : Int): String =
    "JPrimBoolean(" + this._value.toString + ")"
}