package helpers

import scala.collection.mutable.ArrayBuffer

object PathHelper {

  def parsePath(path : String) : ArrayBuffer[String] = {

    val result : ArrayBuffer[String] = new ArrayBuffer[String]()

    if (path.isEmpty) {
      return result
    }

    val it = (path + ".").iterator
    var chunk : String = ""

    while (it.hasNext) {
      var ch : Char = it.next()

      if (ch.equals('\\')) {
        if (it.hasNext) {
          chunk += it.next()

        } else {
          throw new Exception("Path parse error")
        }

      } else if (ch.equals('.')) {

        if (chunk.isEmpty || chunk.trim.isEmpty) {
          throw new Exception("Blank path item")

        } else {
          result.addOne(chunk)
          chunk = ""
        }

      } else {
        chunk += ch
      }
    }

    result
  }

}
