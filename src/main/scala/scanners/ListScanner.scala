package scanners

import iterators.IndexedIterator

import scala.collection.mutable.ListBuffer

object ListScanner {

  def isWhitespacesOnly(it : IndexedIterator[Char]) : Boolean =
    it.forall((ch: Char) => ch.isWhitespace)

  def readWhitespacesTill(it : IndexedIterator[Char], toFind : Char) : Boolean =
    this.readWhitespacesTill(it, List.empty[Char].appended(toFind), null)

  def readWhitespacesTill(it : IndexedIterator[Char], toFind : List[Char]) : Boolean =
    this.readWhitespacesTill(it, toFind, null)

  /**
   * Preskoci nebo nacte prazdne znaky dokud nenarazi na pozadovane znaky.
   * Pokud najde jine, nez pozadovane nebo whitespaces, vraci false
   * @param it iterator Charu
   * @param toFind pozadovane znaky
   * @param dt vystupni buffer (muze byt null)
   * @return
   */
  def readWhitespacesTill(it : IndexedIterator[Char], toFind : List[Char], dt : ListBuffer[Char]) : Boolean = {
    while (it.nonEmpty) {
      val ch : Char = it.next()

      if (dt != null) {
        dt += ch
      }

      if (ch.isWhitespace) {
        // skip ...
      } else if (toFind.contains(ch)) {
        return true
      } else {
        return false
      }
    }

    false
  }

  /**
   * Nacte znaky takovym zpusobem, ze mohou obsahovat vnitrni JLON, dokud se nenarazi na pozadovane znaky.
   * Pokud pozadovane znaky nenajde vraci false.
   * Pokud nalezne kritickou strukturalni chybu JLON (zavorky a string) vyhodi vyjmku.
   * @param it iterator Charu
   * @param toFind pozadovane znaky
   * @param dt vystupni buffer
   * @return
   */
  def readInnerContentToBufferTill(it : IndexedIterator[Char], toFind : List[Char], dt : ListBuffer[Char]) : Boolean = {
    var inString : Boolean = false
    var brLevel : Map[Char, Int] = Map('{' -> 0, '[' -> 0)

    while (it.nonEmpty) {
      val ch : Char = it.next()

      dt += ch

      if (inString) {

        if (ch == '\\') {
          if (it.hasNext) {
            dt += it.next()

          } else {
            throw new Exception("Syntax error!")
          }

        } else if (ch.equals('"')) {
          inString = false
        }

      } else if (ch.equals('"')) {
        inString = true;

      } else if (ch.equals('{')) {

        if (brLevel('[') == 0) {
          brLevel += (ch -> (brLevel(ch) + 1))
        }

      } else if (ch.equals('[')) {

        if (brLevel('{') == 0) {
          brLevel += (ch -> (brLevel(ch) + 1))
        }

      } else if (brLevel('{') > 0 || brLevel('[') > 0) {

        val openCh = if (brLevel('{') > 0) '{' else '['
        val closeCh = if (brLevel('{') > 0) '}' else ']'

        if (ch.equals(openCh)) {
          brLevel += (openCh -> (brLevel(openCh) + 1))

        } else if (ch.equals(closeCh)) {
          brLevel += (openCh -> (brLevel(openCh) - 1))

          if (brLevel(openCh) < 0) {
            throw new Exception("Syntax error!")
          }
        }

      } else if (toFind.contains(ch) && brLevel('{') == 0 && brLevel('[') == 0) {
        // pokud je nalezeny znak a nejsme uvnitr zavorek
        return true;

      } else {
        // jine znaky povoleny
      }
    }

    false
  }

  def readRestToString(it : IndexedIterator[Char]): String = {
    val sb : StringBuilder = new StringBuilder

    while (it.hasNext) {
      sb.append(it.next())
    }

     sb.toString()
  }
}
