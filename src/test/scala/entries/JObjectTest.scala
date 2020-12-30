package entries

import entries.containers.JObject
import org.scalatest.funsuite.AnyFunSuite

class JObjectTest extends AnyFunSuite {

  test("basic object test") {

    val obj : JObject = new JObject()

    assertResult(true)(obj.isQueryable)
    assertResult(true)(obj.isStringPropQueryable)
    assertResult(false)(obj.isIntPropQueryable)
    assertResult("{}")(obj.toJLON)
  }

  test("object with items test") {

    val obj : JObject = new JObject()
    var sbt : List[JEntry] = List.empty

    sbt = sbt.appended(obj)
    sbt = sbt.appended(obj.addItem("prop-a", "bla bla bla"))
    sbt = sbt.appended(obj.addItem("prop-b", 49))
    sbt = sbt.appended(obj.addItem("prop-c", value = true))
    sbt = sbt.appended(obj.addItem("prop-d", value = false))
    sbt = sbt.appended(obj.addItem("prop-e", -17))

    assertResult("{\"prop-a\":\"bla bla bla\",\"prop-b\":49,\"prop-c\":true,\"prop-d\":false,\"prop-e\":-17}")(obj.toJLON)
    assertResult(5)(obj.getItemCount)
    assertResult(sbt)(obj.getWholeSubtree)
    assertResult("prop-a,prop-b,prop-c,prop-d,prop-e")(obj.getProperties.mkString(","))

    assertResult(None)(obj.getItem("neni"))
    assertResult("bla bla bla")(obj.getItem("prop-a").get.getValue)
    assertResult(49)(obj.getItem("prop-b").get.getValue)
  }
}