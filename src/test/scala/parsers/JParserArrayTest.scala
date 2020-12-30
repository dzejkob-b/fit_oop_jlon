package parsers

import entries.containers.JArray
import exceptions.JParseException
import org.scalatest.funsuite.AnyFunSuite

class JParserArrayTest extends AnyFunSuite {

  test("array parse 1") {

    // array parser predpoklada, ze uz je uvnitr zavorky

    assertResult("1,2,3,4")(JDataMock.create("""
        1, 2, 3, 4]
    """, (c : JDataMock) => new JParserArray(c))
      .parseTestSelf().getTestStreams.map(c => c.trim).mkString(","))

    assertResult(""""string",{ object },56,[subarray, {obj}, 5]""")(JDataMock.create("""
        "string"   , { object },  56  ,   [subarray, {obj}, 5] ]
    """, (c : JDataMock) => new JParserArray(c))
      .parseTestSelf().getTestStreams.map(c => c.trim).mkString(","))

    // indexy properties

    assertResult("0,1,2,3,4,5")(JDataMock.create("""
        10, 45, 3, 75, 20, 6]
    """, (c : JDataMock) => new JParserArray(c))
      .parseTest().asInstanceOf[JArray].getProperties.mkString(","))
  }

  test("array parse blank") {

    assertResult("")(JDataMock.create("""
        ]
    """, (c : JDataMock) => new JParserArray(c))
      .parseTest().asInstanceOf[JArray].getProperties.mkString(","))
  }

  test("array parse error 1") {

    assertThrows[JParseException](JDataMock.create("""
        1, 2, 3, 4
    """, (c : JDataMock) => new JParserArray(c))
      .parseTestSelf())

    assertThrows[JParseException](JDataMock.create("""
        neco neco, [ zavorka { zavorka [ neco ]
    """, (c : JDataMock) => new JParserArray(c))
      .parseTestSelf())
  }
}