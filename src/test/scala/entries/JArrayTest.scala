package entries

import entries.containers.JArray
import org.scalatest.funsuite.AnyFunSuite

class JArrayTest extends AnyFunSuite {

  test("basic array test") {

    val arr : JArray = new JArray()

    assertResult(true)(arr.isQueryable)
    assertResult(true)(arr.isIntPropQueryable)
    assertResult(false)(arr.isStringPropQueryable)
    assertResult("[]")(arr.toJLON)
  }

  test("array with items test") {

    val arr : JArray = new JArray()
    var sbt : List[JEntry] = List.empty

    sbt = sbt.appended(arr)
    sbt = sbt.appended(arr.addItem(-1, 45))
    sbt = sbt.appended(arr.addItem(-1, "nejaky text"))
    sbt = sbt.appended(arr.addItem(-1, value = true))

    assertResult("[45,\"nejaky text\",true]")(arr.toJLON)
    assertResult(3)(arr.getItemCount)
    assertResult(sbt)(arr.getWholeSubtree)
    assertResult("0,1,2")(arr.getProperties.mkString(","))

    assertResult(None)(arr.getItem(10))
    assertResult(45)(arr.getItem(0).get.getValue)
    assertResult("nejaky text")(arr.getItem(1).get.getValue)
    assertResult(true)(arr.getItem(2).get.getValue)
  }
}