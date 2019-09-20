package card

import util.TeslException

sealed trait Effect extends Serializable {
  override def toString: String = this.getClass.getSimpleName.replace("$", "")
}

object Effect {
  def fromString(s: String): Effect = {
    val draw = """draw\(([1-9][0-9]*)\)""".r
    val heal = """heal\(([1-9][0-9]*)\)""".r
    val target = """target\(([1-9][0-9]*), ([a-z][a-z]*)\)""".r
    s match {
      case "cover" => Cover
      case "unrestrictedattack" => UnrestrictedAttack
      case draw(n) => Draw(n.toInt)
      case heal(n) => Heal(n.toInt)
      case target(n, effect) => Target(n.toInt, fromString(effect))
      case _ => throw TeslException("Unknown effect: " + s)
    }
  }
}

case object Cover extends Effect

case object UnrestrictedAttack extends Effect

case class Draw(nbCards: Int) extends Effect

case class Heal(points: Int) extends Effect

case class Target(nbTargets: Int, effect: Effect) extends Effect
