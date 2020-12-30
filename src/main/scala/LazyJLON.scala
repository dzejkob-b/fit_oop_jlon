import entries.streamContainers.{JData, JDataLazy}

class LazyJLON(input: LazyList[Char]) extends JLON(input) {

  override protected def createDataObject(input : LazyList[Char]) : JData =
    new JDataLazy(Some(input))

}

object LazyJLON {
  def apply (input : LazyList[Char]) : LazyJLON = new LazyJLON(input)

  def apply (input : String) : LazyJLON = LazyJLON(LazyList.empty :++ input)
}
