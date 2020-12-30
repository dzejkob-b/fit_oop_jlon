package iterators

import scala.reflect.ClassTag

/**
 * Rozsireni iteratoru o pocitani indexu aby bylo mozne zobrazit kde je syntakticka chyba.
 * Pouziva kompozici z duvodu, aby bylo mozne ve vsech pripadech, ktere jsou pouzivany, bezpecne sledovat index pozice.
 *
 * @param dt LazyList nad kterym se iteruje
 * @tparam T Typ iteratoru
 */
class IndexedIterator[T: ClassTag](dt : LazyList[T]) {

  private val _dt : LazyList[T] = dt
  private val _it : Iterator[T] = this._dt.iterator
  private var _index : Int = -1
  private var _savedIndex : Int = -1

  def getIndex : Int =
    this._index

  def saveIndex() : Unit =
    this._savedIndex = this._index

  def getSavedIndex : Int =
    this._savedIndex

  def hasNext : Boolean =
    this._it.hasNext

  def nonEmpty : Boolean =
    this._it.nonEmpty

  def next() : T = {
    this._index += 1
    this._it.next()
  }

  def forall(p : T => Boolean) : Boolean = {
    while (this.hasNext) {
      if (!p(this.next())) {
        return false
      }
    }

    true
  }

  def getArrayTo(toIdx : Int) : Array[T] = {
    val arr : Array[T] = new Array[T](toIdx)
    this._dt.iterator.copyToArray(arr, 0, toIdx)
    arr
  }


}