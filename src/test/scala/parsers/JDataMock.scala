package parsers

import entries.JEntry
import entries.streamContainers.JData
import helpers.PrintHelper

import scala.collection.mutable.ArrayBuffer

class JDataMock(data : Option[LazyList[Char]]) extends JData(data) {

  private val _testStreams : ArrayBuffer[String] = new ArrayBuffer[String]();

  override def onInit(): Unit = {
    // nic
  }

  override def createDataObject(data : Option[LazyList[Char]]) : JData = {
    this._testStreams.addOne(PrintHelper.lazyListToString(data.get))
    new JDataMock(data)
  }

  def getTestStreams : List[String] =
    this._testStreams.toList

  def setParser(parser : JParser) : Unit =
    this._parser = parser

  def parseTest() : JEntry = {
    this._entry = this._parser.parseStart()

    while (this._parser.parseStep()) {
      // loop ...
    }

    this._parser = null
    this._isParsed = true

    this._entry
  }

  def parseTestSelf() : JDataMock = {
    this.parseTest()
    this
  }
}

object JDataMock {
  def create(input : String): JDataMock =
    new JDataMock(Some(LazyList.empty :++ input));

  def create(input : String, callback : (JDataMock) => JParser): JDataMock = {
    val data = JDataMock.create(input)

    data.setParser(callback(data))
    data
  }
}