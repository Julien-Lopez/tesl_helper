package util

object JSONParser {
  sealed trait JSONType
  case class JSONObject(fields: List[JSONField]) extends JSONType
  case class JSONList(list: List[JSONType]) extends JSONType
  case class JSONField(fieldKey: String, fieldValue: JSONType) extends JSONType
  case class JSONString(value: String) extends JSONType
  case class JSONInteger(value: Int) extends JSONType
  case class JSONFloat(value: Float) extends JSONType
  case class UnknownJSON() extends JSONType

  sealed class JSONParserException(message: String) extends RuntimeException(message)

  def parse(json: String): JSONObject = {
    val tokens = lexJSON(json)
    var i = 0
    def nextToken() = {
      val res = tokens(i)
      i += 1
      res
    }
    def parseAny(): JSONType = {
      tokens(i) match {
        case JSONOBracket => parseObject()
        case JSONOList => parseList()
        case JSONStringToken(_) => parseString()
        case JSONIntegerToken(_) => parseInteger()
        case JSONFloatToken(_) => parseFloat()
        case error => throw new JSONParserException(s"Invalid token: $error.")
      }
    }
    def parseObject() = {
      var token = nextToken()
      if (token != JSONOBracket) throw new JSONParserException(s"Invalid token: '$token', expected '{'.")
      val res = parseFieldList()
      token = nextToken()
      if (token != JSONCBracket) {
        Console.println(tokens(i))
        throw new JSONParserException(s"Invalid token: '$token', expected '}'.")
      }
      JSONObject(res)
    }
    def parseList() = {
      var token = nextToken()
      if (token != JSONOList) throw new JSONParserException(s"Invalid token: '$token', expected '['.")
      var res : List[JSONType] = Nil
      if (nextToken() == JSONCList)
        JSONList(res)
      else {
        i -= 1 // Last token seen was not end of list
        do {
          res = parseAny() :: res
        } while (nextToken() == JSONComma)
        i -= 1 // Last token seen was not a comma
        token = nextToken()
        if (token != JSONCList) throw new JSONParserException(s"Invalid token: '$token', expected ']'.")
        JSONList(res.reverse)
      }
    }
    def parseFieldList() = {
      var res : List[JSONField] = Nil
      do {
        val id = parseString()
        val token = nextToken()
        if (token != JSONColon) throw new JSONParserException(s"Invalid token: '$token', expected ':'.")
        res = JSONField(id.value, parseAny()) :: res
      } while (nextToken() == JSONComma)
      i -= 1 // Last token seen was not a comma
      res.reverse
    }
    def parseString() = {
      nextToken() match {
        case JSONStringToken(v) => JSONString(v)
        case error => throw new JSONParserException(s"Invalid token: '$error', expected string.")
      }
    }
    def parseInteger() = {
      nextToken() match {
        case JSONIntegerToken(v) => JSONInteger(v)
        case error => throw new JSONParserException(s"Invalid token: '$error', expected integer.")
      }
    }
    def parseFloat() = {
      nextToken() match {
        case JSONFloatToken(v) => JSONFloat(v)
        case error => throw new JSONParserException(s"Invalid token: '$error', expected float.")
      }
    }
    parseObject()
  }

  private sealed trait JSONToken
  private case object JSONOBracket extends JSONToken
  private case object JSONCBracket extends JSONToken
  private case object JSONOList extends JSONToken
  private case object JSONCList extends JSONToken
  private case object JSONColon extends JSONToken
  private case object JSONComma extends JSONToken
  private case class JSONStringToken(value: String) extends JSONToken
  private case class JSONIntegerToken(value: Int) extends JSONToken
  private case class JSONFloatToken(value: Float) extends JSONToken

  private def lexJSON(json: String) : List[JSONToken] = {
    var tokens : List[JSONToken] = Nil
    var countBrackets = 0
    var countLists = 0
    var currentNumber: Option[String] = None
    var currentString: Option[String] = None
    for (c <- json) {
      if (currentString.isDefined) {
        if (c == '"') {
          tokens = JSONStringToken(currentString.get) :: tokens
          currentString = None
        }
        else
          currentString = Some(currentString.get + c)
      }
      else if (currentNumber.isDefined && c >= '0' && c <= '9')
        currentNumber = Some(currentNumber.get + c)
      else {
        if (currentNumber.isDefined) {
          tokens = JSONIntegerToken(currentNumber.get.toInt) :: tokens
          currentNumber = None
        }
        c match {
          case ' ' | '\t' | '\n' | '\r' =>
          case '{' => countBrackets += 1; tokens = JSONOBracket :: tokens
          case '}' => countBrackets -= 1
            if (countBrackets < 0) throw new JSONParserException("Unmatched bracket '}'.")
            tokens = JSONCBracket :: tokens
          case '[' => countLists += 1; tokens = JSONOList :: tokens
          case ']' => countLists -= 1
            if (countLists < 0) throw new JSONParserException("Unmatched bracket ']'.")
            tokens = JSONCList :: tokens
          case ':' => tokens = JSONColon :: tokens
          case ',' => tokens = JSONComma :: tokens
          case '"' => currentString = Some("")
          case _ if c >= '0' && c <= '9' => currentNumber = Some("" + c)
          case _ => throw new JSONParserException(s"Invalid character '$c'.")
        }
      }
    }
    if (currentString.isDefined) throw new JSONParserException("Unclosed string error.")
    if (countBrackets > 0) throw new JSONParserException("Unclosed bracket error.")
    if (countLists > 0) throw new JSONParserException("Unclosed list error.")
    tokens.reverse
  }
}
