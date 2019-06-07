package util

case class TeslException(private val message: String = "", private val cause: Throwable = None.orNull)
  extends Exception(message, cause)
