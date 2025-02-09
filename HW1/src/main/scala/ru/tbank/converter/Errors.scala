package ru.tbank.converter

object Errors {
  class MoneyAmountShouldBeNonNegativeException extends Exception

  class UnsupportedCurrencyException extends Exception

  class CurrencyMismatchException extends Exception

  class SameCurrencyExchangeException extends Exception
}
