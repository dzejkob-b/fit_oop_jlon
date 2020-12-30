package parsers

import entries.containers.JObject
import exceptions.JParseException
import org.scalatest.funsuite.AnyFunSuite

class JParserObjectTest extends AnyFunSuite {

  test("object parse 1") {

    // object parser predpoklada, ze uz je uvnitr zavorky

    assertResult("""45,"str",[array],neco""")(JDataMock.create("""
            "prop_1" : 45,
            "prop_2" : "str",
            "prop_3" : [array],
            "prop_4" : neco
        }
    """, (c : JDataMock) => new JParserObject(c))
      .parseTestSelf().getTestStreams.map(c => c.trim).mkString(","))

    assertResult("""prop_1,prop_2,prop_3,prop_4""")(JDataMock.create("""
            "prop_1" : 45,
            "prop_2" : "str",
            "prop_3" : [array],
            "prop_4" : neco
        }
    """, (c : JDataMock) => new JParserObject(c))
      .parseTest().asInstanceOf[JObject].getProperties.mkString(","))
  }

  test("object parse blank") {

    assertResult("")(JDataMock.create("""
        }
    """, (c : JDataMock) => new JParserObject(c))
      .parseTest().asInstanceOf[JObject].getProperties.mkString(","))
  }

  test("object parse error 1") {

    assertThrows[JParseException](JDataMock.create(
      """
            "prop_1" : 45,
            prop_2" : "str",
            "prop_3 : [array],
            "prop_4" : neco
        }
    """, (c: JDataMock) => new JParserObject(c))
      .parseTestSelf())
  }

  test("object parse error 2") {

    assertThrows[JParseException](JDataMock.create("""
            "prop_1" : 45,
            "prop_2" : "str",
        }
    """, (c : JDataMock) => new JParserObject(c))
      .parseTestSelf())

    assertThrows[JParseException](JDataMock.create("""
            "prop_1" : { [] {},
            "prop_2" : "str"
        }
    """, (c : JDataMock) => new JParserObject(c))
      .parseTestSelf())
  }
}