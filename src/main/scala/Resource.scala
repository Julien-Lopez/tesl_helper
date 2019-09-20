import java.io.{File, FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

import card.{ActionData, Attribute, CardData, CreatureData, Effect, ExpansionSet, ItemData, Keyword, Race, Rarity, SupportData}
import player.PlayerProfile
import util.JSONParser.{JSONInteger, JSONList, JSONObject, JSONString}
import util.{JSONParser, TeslException}

import scala.collection.mutable.ListBuffer
import scala.io.Source

package object Resource {
  val resourceFolder : String = "resources"
  val imageFolder : String = resourceFolder + "/images"
  val cardData : Array[CardData] = {
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
  }
  var players: Map[String, PlayerProfile] =
    new File(resourceFolder).listFiles
      .filter(_.getPath.endsWith(".data"))
      .map(f => {
        val fis = new FileInputStream(f)
        val ois = new ObjectInputStream(fis)
        ois.readObject.asInstanceOf[PlayerProfile]
      })
      .map(p => p.name -> p)
      .toMap[String, PlayerProfile]
  val ais: Array[PlayerProfile] = Array(new PlayerProfile("Tea"))

  def registerPlayer(name: String): Option[PlayerProfile] = {
    val newPlayer = new PlayerProfile(name)
    if (players.exists(_._2.idName == newPlayer.idName)) None
    else {
      val fos = new FileOutputStream(resourceFolder + "/" + newPlayer.idName + ".data")
      val oos = new ObjectOutputStream(fos)
      players += (name -> newPlayer)
      oos.writeObject(newPlayer)
      Some(newPlayer)
    }
  }
}
