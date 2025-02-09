package ru.tbank.converter

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import ru.tbank.converter.Errors.{CurrencyMismatchException, MoneyAmountShouldBeNonNegativeException, UnsupportedCurrencyException}

class MoneySpec extends AnyFlatSpec with Matchers {
  "apply" should "accept valid non negative amounts" in {
    noException should be thrownBy Money(BigDecimal(0), "USD")
    noException should be thrownBy Money(BigDecimal(100.5), "USD")
  }

  it should "accept valid currencies" in {
    noException should be thrownBy Money(BigDecimal(0), "RUB")
    noException should be thrownBy Money(BigDecimal(0), "USD")
    noException should be thrownBy Money(BigDecimal(0), "EUR")
  }

  it should "throw MoneyAmountShouldBeNonNegativeException for negative amounts" in {
    an[MoneyAmountShouldBeNonNegativeException] should be thrownBy Money(BigDecimal(-20.33), "USD")
    an[MoneyAmountShouldBeNonNegativeException] should be thrownBy Money(BigDecimal("-15"), "RUB")
  }

  it should "throw UnsupportedCurrencyException for not supported currencies" in {
    an[UnsupportedCurrencyException] should be thrownBy Money(BigDecimal(13.2), "CHF")
    an[UnsupportedCurrencyException] should be thrownBy Money(BigDecimal(1000), "GBP")
    an[UnsupportedCurrencyException] should be thrownBy Money(BigDecimal(15320.67), "DZD")
  }

  "+" should "operate with same currencies" in {
    val amount1 = Money(BigDecimal("100.5"), "RUB")
    val amount2 = Money(BigDecimal("500.0"), "RUB")
    (amount1 + amount2) shouldEqual Money(BigDecimal(600.5), "RUB")
  }

  it should "throw CurrencyMismatchException for different currencies" in {
    val amount1 = Money(BigDecimal("100.5"), "USD")
    val amount2 = Money(BigDecimal("500.0"), "RUB")
    an[CurrencyMismatchException] should be thrownBy amount1 + amount2
  }

  "-" should "operate with same currencies" in {
    val amount1 = Money(BigDecimal("100.5"), "RUB")
    val amount2 = Money(BigDecimal("500.0"), "RUB")
    (amount2 - amount1) shouldEqual Money(BigDecimal(399.5), "RUB")
  }

  it should "throw CurrencyMismatchException for different currencies" in {
    val amount1 = Money(BigDecimal("100.5"), "USD")
    val amount2 = Money(BigDecimal("500.0"), "RUB")
    an[CurrencyMismatchException] should be thrownBy amount2 - amount1
  }

  it should "throw MoneyAmountShouldBeNonNegativeException if the result would become negative" in {
    val amount1 = Money(BigDecimal(100.5), "RUB")
    val amount2 = Money(BigDecimal(500.0), "RUB")
    an[MoneyAmountShouldBeNonNegativeException] should be thrownBy amount1 - amount2
  }

  "isSameCurrency" should "return true if currencies are the same" in {
    ???
  }

  it should "return false if currencies are different" in {
    ???
  }
}
