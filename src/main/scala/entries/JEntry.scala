package entries

import entries.containers.{JArray, JObject}
import entries.streamContainers.JData
import helpers.PathHelper
import transport.JGetResult

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

abstract class JEntry {

  def getValue[T >: Any]: T
  def getEntry : JEntry = this

  def isStringPropQueryable : Boolean = false
  def isIntPropQueryable : Boolean = false
  def isQueryable : Boolean = this.isStringPropQueryable || this.isIntPropQueryable
  def isPrimitive : Boolean = false

  def queryStringProp(prop : String) : Option[JEntry] = ???
  def queryIntProp(prop : Int) : Option[JEntry] = ???
  
  def getAllSubchilds : List[JEntry] =
    List.empty[JEntry]

  def getWholeSubtree : List[JEntry] = {
    val result : ArrayBuffer[JEntry] = new ArrayBuffer[JEntry]()
    val stack : mutable.Stack[JEntry] = new mutable.Stack[JEntry]()

    stack.push(this)

    while (stack.nonEmpty) {
      val current : JEntry = stack.pop()

      result.addOne(current)
      stack.addAll(current.getAllSubchilds)
    }

    List.empty[JEntry].appendedAll(result)
  }

  def toJLON: String

  def toJLONpretty(ident : Int) : String
  def toJLONpretty: String =
    this.toJLONpretty(0)

  def toStruct(ident : Int): String
  def toStruct: String =
    this.toStruct(0)

  /**
   * Dotaz na hodnotu v ramci JLONu
   * @param path Cesta
   * @return Hodnota
   */
  def get(path : String) : JGetResult = {

    val pathParts = PathHelper.parsePath(path)
    var ref : Option[JEntry] = Some(this)
    var beforeRef : Option[JEntry] = Some(this)

    if (!ref.get.isQueryable) {
      throw new Exception("This JLON is not queryable")
    }

    if (pathParts.isEmpty) {
      ref = None
    }

    while (pathParts.nonEmpty && ref.isDefined) {
      val current : String = pathParts(0)

      beforeRef = ref

      if (ref.get.isStringPropQueryable) {
        ref = ref.get.queryStringProp(current)

      } else if (ref.get.isIntPropQueryable) {

        try {
          val cNum = current.toInt

          ref = ref.get.queryIntProp(cNum)

        } catch {
          case e: NumberFormatException =>
            ref = None
        }

      } else {
        ref = None
      }

      if (ref.isDefined) {
        pathParts.remove(0)
      }
    }

    if (ref.isDefined && !ref.get.asInstanceOf[JData].isParsed) {
      // neni rozparsovano - pak i entry je typu JData
      new JGetResult(ref, Some(ref.get.asInstanceOf[JData]), Some(beforeRef.get.asInstanceOf[JData]), pathParts)

    } else if (ref.isDefined) {
      new JGetResult(Some(ref.get.getEntry), Some(ref.get.asInstanceOf[JData]), Some(beforeRef.get.asInstanceOf[JData]), pathParts)

    } else {
      new JGetResult(None, None, Some(beforeRef.get.asInstanceOf[JData]), pathParts)
    }
  }

  /**
   * Dotaz na korenovou hodnotu
   * @return Hodnota
   */
  def get() : JGetResult =
    new JGetResult(Some(this.getEntry), Some(this.asInstanceOf[JData]), Some(this.asInstanceOf[JData]), ArrayBuffer.empty)

  def set(path : String, value: String): Unit =
    this.setData(this.get(path), (dt) => dt.setEntry(new JPrimString(value)))

  def set(path : String, value: Boolean): Unit =
    this.setData(this.get(path), (dt) => dt.setEntry(new JPrimBoolean(value)))

  def set(path : String, value: Int): Unit =
    this.setData(this.get(path), (dt) => dt.setEntry(new JPrimNumber(value)))

  def set(path : String, value: List[Any]): Unit =
    this.setData(this.get(path), (dt) => dt.setEntry(new JArray(value)))

  def set(path : String, value: Map[String, Any]): Unit =
    this.setData(this.get(path), (dt) => dt.setEntry(new JObject(value)))

  def set(path : String, data : LazyList[Char]): Unit =
    this.setData(this.get(path), (dt) => dt.setData(data))


  def set(value: String): Unit =
    this.setData(this.get(), (dt) => dt.setEntry(new JPrimString(value)))

  def set(value: Boolean): Unit =
    this.setData(this.get(), (dt) => dt.setEntry(new JPrimBoolean(value)))

  def set(value: Int): Unit =
    this.setData(this.get(), (dt) => dt.setEntry(new JPrimNumber(value)))

  def set(value: List[Any]): Unit =
    this.setData(this.get(), (dt) => dt.setEntry(new JArray(value)))

  def set(value: Map[String, Any]): Unit =
    this.setData(this.get(), (dt) => dt.setEntry(new JObject(value)))

  def set(data : LazyList[Char]): Unit =
    this.setData(this.get(), (dt) => dt.setData(data))


  private def setData(g : JGetResult, dataRefCallback : (JData) => Unit): Unit = {
    if (g.dataEntry.isDefined) {
      dataRefCallback(g.dataEntry.get)

    } else if (g.beforeDataEntry.isDefined) {

      var cRef : JData = g.beforeDataEntry.get
      val it = g.restPath.iterator

      while (it.hasNext) {
        val pathPart = it.next()

        cRef.parseWhole()

        var addDataRef : JData = null

        addDataRef = cRef.createDataObject(None)
        addDataRef.setEntry(new JObject)

        if (cRef.getEntry.isInstanceOf[JObject]) {
          cRef.getEntry.asInstanceOf[JObject].addItem(pathPart, addDataRef)
          cRef = addDataRef

        } else if (cRef.getEntry.isInstanceOf[JArray]) {

          val idx : Int = pathPart.toInt
          val arr : JArray = cRef.getEntry.asInstanceOf[JArray];

          if (idx == -1 || idx == arr.getItemCount) {

            arr.addItem(idx, addDataRef)
            cRef = addDataRef

          } else {
            throw new Exception("Invalid array index to set!")
          }

        } else {
          throw new Exception("Cannot set to entry!")
        }

        if (!it.hasNext) {
          // pokud jiz nejsou dalsi prvky cesty, zavola se callback pro upravu entry
          dataRefCallback(cRef)
        }
      }

    } else {
      throw new Exception("Cannot set property!")
    }
  }

}
