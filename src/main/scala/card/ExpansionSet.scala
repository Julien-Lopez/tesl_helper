package card

import util.TeslException

sealed trait ExpansionSet extends Serializable {
  override def toString: String = this.getClass.getSimpleName.replace("$", "")
}

object ExpansionSet {
  def fromString(s: String): ExpansionSet = {
    s.toLowerCase match {
      case "monthly reward" => MonthlyReward
      case "houses of morrowind" => HousesOfMorrowind
      case "alliance war" => AllianceWar
      case _ => throw TeslException("Unknown expansion set: " + s)
    }
  }
}

case object MonthlyReward extends ExpansionSet

case object HousesOfMorrowind extends ExpansionSet

case object AllianceWar extends ExpansionSet
