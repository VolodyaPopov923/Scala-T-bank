package ru.tbank.converter

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import ru.tbank.converter.Errors.{SameCurrencyExchangeException, UnsupportedCurrencyException}

class CurrencyConverterSpec extends AnyFlatSpec with Matchers {
  "exchange" should "convert money for supported currencies" in {
    val rates = Map(
      "USD" -> Map("RUB" -> BigDecimal(72.5)),
      "RUB" -> Map("USD" -> BigDecimal(1 / 72.5))
    )
    val converter = CurrencyConverter(rates)
    val exchangedRub = converter.exchange(Money(2, "USD"), "RUB")
    val exchangedUsd = converter.exchange(Money(10, "RUB"), "USD")

    exchangedRub.amount shouldEqual 145
    exchangedRub.currency shouldEqual "RUB"
    exchangedUsd.amount shouldEqual BigDecimal(1 / 7.25)
    exchangedUsd.currency shouldEqual "USD"
  }

  it should "throw SameCurrencyExchangeException if source and target currencies are the same" in {
    val rates = Map(
      "USD" -> Map("RUB" -> BigDecimal(72.5)),
      "RUB" -> Map("USD" -> BigDecimal(1 / 72.5))
    )
    val converter = CurrencyConverter(rates)
    a[SameCurrencyExchangeException] should be thrownBy converter.exchange(Money(1, "USD"), "USD")
  }

  "converter constructor" should "throw UnsupportedCurrencyException if rates dictionary contains wrong currency" in {
    val rates = Map(
      "GBP" -> Map("RUB" -> BigDecimal(85))
    )
    an[UnsupportedCurrencyException] should be thrownBy CurrencyConverter(rates)
  }
}
