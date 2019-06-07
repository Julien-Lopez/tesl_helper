package card

class Item(override val data: ItemData) extends Card(data) {
  override def toString: String = super.toString + "(" + data.atkBonus + ", " + data.lifeBonus + ")"
}

class ItemData(override val id: Int, override val name: String, override val rarity: Rarity,
               override val attributes: Traversable[Attribute], override val cost: Int,
               override val expansionSet: ExpansionSet, override val effects: Traversable[Effect],
               override val isProphecy: Boolean, val atkBonus: Int, val lifeBonus: Int,
               val keywords: Traversable[Keyword])
  extends CardData(id, name, rarity, attributes, cost, expansionSet, effects, isProphecy) {
  override def createCard(): Card = new Item(this)
}
