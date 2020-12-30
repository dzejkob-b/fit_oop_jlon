import org.scalatest.funsuite.AnyFunSuite

class JLON_getTest extends AnyFunSuite {

  test("get entries test") {
    val jlon : JLON = JLON("""
      {
        "neco" : "blbost",
        "kravina" : { "ref" : "kocka" },
        "hah" : "televize",
        "kecup" : 456,
        "pole" : [1, 2, 3, [20, 30, 40], 5, 6],
        "cisla" : [5, 8, 9, 6],
        "myprop" : {
          "prop_a" : "hodnota",
          "prop_b" : { "ref" : "pes" },
          "prop_c" : "windows",
          "prop_d" : 456
        }
      }
    """);

    assertResult(Some("blbost"))(jlon.get("neco").any)
    assertResult("""{"ref":"kocka"}""")(jlon.get("kravina").toJLON)
    assertResult("""[20,30,40]""")(jlon.get("pole.3").toJLON)
    assertResult(Some("hodnota"))(jlon.get("myprop.prop_a").any)
    assertResult("""{"ref":"pes"}""")(jlon.get("myprop.prop_b").toJLON)
    assertResult(Some("windows"))(jlon.get("myprop.prop_c").any)
    assertResult(Some(456))(jlon.get("myprop.prop_d").any)

    def listProp(path : String) : List[Any] = jlon.get(path)
    def mapProp(path : String) : Map[String, Any] = jlon.get(path)

    // konverze do listu
    assertResult(List(5, 8, 9, 6))(listProp("cisla"))
    assertResult(List(1, 2, 3, jlon.get("pole.3").any.get, 5, 6))(listProp("pole"))

    // konverze do mapy
    assertResult(Map("prop_a" -> "hodnota", "prop_b" -> jlon.get("myprop.prop_b").any.get, "prop_c" -> "windows", "prop_d" -> 456))(mapProp("myprop"))
  }

  test("sample JLON code") {
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

    assertResult(None)(jlon.get("name5.name5-1").any)
    assertResult(Some(456))(jlon.get("name5.0.name5-1.1").any)
    assertResult(Some(false))(jlon.get("name5.0.name5-1.3").any)
    assertResult(Some("bar"))(jlon.get("name5.0.name5-1.4.0.foo").any)
    assertResult(Some("str"))(jlon.get("name5.0.name5-1.5").any)
    assertResult(Some("string"))(jlon.get("name4.name4-3.2").any)
    assertResult(Some("another string"))(jlon.get("name4.name4-3.3.name4-3-1").any)
  }

  test("sample JLON toJLON and reparse") {
    val origJlon : JLON = JLON("""
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

    val jlon : JLON = JLON(origJlon.toJLON)

    assertResult(None)(jlon.get("name5.name5-1").any)
    assertResult(Some(456))(jlon.get("name5.0.name5-1.1").any)
    assertResult(Some(false))(jlon.get("name5.0.name5-1.3").any)
    assertResult(Some("bar"))(jlon.get("name5.0.name5-1.4.0.foo").any)
    assertResult(Some("str"))(jlon.get("name5.0.name5-1.5").any)
    assertResult(Some("string"))(jlon.get("name4.name4-3.2").any)
    assertResult(Some("another string"))(jlon.get("name4.name4-3.3.name4-3-1").any)
  }

}