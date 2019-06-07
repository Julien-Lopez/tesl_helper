package card

import util.TeslException

sealed trait Attribute extends Serializable {
  override def toString: String = this.getClass.getSimpleName.replace("$", "")
}

object Attribute {
  def fromString(s: String): Attribute = {
    s.toLowerCase match {
      case "strength" => Strength
      case "intelligence" => Intelligence
      case "willpower" => Willpower
      case "agility" => Agility
      case "wisdom" => Wisdom
      case _ => throw TeslException("Unknown attribute: " + s)
    }
  }
}

case object Strength extends Attribute

case object Intelligence extends Attribute

case object Willpower extends Attribute

case object Agility extends Attribute

case object Wisdom extends Attribute

case object Neutral extends Attribute
