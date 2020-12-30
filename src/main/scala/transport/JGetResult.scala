package transport

import entries.containers.{JArray, JObject}
import entries.streamContainers.JData
import entries.{JEntry, JPrimBoolean, JPrimNumber, JPrimString}

import scala.collection.mutable.ArrayBuffer

/**
 * Prihradka pro návratovou hodnotu JLON.get
 * @param entry Ref JEntry obsahujici hodnotu
 */
class JGetResult(val entry : Option[JEntry], val dataEntry : Option[JData], val beforeDataEntry : Option[JData], val restPath : ArrayBuffer[String]) {

  def any : Option[Any] =
    JGetResult.toOptAny(this)

}

/**
 * Implicitní koverze JGetResult do potřebných typů
 */
object JGetResult {
  implicit def toJEntry(i : JGetResult) : JEntry = {
    if (i.entry.isDefined) {
      i.entry.get

    } else {
      throw new Exception("Entry is not defined!")
    }
  }

  /*
  implicit def toJObject(i : JGetResult) : JObject = {
    if (i.entry.isDefined && i.entry.get.isInstanceOf[JObject]) {
      i.entry.get.asInstanceOf[JObject]

    } else {
      throw new Exception("Entry is not defined!")
    }
  }

  implicit def toJArray(i : JGetResult) : JArray = {
    if (i.entry.isDefined && i.entry.get.isInstanceOf[JArray]) {
      i.entry.get.asInstanceOf[JArray]

    } else {
      throw new Exception("Entry is not defined!")
    }
  }
  */

  implicit def toOptAny(i : JGetResult) : Option[Any] = {
    if (i.entry.isDefined) {
      Some(i.entry.get.getValue)

    } else {
      None
    }
  }

  implicit def toString(i : JGetResult): String = {
    if (i.entry.isDefined && i.entry.get.isInstanceOf[JPrimString]) {
      i.entry.get.getValue.asInstanceOf[String]

    } else if (i.entry.isDefined) {
      throw new Exception("Entry is not string!")

    } else {
      throw new Exception("Entry is not defined!")
    }
  }

  implicit def toInt(i : JGetResult): Int = {
    if (i.entry.isDefined && i.entry.get.isInstanceOf[JPrimNumber]) {
      i.entry.get.getValue.asInstanceOf[Int]

    } else if (i.entry.isDefined) {
      throw new Exception("Entry is not int!")

    } else {
      throw new Exception("Entry is not defined!")
    }
  }

  implicit def toBoolean(i : JGetResult): Boolean = {
    if (i.entry.isDefined && i.entry.get.isInstanceOf[JPrimBoolean]) {
      i.entry.get.getValue.asInstanceOf[Boolean]

    } else if (i.entry.isDefined) {
      throw new Exception("Entry is not boolean!")

    } else {
      throw new Exception("Entry is not defined!")
    }
  }

  implicit def toList(i : JGetResult): List[Any] = {
    if (i.entry.isDefined && i.dataEntry.get.parseWhole().isInstanceOf[JArray]) {
      i.dataEntry.get.getEntry.asInstanceOf[JArray].toList()

    } else if (i.entry.isDefined) {
      throw new Exception("Entry is not array!")

    } else {
      throw new Exception("Entry is not defined!")
    }
  }

  implicit def toMap(i : JGetResult) : Map[String, Any] = {
    if (i.entry.isDefined && i.dataEntry.get.parseWhole().isInstanceOf[JObject]) {
      i.dataEntry.get.getEntry.asInstanceOf[JObject].toMap()

    } else if (i.entry.isDefined) {
      throw new Exception("Entry is not object!")

    } else {
      throw new Exception("Entry is not defined!")
    }
  }
}