import entries.JEntry
import entries.containers.JObject
import entries.streamContainers.JData
import exceptions.JParseException
import org.scalatest.funsuite.AnyFunSuite

class JLON_lazyTest extends AnyFunSuite {

  test("before lazy load test") {
    val jlon : JLON = JLON("""
      {
        "neco" : "blbost",
        "kravina" : { "ref" : "kocka" },
        "hodnota" : "cosi",
        "myprop" : {
          "prop_a" : "hodnota",
          "prop_b" : { "ref" : "pes" },
          "prop_c" : "windows",
          "prop_d" : 456
        },
        "hah" : "televize",
        "pole" : [1, 2, 3, [20, 30, 40], 5, 6],
        "kecup" : 456
      }
    """);

    assertResult(Some("cosi"))(jlon.get("hodnota").any)
    assertResult(Some(456))(jlon.get("kecup").any)
    assertResult(None)(jlon.get("neexistuje").any)

    // celkovy pocet entries je 23
    assertResult(23)(jlon.get().getWholeSubtree.size);
  }

  test("lazy load test") {
    val jlon : LazyJLON = LazyJLON("""
      {
        "neco" : "blbost",
        "kravina" : { "ref" : "kocka" },
        "hodnota" : "cosi",
        "myprop" : {
          "prop_a" : "hodnota",
          "prop_b" : { "ref" : "pes" },
          "prop_c" : "windows",
          "prop_d" : 456
        },
        "hah" : "televize",
        "pole" : [1, 2, 3, [20, 30, 40], 5, 6],
        "kecup" : 456
      }
    """);

    assertResult(Some("cosi"))(jlon.get("hodnota").any)

    // kontejner neni plne rozparsovan
    assertResult(false)(jlon.get().dataEntry.get.isParsed)

    // 1x plne nerozparsovany toplevel kontejner, 1x nerozparsovane "blbost", 1x nerozparsovane "{ "ref" : "kocka" }"
    assertResult(3)(jlon.get().getWholeSubtree.count((e: JEntry) => e.isInstanceOf[JData]));

    // vic properties nacteno neni
    assertResult("neco,kravina,hodnota")(jlon.get().dataEntry.get.getEntry.asInstanceOf[JObject].getProperties.mkString(","))

    assertResult(Some(456))(jlon.get("kecup").any)

    // pribyly nerozparsovane obsahy "myprop", "hah", "pole"
    assertResult(6)(jlon.get().dataEntry.get.getWholeSubtree.count((e: JEntry) => e.isInstanceOf[JData]));

    // celkovy pocet entries je 8 nebot rozparsovane jsou obsahy "hodnota" a "kecup"
    assertResult(8)(jlon.get().dataEntry.get.getWholeSubtree.size);

    assertResult(None)(jlon.get("neexistuje").any)

    // zde jiz doslo k plnemu rozparsovani kontejneru
    assertResult(true)(jlon.get().dataEntry.get.isParsed)

    // celkovy pocet entries je ale stale 8
    assertResult(8)(jlon.get().dataEntry.get.getWholeSubtree.size);
  }

  test("lazy load with errors 1") {
    val jlon : LazyJLON = LazyJLON("""
      {
        "neco" : "blbost",
        "kravina" : { "ref" : "kocka" },
        "hodnota" : "cosi",
        "myprop" : {
          "prop_a" : "hodnota",
          "prop_b" : chyba { "ref" : "pes" chyba } bla bla,
          "prop_c" : "windows" spatne,
          "prop_d" : 456CHYBA
        },
        "hah" : "televize",
        "pole" : [1, 2, 3, [20, 30, 40], 5, 6],
        "kecup" : 456
      }
    """);

    assertResult(Some(20))(jlon.get("pole.3.0").any)
    assertResult(Some(456))(jlon.get("kecup").any)

    // tohle neskonci chybou
    jlon.get("myprop")

    // tohle uz ano
    assertThrows[JParseException](jlon.get("myprop.prop_b.ref"))
  }

  test("lazy load with errors 2") {
    val jlon : LazyJLON = LazyJLON("""
      {
        "neco" : "blbost",
        "kravina" : { "ref"    :     "kocka" },
        "hodnota" : "cosi",
        xk:R%LxU}a*b2VbKfbzL;YYtpYYm,Z
        LkSx-Uy,Z{Jy%,P()CZ8mM%yL/HcFU
        m-ua#N#-]8g]$ZKQajMr87bNZBFE.m
        V6f5p@x:4H&wQK_gS3vhPK]b{gUP9j
        @j*ZmDTaN]twh{Vy3na,!gf]z]SXXS
        yb}LM,b{wn{rwKtzk,7iawM.#nuP4j
        F/8d;xT6={3jT?,t[mzQCZ(RZ=_dS7
        33BRfjWwDbD#E%fWn9JqxJCt:![2cz
        [(p=(F28[DxcCEK2/ur_rH;rM$=a{8
        bmHt$2[;B}p&r(M_}6c5U*-AM!,Vjh
    """);

    assertResult(Some("blbost"))(jlon.get("neco").any)
    // vnitrek " \"kocka\" " je nerozparsovany
    assertResult(""" { "ref"    :     "kocka" }""")(jlon.get("kravina").toJLON)

    assertResult(Some("cosi"))(jlon.get("hodnota").any)
    assertThrows[JParseException](jlon.get("neexistuje"))
  }

  test("sample JLON code lazy") {
    val jlon : LazyJLON = LazyJLON("""
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

  test("sample JLON code lazy details") {
    val jlon : LazyJLON = LazyJLON("""
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

    assertResult(Some("string value"))(jlon.get("name1").any)

    // nacteny pouze dve entries - kontejner a "string value"
    assertResult(2)(jlon.get().dataEntry.get.getWholeSubtree.size)

    // a ten kontejner ma pouze jedno property
    assertResult("name1")(jlon.get().getEntry.asInstanceOf[JObject].getProperties.mkString(","))

    jlon.get("name5")

    // ted uz jsou vsechny properties v kontejneru
    assertResult("name1,name2,name3,name4,name5")(jlon.get().getEntry.asInstanceOf[JObject].getProperties.mkString(","))

    // a entries je 6 - kontejner, nerozparsovane name1-5
    assertResult(6)(jlon.get().dataEntry.get.getWholeSubtree.size)

    // rozparsovane etries je jen jedna - string "string value"
    assertResult(1)(jlon.get().dataEntry.get.getWholeSubtree.count((e: JEntry) => !e.isInstanceOf[JData]))

    jlon.get("name5.0")

    // rozparsovane etries jsou jiz dve - string "string value" a jednoprvkove pole s nerozparsovanym vnitrkem
    assertResult(2)(jlon.get().dataEntry.get.getWholeSubtree.count((e: JEntry) => !e.isInstanceOf[JData]))
  }

}