package card

abstract class Card(val data: CardData) {
  override def toString: String = data.name
}

abstract class CardData(val id: Int, val name: String, val rarity: Rarity, val attributes: Traversable[Attribute],
                        val cost: Int, val expansionSet: ExpansionSet, val effects: Traversable[Effect],
                        val isProphecy: Boolean)
  extends Serializable {
  def createCard(): Card
}
