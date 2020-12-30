package entries.containers

import entries.JEntry
import helpers.PrintHelper

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * Kontejner indexovaneho pole.
 */
class JArray() extends JCont[Int]() {

  private val _items : ArrayBuffer[JEntry] = new ArrayBuffer[JEntry]()

  def this(its : List[Any]) {
    this()

    its.foreach(c => {
      this.addItem(-1, c)
    })
  }

  override def getValue[T >: Any]: JArray = this

  override def getItemCount: Int =
    this._items.size

  override def getItem(key: Int): Option[JEntry] = {
    if (key >= 0 && key < this._items.length) Some(this._items.apply(key))
    else None
  }

  /**
   * Pridani prvku na konec pole. Index je ignorovan.
   * @param key Index prvku je ignorovan
   * @param inst Pridavany prvek
   */
  override def addItem(key: Int, inst: JEntry): JEntry = {
    this._items.addOne(inst)
    inst
  }

  def getProperties: List[Int] =
    List.empty[Int].appendedAll(this._items.indices)

  override def getAllSubchilds : List[JEntry] =
    List.empty[JEntry].appendedAll(this._items)

  override def toJLON: String = {
    if (this._items.isEmpty) {
      "[]"

    } else {

      def fn(ref : JEntry) : String = {
        ref.toJLON
      }

      PrintHelper.toJLONstructure('[', ']', this._items.iterator, fn)
    }
  }

  override def toJLONpretty(ident : Int): String = {
    if (this._items.isEmpty) {
      "[]\n"

    } else {

      def fn(ref : JEntry, ident : Int) : String = {
        ref.toJLONpretty(ident + 1)
      }

      PrintHelper.toJLONstructurePretty('[', ']', ident, this._items.iterator, fn)
    }
  }

  override def toStruct(ident : Int): String = {
    val sb : mutable.StringBuilder = new mutable.StringBuilder()
    val it = this._items.iterator

    sb.append("JArray [\n")

    var begin = true

    while (it.hasNext) {
      if (begin) begin = false
      else sb.append(",\n")

      sb.append(PrintHelper.identLevel(ident + 1))
      sb.append(it.next().toStruct(ident + 1))
    }

    sb.append("\n")
    sb.append(PrintHelper.identLevel(ident))
    sb.append("]")

    sb.toString()
  }

  override def isIntPropQueryable: Boolean = true

  override def queryIntProp(prop: Int): Option[JEntry] = this.getItem(prop)

  def toList() : List[Any] =
    this._items.map(c => c.getValue).toList

}