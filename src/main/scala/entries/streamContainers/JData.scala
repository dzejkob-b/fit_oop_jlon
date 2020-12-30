package entries.streamContainers

import entries.JEntry
import exceptions.JParseException
import helpers.PrintHelper
import iterators.IndexedIterator
import parsers._
import scanners.ListScanner

import scala.collection.mutable

/**
 * Top level trida, ktera predstavuje nerozparsovany JLON.
 * Je svazan kompozici s finalnim JEntry.
 * Rozparsovani provede ulozeni prvku JEntry se kterym se posleze manipuluje (dotazovani apod.).
 * @param data Vstupni stream znaku
 */
class JData(data : Option[LazyList[Char]]) extends JEntry {

  protected var _data : LazyList[Char] = if (data.isDefined) data.get else null
  protected var _entry : JEntry = _
  protected var _parser : JParser = _
  protected var _isParsed : Boolean = false
  protected var _it : IndexedIterator[Char] = if (data.isDefined) new IndexedIterator[Char](this._data) else null

  this.onInit()

  protected def onInit(): Unit = {
    if (this._data != null) {
      this.parseWhole()
    }
  }

  def createDataObject(data : Option[LazyList[Char]]) : JData =
    new JData(data)

  def getIterator: IndexedIterator[Char] =
    this._it

  override def getValue[T >: Any]: Any =
    this.getEntry.getValue

  override def getEntry : JEntry = {
    if (!this._isParsed) {
      // pri vraceni hodnoty se parsuje cele
      this.parseWhole()
    }

    // zde entry nemuze byt null - bud je nastaveno, nebo nastala JParseException
    this._entry
  }

  def setEntry(en : JEntry) : Unit = {
    if (en.isInstanceOf[JData]) {
      throw new Exception("Cannot set JData entry directly!")

    } else {
      // primo nastavena entry
      this._entry = en
      this._data = null
      this._isParsed = true
      this._parser = null
    }
  }

  def getData: LazyList[Char] =
    this._data

  def setData(data : LazyList[Char]) : Unit = {
    this._data = data;
    this._it = new IndexedIterator[Char](this._data)
    this._entry = null;
    this._isParsed = false
    this._parser = null
  }

  def isParsed : Boolean =
    this._isParsed

  override def toJLON: String =
    this.getEntry.toJLON

  override def toJLONpretty(ident : Int): String =
    this.getEntry.toJLONpretty(ident)

  override def toStruct(ident : Int): String = {
    val sb : mutable.StringBuilder = new mutable.StringBuilder()

    sb.append("JData(")

    if (this._isParsed) {
      sb.append("isParsed=true")
    } else {
      sb.append("isParsed=false")
    }

    sb.append(")")

    if (this._entry != null) {
      sb.append("\n")
      sb.append(PrintHelper.identLevel(ident + 1))
      sb.append(this._entry.toStruct(ident + 1))
    }

    if (!this._isParsed) {
      sb.append("\n")
      sb.append(PrintHelper.identLevel(ident + 1))
      sb.append("Unparsed data: \n")
      sb.append(PrintHelper.identLevel(ident + 1))
      sb.append(ListScanner.readRestToString(this._it))
    }

    sb.toString()
  }

  override def getAllSubchilds : List[JEntry] =
    this.getEntry.getAllSubchilds

  override def isStringPropQueryable : Boolean =
    this.getEntry.isStringPropQueryable

  override def isIntPropQueryable : Boolean =
    this.getEntry.isIntPropQueryable

  override def queryStringProp(prop : String) : Option[JEntry] =
    this.getEntry.queryStringProp(prop)

  override def queryIntProp(prop : Int) : Option[JEntry] =
    this.getEntry.queryIntProp(prop)

  def parseWhole() : JEntry = {
    if (this._isParsed) {
      return this._entry
    }

    if (this._entry == null) {
      this._entry = this.parseStart()
    }

    while (this.parseStep()) {
      // loop ...
    }

    this._entry
  }

  def parseStart() : JEntry = {

    var ch : Char = 0: Char
    var entry : JEntry = null

    while (this._it.nonEmpty && this._parser == null) {

        ch = this._it.next()

        if (ch.isWhitespace) {
          // skip ...

        } else if (ch.equals('{')) {
          // zacina objekt

          this._parser = new JParserObject(this)
          entry = this._parser.parseStart()

        } else if (ch.equals('[')) {
          // zacina pole

          this._parser = new JParserArray(this)
          entry = this._parser.parseStart()

        } else if (ch.isDigit || ch.equals('-')) {
          // zacina cislo

          this._parser = (new JParserNumber(this)).appendChar(ch)
          entry = this._parser.parseStart()

        } else if (ch.equals('"')) {
          // zacina string

          this._parser = new JParserString(this)
          entry = this._parser.parseStart()

        } else if (ch.equals('t') || ch.equals('f')) {
          // boolean (true, false - t, f)

          this._parser = new JParserBoolean(this).appendChar(ch)
          entry = this._parser.parseStart()

        } else {
          // nesmysl

          this._it.saveIndex()
          throw JParseException("Unexpected character `" + ch + "`", this._it)
        }
    }

    if (this._parser == null) {
      // exception pro bezpecnost - parser musi byt nastaven jinak to je nesmysl
      throw JParseException("Parser not specified!", this._it)
    }

    if (this._parser.isParsed) {
      // stacil jeden krok parseru
      this._parser = null
      this._isParsed = true
    }

    entry
  }

  def parseStep() : Boolean = {
    if (this._parser == null) {
      false

    } else if (this._parser.parseStep()) {
      true

    } else {
      // uz neni co parsovat, kontrola zbytku

      var ch : Char = 0

      while (this._it.nonEmpty) {
        ch = this._it.next()

        if (ch.isWhitespace) {
          // skip ...

        } else {
          // nesmysl na konci

          this._it.saveIndex()
          throw JParseException("Unexpected character `" + ch + "`", this._it)
        }
      }

      this._data = null
      this._it = null

      // jiz je rozparsovano
      this._parser = null
      this._isParsed = true

      false
    }
  }
}