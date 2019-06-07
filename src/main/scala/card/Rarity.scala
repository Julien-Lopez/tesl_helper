package card

import util.TeslException

sealed trait Rarity extends Serializable {
  override def toString: String = this.getClass.getSimpleName.replace("$", "")
}

object Rarity {
  def fromString(s: String): Rarity = {
    s.toLowerCase match {
      case "common" => Common
      case "rare" => Rare
      case "epic" => Epic
      case "legendary" => Legendary
      case _ => throw TeslException("Unknown rarity: " + s)
    }
  }
}

case object Common extends Rarity

case object Rare extends Rarity

case object Epic extends Rarity

case object Legendary extends Rarity
