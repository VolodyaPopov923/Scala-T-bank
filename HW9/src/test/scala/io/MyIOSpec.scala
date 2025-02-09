package io

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{Failure, Try}

class MyIOSpec extends AnyFlatSpec with Matchers {

  private val exception   = new RuntimeException("Oops")
  private val largeNumber = 100000

  "MyIO" should "handle error from raiseError" in {
    MyIO(1)
      .flatMap[Int](_ => MyIO.raiseError(exception).as(2))
      .as(3)
      .attempt
      .unsafeRunSync shouldBe Left(exception)
  }

  it should "handle error from throw" in {
    MyIO(1)
      .flatMap[Int](_ => MyIO.delay(throw exception).as(2))
      .as(3)
      .attempt
      .unsafeRunSync shouldBe Left(exception)
  }

  it should "combine tailRecM with flatMap and redeemWith to handle side effects and errors correctly" in {
    var sideEffectCounter = 0

    def incrementWithFlatMap(n: Int): MyIO[Int] = Computation[MyIO].tailRecM(0) { acc =>
      MyIO.suspend {
        sideEffectCounter += 1
        if (sideEffectCounter == 10)
          MyIO.raiseError[Either[Int, Int]](new RuntimeException("Error at 10"))
        else if (acc >= n) MyIO(Right(acc))
        else MyIO(Left(acc + 1))
      }
    }

    val result = incrementWithFlatMap(20)
      .redeemWith(
        _ => MyIO(-1),
        v => MyIO(v + sideEffectCounter)
      )
      .flatMap(count => MyIO(count * 2))
      .unsafeRunSync

    sideEffectCounter shouldBe 10
    result shouldBe -2
  }

  it should "not execute side effects until unsafeRunSync is called" in {
    var sideEffectCounter = 0

    val incrementEffect: MyIO[Unit] = MyIO {
      sideEffectCounter += 1
    }

    sideEffectCounter shouldBe 0

    incrementEffect.unsafeRunSync

    sideEffectCounter shouldBe 1
  }

  it should "not execute side effects in flatMap chains until unsafeRunSync is called" in {
    var sideEffectCounter = 0

    val chainedEffect: MyIO[Int] = MyIO {
      sideEffectCounter += 1
      1
    }.flatMap { x =>
      MyIO {
        sideEffectCounter += 1
        x + 1
      }
    }

    sideEffectCounter shouldBe 0

    val result = chainedEffect.unsafeRunSync

    sideEffectCounter shouldBe 2

    result shouldBe 2
  }

  it should "correctly combine pure and flatMap" in {
    val result = MyIO(1).flatMap(v => MyIO(v + 1)).unsafeRunSync
    result shouldBe 2
  }

  it should "correctly combine pure and raiseError" in {
    val result = MyIO
      .pure(1)
      .flatMap(_ => MyIO.raiseError[Int](exception))
      .redeemWith(
        _ => MyIO.pure(-1),
        v => MyIO.pure(v)
      )
      .unsafeRunSync

    result shouldBe -1
  }

  it should "defer side effects with suspend and flatMap" in {
    var sideEffectCounter = 0

    val effect = MyIO.suspend {
      sideEffectCounter += 1
      MyIO.pure(sideEffectCounter)
    }

    sideEffectCounter shouldBe 0

    val result = effect.flatMap(v => MyIO.pure(v + 1)).unsafeRunSync

    sideEffectCounter shouldBe 1
    result shouldBe 2
  }

  it should "combine raiseError with redeemWith to recover from errors" in {
    val result = MyIO
      .raiseError[Int](exception)
      .redeemWith(
        _ => MyIO.pure(-1),
        MyIO.pure
      )
      .unsafeRunSync

    result shouldBe -1
  }

  it should "handle flatMap after raiseError and not execute subsequent operations" in {
    var sideEffectCounter = 0
    val effect = MyIO
      .raiseError[Int](exception)
      .flatMap(_ =>
        MyIO {
          sideEffectCounter += 1
          sideEffectCounter
        }
      )
      .redeemWith(
        _ => MyIO.pure(-1),
        MyIO.pure
      )

    val result = effect.unsafeRunSync

    sideEffectCounter shouldBe 0
    result shouldBe -1
  }

  it should "combine suspend, flatMap, and raiseError with redeemWith for deferred errors" in {
    var sideEffectCounter = 0

    val effect = MyIO
      .suspend {
        sideEffectCounter += 1
        MyIO.raiseError[Int](exception)
      }
      .flatMap(_ =>
        MyIO {
          sideEffectCounter += 1
          sideEffectCounter
        }
      )
      .redeemWith(
        _ => MyIO.pure(-1),
        MyIO.pure
      )

    val result = effect.unsafeRunSync

    sideEffectCounter shouldBe 1
    result shouldBe -1
  }

  it should "properly chain suspend, flatMap, and redeemWith" in {
    var sideEffectCounter = 0

    val effect = MyIO
      .suspend {
        sideEffectCounter += 1
        MyIO.pure(5)
      }
      .flatMap(v => MyIO.pure(v * 2))
      .redeemWith(
        _ => MyIO.pure(-1),
        v => MyIO.pure(v + 3)
      )

    val result = effect.unsafeRunSync

    sideEffectCounter shouldBe 1
    result shouldBe 13 // (5 * 2) + 3 = 13
  }

  it should "compose pure, suspend, raiseError, and redeemWith correctly" in {
    var sideEffectCounter = 0

    val effect = MyIO
      .pure(10)
      .flatMap(_ =>
        MyIO.suspend {
          sideEffectCounter += 1
          MyIO.raiseError[Int](exception)
        }
      )
      .redeemWith(
        _ => MyIO(sideEffectCounter),
        MyIO.pure
      )

    val result = effect.unsafeRunSync

    sideEffectCounter shouldBe 1
    result shouldBe 1
  }

  it should "execute only on unsafeRunSync when combining pure, suspend, and redeemWith" in {
    var sideEffectCounter = 0

    val effect = MyIO
      .pure(1)
      .flatMap(_ =>
        MyIO.suspend {
          sideEffectCounter += 1
          MyIO.pure(2)
        }
      )
      .flatMap(v => MyIO.pure(v * 3))
      .redeemWith(
        _ => MyIO.pure(-1),
        v => MyIO.pure(v + 1)
      )

    sideEffectCounter shouldBe 0

    val result = effect.unsafeRunSync

    sideEffectCounter shouldBe 1
    result shouldBe 7 // (2 * 3) + 1 = 7
  }

  "suspend" should "be stack-safe for large recursive compositions" in {
    def recursiveSuspend(n: Int): MyIO[Int] =
      if (n <= 0) MyIO.pure(0)
      else MyIO.suspend(recursiveSuspend(n - 1))

    val result = recursiveSuspend(largeNumber).unsafeRunSync
    result shouldBe 0
  }

  it should "delay side effects until unsafeRunSync is called" in {
    var sideEffect = false

    val io = MyIO.suspend {
      sideEffect = true
      MyIO.pure(42)
    }

    sideEffect shouldBe false
    io.unsafeRunSync shouldBe 42
    sideEffect shouldBe true
  }

  "raiseError" should "throw correct exception" in {
    Try {
      MyIO.raiseError(new RuntimeException("message")).unsafeRunSync
    } match {
      case Failure(ex: RuntimeException) => ex.getMessage shouldEqual "message"
      case result => fail(s"""Expected RuntimeException("message"), but was '$result'""")
    }
  }

  it should "throw correct exception with big stack" in {
    def recursiveErrorHandling(n: Int): MyIO[Int] =
      if (n <= 0) MyIO.pure(42)
      else
        MyIO
          .raiseError[Int](new RuntimeException(n.toString))
          .flatMap(_ => recursiveErrorHandling(n - 1))

    val result = Try(recursiveErrorHandling(largeNumber).unsafeRunSync)
    result match {
      case Failure(ex) => ex.getMessage shouldEqual largeNumber.toString
      case result      => fail(s"""Expected RuntimeException("message"), but was '$result'""")
    }
  }

  "flatMap" should "be stack-safe for large flatMap chains" in {
    def recursiveFlatMap(n: Int): MyIO[Int] =
      if (n <= 0) MyIO.pure(0)
      else MyIO.pure(n).flatMap(x => recursiveFlatMap(x - 1))

    val result = recursiveFlatMap(largeNumber).unsafeRunSync
    result shouldBe 0
  }

  it should "chain computations and produce side effects in order" in {
    var sideEffect1 = false
    var sideEffect2 = false

    val io = MyIO { sideEffect1 = true; 21 }
      .flatMap(value => MyIO { sideEffect2 = true; value * 2 })

    sideEffect1 shouldBe false
    sideEffect2 shouldBe false
    io.unsafeRunSync shouldBe 42
    sideEffect1 shouldBe true
    sideEffect2 shouldBe true
  }

  "redeemWith" should "be stack-safe for large chains of error handling" in {
    val program = (1 to largeNumber).foldLeft[MyIO[Int]](MyIO.raiseError(exception))((acc, n) =>
      acc.redeemWith(
        _ => MyIO.pure(n),
        MyIO.pure
      )
    )

    program.unsafeRunSync shouldBe 1
  }

  it should "produce side effects only in the correct branch based on success or failure" in {
    var recoverEffect = false
    var bindEffect    = false
    val exception     = new RuntimeException("Test error")

    val ioError = MyIO
      .raiseError[Int](exception)
      .redeemWith(
        _ => MyIO { recoverEffect = true; -1 },
        _ => MyIO { bindEffect = true; 1 }
      )

    recoverEffect shouldBe false
    bindEffect shouldBe false
    ioError.unsafeRunSync shouldBe -1
    recoverEffect shouldBe true
    bindEffect shouldBe false

    recoverEffect = false
    bindEffect = false

    val ioSuccess = MyIO
      .pure(42)
      .redeemWith(
        _ => MyIO { recoverEffect = true; -1 },
        _ => MyIO { bindEffect = true; 1 }
      )

    ioSuccess.unsafeRunSync shouldBe 1
    recoverEffect shouldBe false
    bindEffect shouldBe true
  }

  "tailRecM" should "perform tail-recursive computation without stack overflow" in {
    def countdown(n: Int): MyIO[Int] = Computation[MyIO].tailRecM(n) { current =>
      if (current <= 0) MyIO.delay(Right(0))
      else MyIO.delay(Left(current - 1))
    }

    countdown(largeNumber).unsafeRunSync shouldBe 0
  }

  it should "only execute side effects once upon unsafeRunSync" in {
    var counter = 0

    def incrementUpTo(n: Int): MyIO[Int] = Computation[MyIO].tailRecM(0) { acc =>
      MyIO.suspend {
        if (acc >= n)
          MyIO.pure(Right(acc))
        else {
          counter += 1
          MyIO.pure(Left(acc + 1))
        }
      }
    }

    val target = 50
    counter shouldBe 0

    val result = incrementUpTo(target).unsafeRunSync

    counter shouldBe target
    result shouldBe target
  }

  it should "respect deferred side effects with tailRecM when combined with raiseError" in {
    var sideEffectCounter = 0

    def incrementWithError(n: Int): MyIO[Int] = Computation[MyIO].tailRecM(0) { acc =>
      MyIO.suspend {
        sideEffectCounter += 1
        if (sideEffectCounter == 10)
          MyIO.raiseError[Either[Int, Int]](new RuntimeException("Error at 10"))
        else if (acc >= n) MyIO.pure(Right(acc))
        else MyIO.pure(Left(acc + 1))
      }
    }

    val result = incrementWithError(50)
      .redeemWith(
        _ => MyIO.pure(-1),
        v => MyIO.pure(v)
      )
      .unsafeRunSync

    sideEffectCounter shouldBe 10
    result shouldBe -1
  }

  "delay" should "be stack-safe for large recursive compositions" in {
    def recursiveDelay(n: Int): MyIO[Int] =
      if (n <= 0) MyIO.pure(0)
      else MyIO.delay(recursiveDelay(n - 1)).flatMap(identity)

    val result = recursiveDelay(largeNumber).unsafeRunSync
    result shouldBe 0
  }

  it should "be stack-safe for large recursive compositions add throw correct error" in {
    def recursiveDelay(n: Int): MyIO[Int] =
      if (n <= 0) throw new RuntimeException("message")
      else MyIO.delay(recursiveDelay(n - 1)).flatMap(identity)

    Try(recursiveDelay(largeNumber).unsafeRunSync) match {
      case Failure(ex: RuntimeException) => ex.getMessage shouldEqual "message"
      case result => fail(s"""Expected RuntimeException("message"), but was '$result'""")
    }
  }

}
