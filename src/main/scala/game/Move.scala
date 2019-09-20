package game

import util.TeslException

sealed trait Move extends Serializable {
  override def toString: String = this.getClass.getSimpleName.replace("$", "")
}

object Move {
  def fromString(s: String): Move = {
    val playCard = """draw\(([1-9][0-9]*)\)""".r
    val target = """target\(([1-9][0-9]*), ([1-9][0-9]*)\)""".r
    s.toLowerCase match {
      case "endturn" => EndTurn
      case "usering" => EndTurn
      case playCard(n) => PlayCard(n.toInt)
      case target(n1, n2) => Target(n1.toInt, n2.toInt)
      case _ => throw TeslException("Unknown move: " + s)
    }
  }
}

case object EndTurn extends Move

case object UseRing extends Move

case class PlayCard(indexCard: Int) extends Move

case class Target(indexAttacker: Int, indexDefender: Int) extends Move
