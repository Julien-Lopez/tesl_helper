package card

class Support(override val data: SupportData) extends Card(data) {
}

class SupportData(override val id: Int, override val name: String, override val rarity: Rarity,
                  override val attributes: Traversable[Attribute], override val cost: Int,
                  override val expansionSet: ExpansionSet, override val effects: Traversable[Effect],
                  override val isProphecy: Boolean)
  extends CardData(id, name, rarity, attributes, cost, expansionSet, effects, isProphecy) {
  override def createCard(): Card = new Support(this)
}
