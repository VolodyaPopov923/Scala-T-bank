package ru.tbank.converter

import scala.annotation.targetName

case class Money private (amount: BigDecimal, currency: String) {
  def +(other: Money): Money = ???

  def -(other: Money): Money = ???

  def isSameCurrency(other: Money): Boolean = ???
}

object Money {
  def apply(amount: BigDecimal, currency: String): Money = ???
}
