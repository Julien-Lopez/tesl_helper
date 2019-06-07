package card

import util.TeslException

sealed trait Keyword extends Serializable {
  override def toString: String = this.getClass.getSimpleName.replace("$", "")
}

object Keyword {
  def fromString(s: String): Keyword = {
    val rally = """rally\(([1-9][0-9]*)\)""".r
    s.toLowerCase match {
      case "breakthrough" => Breakthrough
      case "charge" => Charge
      case "drain" => Drain
      case "guard" => Guard
      case "lethal" => Lethal
      case "regenerate" => Regenerate
      case rally(n) => Rally(n.toInt)
      case "ward" => Ward
      case _ => throw TeslException("Unknown keyboard: " + s)
    }
  }
}

case object Breakthrough extends Keyword

case object Charge extends Keyword

case object Drain extends Keyword

case object Guard extends Keyword

case object Lethal extends Keyword

case object Regenerate extends Keyword

case class Rally(number: Int) extends Keyword

case object Ward extends Keyword
