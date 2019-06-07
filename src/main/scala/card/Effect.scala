package card

import util.TeslException

sealed trait Effect extends Serializable {
  override def toString: String = this.getClass.getSimpleName.replace("$", "")
}

object Effect {
  def fromString(s: String): Effect = {
    val draw = """draw\(([1-9][0-9]*)\)""".r
    val heal = """heal\(([1-9][0-9]*)\)""".r
    s match {
      case draw(n) => Draw(n.toInt)
      case heal(n) => Heal(n.toInt)
      case _ => throw TeslException("Unknown effect: " + s)
    }
  }
}

case class Draw(nbCards: Int) extends Effect

case class Heal(points: Int) extends Effect
