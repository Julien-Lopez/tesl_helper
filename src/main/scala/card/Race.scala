package card

import util.TeslException

sealed trait Race extends Serializable {
  override def toString: String = this.getClass.getSimpleName.replace("$", "")
}

object Race {
  def fromString(s: String): Race = {
    s.toLowerCase match {
      case "dark elf" => DarkElf
      case _ => throw TeslException("Unknown race: " + s)
    }
  }
}

case object DarkElf extends Race
