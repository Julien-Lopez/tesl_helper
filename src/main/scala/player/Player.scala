package player

import scala.collection.mutable.ListBuffer

abstract class Player(val profile: PlayerProfile) {
  override def toString: String = profile.name
}

class PlayerProfile(val name: String) extends Serializable {
  val idName: String = name.toLowerCase
  val deckRecipes: ListBuffer[DeckRecipe] = ListBuffer()
}