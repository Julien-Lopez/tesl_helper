import util.JSONParser.{JSONInteger, JSONList, JSONObject, JSONString}
import card.{ActionData, Attribute, CardData, Creature, CreatureData, Effect, ExpansionSet, ItemData, Keyword, Race, Rarity, SupportData}
import util.{JSONParser, TeslException}

import scala.collection.mutable.ListBuffer
import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    val src = Source.fromFile("resources/cards.json")
    val json = JSONParser.parse(src.getLines().foldLeft("")((acc, l) => acc + l))
    val res: ListBuffer[CardData] = new ListBuffer[CardData]()

    for (card <- json.fields.head.fieldValue.asInstanceOf[JSONList].list) {
      val o = card.asInstanceOf[JSONObject]
      val id = o.fields.head.fieldValue.asInstanceOf[JSONInteger].value
      val name = o.fields(1).fieldValue.asInstanceOf[JSONString].value
      val rarity = Rarity.fromString(o.fields(2).fieldValue.asInstanceOf[JSONString].value)
      val cardType = o.fields(3).fieldValue.asInstanceOf[JSONString].value
      val attributes = o.fields(4).fieldValue.asInstanceOf[JSONList].list.map(
        v => Attribute.fromString(v.asInstanceOf[JSONString].value)).toArray
      val cost = o.fields(5).fieldValue.asInstanceOf[JSONInteger].value
      val expansionSet = ExpansionSet.fromString(o.fields(6).fieldValue.asInstanceOf[JSONString].value)
      val effects: Array[Effect] = o.fields(7).fieldValue.asInstanceOf[JSONList].list.map(
        v => Effect.fromString(v.asInstanceOf[JSONString].value)).toArray
      val isProphecy = o.fields(8).fieldValue.asInstanceOf[JSONString].value == "true"
      cardType match {
        case "creature" =>
          val atk = o.fields(9).fieldValue.asInstanceOf[JSONInteger].value
          val life = o.fields(10).fieldValue.asInstanceOf[JSONInteger].value
          val keywords = o.fields(11).fieldValue.asInstanceOf[JSONList].list.map(
            v => Keyword.fromString(v.asInstanceOf[JSONString].value)).toArray
          val races = o.fields(12).fieldValue.asInstanceOf[JSONList].list.map(
            v => Race.fromString(v.asInstanceOf[JSONString].value)).toArray
          res += new CreatureData(id, name, rarity, attributes, cost, expansionSet, effects, isProphecy, atk, life, keywords, races)
        case "item" =>
          val atkBonus = o.fields(9).fieldValue.asInstanceOf[JSONInteger].value
          val lifeBonus = o.fields(10).fieldValue.asInstanceOf[JSONInteger].value
          val keywords = o.fields(11).fieldValue.asInstanceOf[JSONList].list.map(
            v => Keyword.fromString(v.asInstanceOf[JSONString].value)).toArray
          res += new ItemData(id, name, rarity, attributes, cost, expansionSet, effects, isProphecy, atkBonus, lifeBonus, keywords)
        case "action" => new ActionData(id, name, rarity, attributes, cost, expansionSet, effects, isProphecy)
        case "support" => new SupportData(id, name, rarity, attributes, cost, expansionSet, effects, isProphecy)
        case _ => throw TeslException("[ERROR] Error loading card database: unknown card type " + cardType)
      }
    }
    res.toArray
    print(new Creature(res.toArray.head.asInstanceOf[CreatureData]))
  }
}
