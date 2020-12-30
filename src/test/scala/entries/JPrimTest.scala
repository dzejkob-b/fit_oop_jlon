package entries

import org.scalatest.funsuite.AnyFunSuite

class JPrimTest extends AnyFunSuite {

  test("boolean test") {

    val prim : JPrimBoolean = new JPrimBoolean(true)

    assertResult(true)(prim.getValue)
    assertResult("true")(prim.toJLON)
    assertResult(false)(prim.isQueryable)
    assertResult(true)(prim.getAllSubchilds.isEmpty)
  }

  test("number test") {

    val prim : JPrimNumber = new JPrimNumber(45)

    assertResult(45)(prim.getValue)
    assertResult("45")(prim.toJLON)
    assertResult(false)(prim.isQueryable)
    assertResult(true)(prim.getAllSubchilds.isEmpty)
  }

  test("negative number test") {

    val prim : JPrimNumber = new JPrimNumber(-756)

    assertResult(-756)(prim.getValue)
    assertResult("-756")(prim.toJLON)
  }

  test("string test 1") {

    val prim : JPrimString = new JPrimString("nejaky text")

    assertResult("nejaky text")(prim.getValue)
    assertResult(""""nejaky text"""")(prim.toJLON)
    assertResult(false)(prim.isQueryable)
    assertResult(true)(prim.getAllSubchilds.isEmpty)
  }

  test("string test 2") {

    val prim : JPrimString = new JPrimString("""a [ { "ee }{ a" {[[ A e\ A \][E][ [e]{""")

    assertResult("""a [ { "ee }{ a" {[[ A e\ A \][E][ [e]{""")(prim.getValue)
    assertResult(""""a [ { \"ee }{ a\" {[[ A e\\ A \\][E][ [e]{"""")(prim.toJLON)
    assertResult(false)(prim.isQueryable)
    assertResult(true)(prim.getAllSubchilds.isEmpty)
  }
}