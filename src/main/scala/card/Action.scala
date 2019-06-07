package card

class Action(override val data: ActionData) extends Card(data) {
}

class ActionData(override val id: Int, override val name: String, override val rarity: Rarity,
                 override val attributes: Traversable[Attribute], override val cost: Int,
                 override val expansionSet: ExpansionSet, override val effects: Traversable[Effect],
                 override val isProphecy: Boolean)
  extends CardData(id, name, rarity, attributes, cost, expansionSet, effects, isProphecy) {
  override def createCard(): Card = new Action(this)
}
