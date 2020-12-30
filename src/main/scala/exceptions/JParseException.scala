package exceptions

import iterators.IndexedIterator

final case class JParseException(val message : String, val it : IndexedIterator[Char], val cause : Throwable = None.orNull) extends Exception(message, cause) {

  override def getMessage: String = message + ", parse context: `" + (new String(it.getArrayTo(it.getSavedIndex + 1))) + "`"

}
