package entries.streamContainers

import entries.JEntry
import helpers.PrintHelper

/**
 * Rozsireni nerozparsovaneho JLONu o lazy funkcionalitu.
 * @param data Vstupni stream znaku
 */
class JDataLazy(data : Option[LazyList[Char]]) extends JData(data) {

  override def onInit(): Unit = {
    // tady se uz cele rozparsovani nedeje ...
  }

  override def createDataObject(data : Option[LazyList[Char]]) : JData =
    new JDataLazy(data)

  /**
   * Kompletne rozparsovane entries vraci v jejich originalnim typu
   * a ty ktere jsou nerozparsovane ponecha jako JData / JDataLazy.
   * @return List Entries
   */
  override def getAllSubchilds : List[JEntry] = {
    if (this._entry != null) {
      this._entry.getAllSubchilds.map((e : JEntry) => {
        if (e.isInstanceOf[JData] && e.asInstanceOf[JData].isParsed) {
          e.asInstanceOf[JData].getEntry
        } else {
          e
        }
      })

    } else {
      List.empty
    }
  }

  override def getEntry : JEntry = {
    this.parseStartCheck()
    this._entry
  }

  override def isStringPropQueryable : Boolean = {
    this.parseStartCheck()
    this._entry.isStringPropQueryable
  }

  override def isIntPropQueryable : Boolean = {
    this.parseStartCheck()
    this._entry.isIntPropQueryable
  }

  private def parseStartCheck() : Unit = {
    // provede se prvni krok parsovani cimz se nacte typ pozadovane JEntry
    if (!this._isParsed && this._parser == null) {
      this._entry = this.parseStart()
    }
  }

  override def queryStringProp(prop : String) : Option[JEntry] = {
    this.queryPropAny[String](prop,
      (entry) => entry.isStringPropQueryable,
      (entry, prop) => entry.queryStringProp(prop))
  }

  override def queryIntProp(prop : Int) : Option[JEntry] = {
    this.queryPropAny[Int](prop,
      (entry) => entry.isIntPropQueryable,
      (entry, prop) => entry.queryIntProp(prop))
  }

  private def queryPropAny[T](prop : T, queryableCall : (JEntry) => Boolean, findPropCall : (JEntry, T) => Option[JEntry]) : Option[JEntry] = {
    if (this._isParsed) {
      return findPropCall(this._entry, prop)
    }

    this.parseStartCheck()

    var entryFound : Option[JEntry] = None

    if (queryableCall(this._entry)) {
      // postupne parsovani a testovani, zda neni pozadovana property jiz k nalezeni
      while (entryFound.isEmpty && this._parser.parseStep()) {
        entryFound = findPropCall(this._entry, prop)
      }
    }

    if (this._parser.isParsed) {
      // vsechny kroky provedeny - jiz je rozparsovano
      this._parser = null
      this._isParsed = true
    }

    entryFound
  }

  override def toJLON: String = {
    if (this._isParsed) {
      this._entry.toJLON

    } else {
      // nerozparsovane vnitrky jdou do JLONu tak jak jsou nacteny
      PrintHelper.lazyListToString(this._data)
    }
  }

  override def toJLONpretty(ident : Int): String = {
    if (this._isParsed) {
      this._entry.toJLONpretty(ident)

    } else {
      PrintHelper.lazyListToString(this._data)
    }
  }

}