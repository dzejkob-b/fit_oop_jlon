import entries.streamContainers.JData
import transport.JGetResult

class JLON(input : LazyList[Char]) {

  protected val _data : JData = this.createDataObject(input)

  protected def createDataObject(input : LazyList[Char]) : JData =
    new JData(Some(input))

  /**
   * Dotaz na hodnotu v ramci JLONu
   * @param path Cesta
   * @return Finalni typovana hodnota nebo JLON entita
   */
  def get(path : String) : JGetResult =
    this._data.get(path)

  /**
   * Dotaz na korenovou hodnotu
   * @return Finalni typovana hodnota nebo textovy fragment JLON
   */
  def get() : JGetResult =
    this._data.get()

  def set(path : String, value: String): Unit = this._data.set(path, value)
  def set(path : String, value: Boolean): Unit = this._data.set(path, value)
  def set(path : String, value: Int): Unit = this._data.set(path, value)
  def set(path : String, value: List[Any]): Unit = this._data.set(path, value)
  def set(path : String, value: Map[String, Any]): Unit = this._data.set(path, value)
  def set(path : String, data : LazyList[Char]): Unit = this._data.set(path, data)

  def set(value: String): Unit = this._data.set(value)
  def set(value: Boolean): Unit = this._data.set(value)
  def set(value: Int): Unit = this._data.set(value)
  def set(value: List[Any]): Unit = this._data.set(value)
  def set(value: Map[String, Any]): Unit = this._data.set(value)
  def set(data : LazyList[Char]): Unit = this._data.set(data)

  def toJLON : String =
    this._data.toJLON

  def toJLONpretty : String =
    this._data.toJLONpretty(0)

  def toStruct : String =
    this._data.toStruct(0)

  override def toString: String =
    this.toJLON
}

object JLON {
  def apply (input : LazyList[Char]) : JLON = new JLON(input)

  def apply (input : String) : JLON = JLON(LazyList.empty :++ input)
}
