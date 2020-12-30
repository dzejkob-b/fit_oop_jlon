package entries.containers

import entries.streamContainers.{JData, JDataLazy}
import entries.{JEntry, JPrimBoolean, JPrimNumber, JPrimString}

/**
 * Obecny kontejner v ramci JLON, ktery poskytuje random access a pridani prvku.
 *
 * @tparam T Typ klice ktery drzi kontejner
 */
abstract class JCont[T]() extends JEntry {

  def getItemCount: Int
  def getItem(key : T) : Option[JEntry]
  def addItem(key : T, inst : JEntry): JEntry

  def addItem(key : T, value : Boolean): JEntry =
    this.addItem(key, new JPrimBoolean(value))

  def addItem(key : T, value : Int): JEntry =
    this.addItem(key, new JPrimNumber(value))

  def addItem(key : T, value : String): JEntry =
    this.addItem(key, new JPrimString(value))

  def addItem(key : T, value : Any): JEntry = {
    value match {
      case bool: Boolean =>
        this.addItem(key, bool)

      case i: Int =>
        this.addItem(key, i)

      case str: String =>
        this.addItem(key, str)

      case _ =>
        throw new Exception("Invalid argument type!")
    }
  }

  def addItemToParse(key : T, input : String): JEntry =
    this.addItem(key, new JData(Some(LazyList.empty :++ input)))

  def addItemToParseLazy(key : T, input : String): JEntry =
    this.addItem(key, new JDataLazy(Some(LazyList.empty :++ input)))

  /**
   * @return Vrati vsechny nastavene properties
   */
  def getProperties: List[T]

}