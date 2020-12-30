import entries.containers.{JArray, JObject}
import org.scalatest.funsuite.AnyFunSuite

class JLON_convTest extends AnyFunSuite {

  test("implicit conversion test") {
    val jlon : JLON = JLON("""
      {
        "name1" : "string value",
        "name2" : 23,
        "name3" : true,
        "name4" :
        {
          "name4-1" : false,
          "name4-2" : "string again",
          "name4-3" : [true, false, "string", { "name4-3-1" : "another string" }]
        },
        "name5" : [ { "name5-1" : [123, 456, true, false, [ { "foo" : "bar" } ], "str"] } ]
      }
    """);

    def stringProp(path : String) : String = jlon.get(path)
    def intProp(path : String) : Int = jlon.get(path)
    def booleanProp(path : String) : Boolean = jlon.get(path)
    def listProp(path : String) : List[Any] = jlon.get(path)
    def mapProp(path : String) : Map[String, Any] = jlon.get(path)
    def JObjectProp(path : String) : JObject = jlon.get(path).entry.get.asInstanceOf[JObject]
    def JArrayProp(path : String) : JArray = jlon.get(path).entry.get.asInstanceOf[JArray]

    assertResult("string value")(stringProp("name1"))
    assertResult(23)(intProp("name2"))
    assertResult(true)(booleanProp("name3"))

    // invalid types
    assertThrows[Exception](stringProp("name2"))
    assertThrows[Exception](intProp("name3"))
    assertThrows[Exception](booleanProp("name1"))

    // list
    assertResult(List.empty[Any].appended(true).appended(false).appended("string").appended(jlon.get("name4.name4-3.3").entry.get))(listProp("name4.name4-3"))

    // map
    assertResult(Map("name4-1" -> false, "name4-2" -> "string again", "name4-3" -> jlon.get("name4.name4-3").entry.get))(mapProp("name4"))

    // JObject
    assertResult("string again")(JObjectProp("name4").getItem("name4-2").get.getValue)

    // JArray
    assertResult(456)(JArrayProp("name5.0.name5-1").getItem(1).get.getValue)
    assertResult(123)(JArrayProp("name5.0.name5-1").getItem(0).get.getValue)
  }
}
