package parsers

import entries.JPrimNumber
import exceptions.JParseException
import org.scalatest.funsuite.AnyFunSuite

class JParserNumberTest extends AnyFunSuite {

  test("number parse 1") {

    assertResult(45648)(
      JDataMock.create("45648", (c : JDataMock) => new JParserNumber(c))
        .parseTest().asInstanceOf[JPrimNumber].getValue
    )

    assertResult(-35)(
      JDataMock.create("     -35  ", (c : JDataMock) => new JParserNumber(c))
        .parseTest().asInstanceOf[JPrimNumber].getValue
    )
  }

  test("number error 1") {

    assertThrows[JParseException](
      JDataMock.create(" 45 68 ", (c : JDataMock) => new JParserNumber(c))
        .parseTest().asInstanceOf[JPrimNumber].getValue
    )

    assertThrows[JParseException](
      JDataMock.create("- 56", (c : JDataMock) => new JParserNumber(c))
        .parseTest().asInstanceOf[JPrimNumber].getValue
    )

    assertThrows[JParseException](
      JDataMock.create("cislo", (c : JDataMock) => new JParserNumber(c))
        .parseTest().asInstanceOf[JPrimNumber].getValue
    )

    assertThrows[JParseException](
      JDataMock.create(" 456a  ", (c : JDataMock) => new JParserNumber(c))
        .parseTest().asInstanceOf[JPrimNumber].getValue
    )
  }

}