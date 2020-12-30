import entries.containers.{JArray, JObject}
import org.scalatest.funsuite.AnyFunSuite

class JLON_rootsTest extends AnyFunSuite {

  test("root as array") {
    val jlon : JLON = JLON("""[45, true, "neco", false]""")

    assertResult(Some(45))(jlon.get("0").any)
    assertResult(Some(true))(jlon.get("1").any)
    assertResult(Some("neco"))(jlon.get("2").any)
    assertResult(Some(false))(jlon.get("3").any)
  }

  test("root as primitive") {
    var jlon : JLON = JLON("45")

    assertResult(Some(45))(jlon.get().any)

    jlon = JLON("true")

    assertResult(Some(true))(jlon.get().any)

    jlon = JLON(""""neco"""")

    assertResult(Some("neco"))(jlon.get().any)
  }

  test("root as primitive lazy") {
    var jlon : LazyJLON = LazyJLON("45")

    assertResult(Some(45))(jlon.get().any)

    jlon = LazyJLON("true")

    assertResult(Some(true))(jlon.get().any)

    jlon = LazyJLON(""""neco"""")

    assertResult(Some("neco"))(jlon.get().any)
  }

  test("root as blank object") {
    val jlon : LazyJLON = LazyJLON("{}");

    assertResult("{}")(jlon.get().toJLON)
    assertResult(true)(jlon.get().entry.get.isInstanceOf[JObject])
  }

  test("root as blank array") {
    val jlon : LazyJLON = LazyJLON("[]");

    assertResult("[]")(jlon.get().toJLON)
    assertResult(true)(jlon.get().entry.get.isInstanceOf[JArray])
  }
}
