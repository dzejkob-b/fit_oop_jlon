package parsers

import entries.JEntry
import entries.streamContainers.JData
import iterators.IndexedIterator

/**
 * Trida parseru. Prochazi iterator a vrati JEntry
 */
abstract class JParser(data : JData) {

  protected val _data : JData = data
  protected val _it : IndexedIterator[Char] = data.getIterator
  protected var _entry : JEntry = _
  protected var _isParsed : Boolean = false

  def isStringPropQueryable : Boolean =
    false
  def isIntPropQueryable : Boolean =
    false

  def parseStart() : JEntry
  def isParsed: Boolean =
    this._isParsed
  def parseStep() : Boolean =
    false

}