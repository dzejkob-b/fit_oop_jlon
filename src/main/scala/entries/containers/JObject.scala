package entries.containers

import entries.JEntry
import helpers.PrintHelper

import scala.collection.mutable

/**
 * Kontejner objektu - mapy prvku.
 */
class JObject() extends JCont[String]() {

  private val _items : mutable.LinkedHashMap[String, JEntry] = mutable.LinkedHashMap()

  def this(it : Map[String, Any]) {
    this()

    it.foreach(c => {
      this.addItem(c._1, c._2)
    })
  }

  override def getValue[T >: Any]: JObject = this

  override def getItemCount: Int =
    this._items.size

  override def getItem(key: String): Option[JEntry] =
    this._items.get(key)

  override def addItem(key: String, inst: JEntry): JEntry = {
    this._items.addOne((key -> inst))
    inst
  }

  def getProperties: List[String] =
    List.empty[String].appendedAll(this._items.keySet)

  override def getAllSubchilds : List[JEntry] = {
    val it = this._items.iterator
    var result : List[JEntry] = List.empty

    while (it.hasNext) {
      result = result.appended(it.next()._2)
    }

    result
  }

  override def toJLON: String = {
    if (this._items.isEmpty) {
      "{}"

    } else {

      def fn(ref : (String, JEntry)) : String = {
        PrintHelper.escapedStringWithQuotes(ref._1) + ":" + ref._2.toJLON
      }

      PrintHelper.toJLONstructure[(String, JEntry)]('{', '}', this._items.iterator, fn)
    }
  }

  override def toJLONpretty(ident : Int): String = {
    if (this._items.isEmpty) {
      "{}\n"

    } else {

      def fn(ref : (String, JEntry), ident : Int) : String = {
        PrintHelper.escapedStringWithQuotes(ref._1) + " : " + ref._2.toJLONpretty(ident + 1)
      }

      PrintHelper.toJLONstructurePretty[(String, JEntry)]('{', '}', ident, this._items.iterator, fn)
   }
  }

  override def toStruct(ident : Int): String = {
    val sb : mutable.StringBuilder = new mutable.StringBuilder()
    val it = this._items.iterator

    sb.append("JObject {\n")

    var begin = true

    while (it.hasNext) {
      if (begin) begin = false
      else sb.append(",\n")

      val ref = it.next()

      sb.append(PrintHelper.identLevel(ident + 1))
      sb.append(PrintHelper.escapedStringWithQuotes(ref._1))
      sb.append(": ")
      sb.append(ref._2.toStruct(ident + 1))
    }

    sb.append("\n")
    sb.append(PrintHelper.identLevel(ident))
    sb.append("}")

    sb.toString()
  }

  override def isStringPropQueryable: Boolean = true

  override def queryStringProp(prop: String): Option[JEntry] = {
    this.getItem(prop)
  }

  def toMap() : Map[String, Any] =
    this._items.map(t => (t._1, t._2.getValue)).toMap

}