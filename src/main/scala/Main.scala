
object Main extends App {
  println("== JLON library ==")

  var jlon : JLON = LazyJLON("""
      {
        "name1" : {
          "a" : "string value",
          "b" : 620
        },
        "name2" : 23,
        "name3" : true,
        "name4" : [1, 2, 3, 4, 5]
      }
    """);

  jlon.get("name2").any

  // unparsed je "name1" a zbytek od "name3"
  println(jlon.toStruct)
}
