package parsers

import entries.JPrimString
import exceptions.JParseException
import org.scalatest.funsuite.AnyFunSuite

class JParserStringTest extends AnyFunSuite {

  test("string parse 1") {

    // string parser predpoklada, ze uz je uvnitr uvozovek !

    assertResult(" nejaky text ")(
      JDataMock.create(" nejaky text \"  ", (c : JDataMock) => new JParserString(c))
        .parseTest().asInstanceOf[JPrimString].getValue
    )

    assertResult("123456")(
      JDataMock.create("123456\"  ", (c : JDataMock) => new JParserString(c))
        .parseTest().asInstanceOf[JPrimString].getValue
    )

    assertResult("""some " escaping \ etc""")(
      JDataMock.create("""some \" escaping \\ etc"""", (c : JDataMock) => new JParserString(c))
        .parseTest().asInstanceOf[JPrimString].getValue
    )

    assertResult("""some " {{{ ] { ]escaping \  ]] {{ and brackets ]]] { }""")(
      JDataMock.create("""some \" {{{ ] { ]escaping \\  ]] {{ and brackets ]]] { }"""", (c : JDataMock) => new JParserString(c))
        .parseTest().asInstanceOf[JPrimString].getValue
    )
  }

  test("string parse error 1") {

    assertThrows[JParseException](
      JDataMock.create("string\" bla bla? ", (c : JDataMock) => new JParserString(c))
        .parseTest().asInstanceOf[JPrimString].getValue
    )

    assertThrows[JParseException](
      JDataMock.create("nebudou uvozovky? ", (c : JDataMock) => new JParserString(c))
        .parseTest().asInstanceOf[JPrimString].getValue
    )
  }
}