import org.scalatest.funsuite.AnyFunSuite

class JLON_setTest extends AnyFunSuite {

  test("set entries test") {
    val jlon : JLON = JLON("""
      {
        "name1" : "string value",
        "name2" : 23,
        "name3" : true
      }
    """);

    jlon.set("name1", 1)
    jlon.set("name2", 2)
    jlon.set("name3", 3)
    jlon.set("name4.neco.dalsiho", 4)
    jlon.set("sub", LazyList.empty :++ "[1, 2, 3, 4]")
    jlon.set("a.b.c.d", LazyList.empty :++ "[1, 2, 3, 4]")

    assertResult("""{"name1":1,"name2":2,"name3":3,"name4":{"neco":{"dalsiho":4}},"sub":[1,2,3,4],"a":{"b":{"c":{"d":[1,2,3,4]}}}}""")(jlon.toJLON)

    assertResult(Some(1))(jlon.get("name1").any)
    assertResult(Some(2))(jlon.get("name2").any)
    assertResult(Some(4))(jlon.get("name4.neco.dalsiho").any)
    assertResult(Some(3))(jlon.get("a.b.c.d.2").any)

  }

  test("set array test") {
    val jlon : JLON = JLON("""
      {
        "name1" : "string value",
        "name2" : 23,
        "name3" : true,
        "name4" : [4, 6, 20, "str", false]
      }
    """);

    jlon.set("name4.1", 80)
    jlon.set("name4.0", "str")
    jlon.set("name4.5", "nove")
    jlon.set("name4.-1", true)
    jlon.set("name4.-1", false)

    assertResult("""{"name1":"string value","name2":23,"name3":true,"name4":["str",80,20,"str",false,"nove",true,false]}""")(jlon.toJLON)

    assertResult(Some(80))(jlon.get("name4.1").any)
    assertResult(Some("str"))(jlon.get("name4.0").any)
    assertResult(Some("nove"))(jlon.get("name4.5").any)
    assertResult(Some(true))(jlon.get("name4.6").any)
    assertResult(Some(false))(jlon.get("name4.7").any)

    // invalid array index
    assertThrows[Exception](jlon.set("name4.650", 20))
  }

  test("set to root test") {
    val jlon : JLON = JLON("45")

    assertResult(Some(45))(jlon.get().any)

    jlon.set(90)

    assertResult(Some(90))(jlon.get().any)

    jlon.set(true)

    assertResult(Some(true))(jlon.get().any)

    jlon.set("nejaky string")

    assertResult(Some("nejaky string"))(jlon.get().any)

    // cannot set property to primitive value (which is root)
    assertThrows[Exception](jlon.set("prop", 5))

    // set root to some object
    jlon.set(LazyList.empty :++ "{ \"a\" : 5 }")

    // and now its possible
    jlon.set("b", 6)

    assertResult(Some(6))(jlon.get("b").any)
  }

  test("set to blank root test") {
    val jlon : JLON = JLON("45")

    assertResult(Some(45))(jlon.get().any)

    jlon.set(LazyList.empty :++ "{}");

    assertResult("{}")(jlon.get().toJLON)
  }

  test("set array as arg test") {
    val jlon : JLON = JLON("0")

    jlon.set(List.empty[Any].appended(45).appended("string").appended(true).appended(false).appended(30).appended("neco"))

    assertResult("""[45,"string",true,false,30,"neco"]""")(jlon.get().toJLON)
  }

  test("set object as arg test") {
    val jlon : JLON = JLON("0")

    jlon.set(Map("prop_a" -> 5, "prop_b" -> 8, "prop_c" -> "str", "prop_d" -> true))

    assertResult("""{"prop_a":5,"prop_b":8,"prop_c":"str","prop_d":true}""")(jlon.get().toJLON)
  }
}
