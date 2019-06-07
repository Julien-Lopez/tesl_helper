package card

class Creature(override val data: CreatureData) extends Card(data) {
  override def toString: String = super.toString + "(" + data.atk + ", " + data.life + ")"
}

class CreatureData(override val id: Int, override val name: String, override val rarity: Rarity,
                   override val attributes: Traversable[Attribute], override val cost: Int,
                   override val expansionSet: ExpansionSet, override val effects: Traversable[Effect],
                   override val isProphecy: Boolean, val atk: Int, val life: Int, val keywords: Traversable[Keyword],
                   val races: Traversable[Race])
  extends CardData(id, name, rarity, attributes, cost, expansionSet, effects, isProphecy) {
  override def createCard(): Card = new Creature(this)
}
