import org.scalatest.funsuite.AnyFunSuite

class JLON_formatTest extends AnyFunSuite {

  test("format test 1") {
    val jlon : JLON = JLON("""{     "foo"   :   [   1 ,    2 """ + """  ,  3    ]    }""")

    assertResult("""{"foo":[1,2,3]}""")(jlon.toJLON)
  }

  test("format test 2") {
    val jlon : JLON = JLON("""
      {
        "neco" : "blbost",
        "kravina" : { "ref" : "kocka" },
        "hah" : "televize",
        "kecup" : 456,
        "pole" : [1, 2, 3, [20, 30, 40], 5, 6],
        "myprop" : {
          "prop_a" : "hodnota",
          "prop_b" : { "ref" : "pes" },
          "prop_c" : "windows",
          "prop_d" : 456
        }
      }
    """);

    assertResult("""{"neco":"blbost","kravina":{"ref":"kocka"},"hah":"televize","kecup":456,"pole":[1,2,3,[20,30,40],5,6],"myprop":{"prop_a":"hodnota","prop_b":{"ref":"pes"},"prop_c":"windows","prop_d":456}}""")(jlon.toJLON)
  }

  test("to JLON pretty test") {
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

    val cmp : String = """{
                         |	"name1" : "string value",
                         |	"name2" : 23,
                         |	"name3" : true,
                         |	"name4" : {
                         |		"name4-1" : false,
                         |		"name4-2" : "string again",
                         |		"name4-3" : [
                         |			true,
                         |			false,
                         |			"string",
                         |			{
                         |				"name4-3-1" : "another string"
                         |			}
                         |		]
                         |	},
                         |	"name5" : [
                         |		{
                         |			"name5-1" : [
                         |				123,
                         |				456,
                         |				true,
                         |				false,
                         |				[
                         |					{
                         |						"foo" : "bar"
                         |					}
                         |				],
                         |				"str"
                         |			]
                         |		}
                         |	]
                         |}""".stripMargin

    assertResult(cmp)(jlon.toJLONpretty)
  }
}