package scanners

import helpers.PrintHelper
import iterators.IndexedIterator
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable.ListBuffer

class ListScannerTest extends AnyFunSuite {

  test("scan array brackets skip") {

    val dt : LazyList[Char] = LazyList.empty :++ """ Q } [  A } B [ dd, { a { xx }, {} ff [ " [[][, ][x{}o, \" []b " ss ] XX, } } ] g, } b, { ]  e ,    """
    val it : IndexedIterator[Char] = new IndexedIterator[Char](dt)
    val buff : ListBuffer[Char] = new ListBuffer[Char]

    // po uzavreni vsech zavorek [] se ocekava konecna ] (ukonceni pole) nebo , (dalsi prvek pole)

    assertResult(true)(ListScanner.readInnerContentToBufferTill(it, List(']', ','), buff))
    assertResult(PrintHelper.lazyListToString(dt).trim)(PrintHelper.listBufferToString(buff).trim)
  }

  test("scan object brackets skip") {

    val dt : LazyList[Char] = LazyList.empty :++ """ a { b [ ] [ [ { e [ x " {{}} } ][ e a \" esc \" [ { [ { ] " } [ e e { } , } eee , """
    val it : IndexedIterator[Char] = new IndexedIterator[Char](dt)
    val buff : ListBuffer[Char] = new ListBuffer[Char]

    // po uzavreni vsech zavorek {} se ocekava konecna } (ukonceni pole) nebo , (dalsi prvek pole)

    assertResult(true)(ListScanner.readInnerContentToBufferTill(it, List('}', ','), buff))
    assertResult(PrintHelper.lazyListToString(dt).trim)(PrintHelper.listBufferToString(buff).trim)
  }
}