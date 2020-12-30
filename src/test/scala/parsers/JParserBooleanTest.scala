package parsers

import entries.JPrimBoolean
import exceptions.JParseException
import org.scalatest.funsuite.AnyFunSuite

class JParserBooleanTest extends AnyFunSuite {

  test("boolean parse 1") {

    assertResult(true)(
      JDataMock.create("true", (c : JDataMock) => new JParserBoolean(c))
        .parseTest().asInstanceOf[JPrimBoolean].getValue
    )

    assertResult(false)(
      JDataMock.create("false", (c : JDataMock) => new JParserBoolean(c))
        .parseTest().asInstanceOf[JPrimBoolean].getValue
    )
  }

  test("boolean parse 2") {
    assertResult(true)(
      JDataMock.create("    true        ", (c : JDataMock) => new JParserBoolean(c))
        .parseTest().asInstanceOf[JPrimBoolean].getValue
    )

    assertResult(false)(
        JDataMock.create("        false        ", (c : JDataMock) => new JParserBoolean(c))
        .parseTest().asInstanceOf[JPrimBoolean].getValue
    )
  }

  test("boolean parse errors") {

    assertThrows[JParseException](
      JDataMock.create(" true false ", (c : JDataMock) => new JParserBoolean(c))
        .parseTest().asInstanceOf[JPrimBoolean].getValue
    )

    assertThrows[JParseException](
      JDataMock.create(" lorem ipsum ", (c : JDataMock) => new JParserBoolean(c))
        .parseTest().asInstanceOf[JPrimBoolean].getValue
    )
  }
}