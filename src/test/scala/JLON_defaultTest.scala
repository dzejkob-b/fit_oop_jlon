import org.scalatest.funsuite.AnyFunSuite

class JLON_defaultTest extends AnyFunSuite {

  test("it should just compile") {
    assertCompiles("JLON(\"\"\"{ \"foo\" : [1, 2 \"\"\" + \"\"\", 3] }\"\"\")")
  }

  test("it should just run") {
    val jlon : JLON = JLON("""{ "foo" : [1, 2 """ + """, 3] }""")
  }

  test("get should just return None and array values") {
    val jlon : JLON = JLON("""{ "foo" : [1, 2 """ + """, 3] }""")

    assertResult(None)(jlon.get("").any)
    assertResult(Some(1))(jlon.get("foo.0").any)
    assertResult(Some(2))(jlon.get("foo.1").any)
    assertResult(Some(3))(jlon.get("foo.2").any)
  }
}