package sbt

final class MessageOnlyException(override val toString: String) extends RuntimeException(toString)