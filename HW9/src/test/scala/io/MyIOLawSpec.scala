package io

import cats.laws.discipline.MonadErrorTests
import cats.syntax.all._
import cats.{Eq, MonadThrow}
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.Checkers
import org.typelevel.discipline.scalatest.FunSuiteDiscipline

import scala.util.Try

class MyIOLawSpec extends AnyFunSuite with FunSuiteDiscipline with Checkers {

  checkAll("IO.MonadErrorLaws", MonadErrorTests[MyIO, Throwable].monadError[Int, Int, Int])

  private implicit def monadThrowForIO[F[_]: Computation]: MonadThrow[F] = new MonadThrow[F] {
    override def map[A, B](fa: F[A])(f: A => B): F[B]                = Computation[F].map(fa)(f)
    override def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]         = Computation[F].flatMap(fa)(f)
    override def tailRecM[A, B](a: A)(f: A => F[Either[A, B]]): F[B] = Computation[F].tailRecM(a)(f)
    override def pure[A](x: A): F[A]                                 = Computation[F].pure(x)
    override def productR[A, B](fa: F[A])(fb: F[B]): F[B]            = Computation[F].*>(fa)(fb)
    override def as[A, B](fa: F[A], b: B): F[B]                      = Computation[F].as(fa)(b)
    override def void[A](fa: F[A]): F[Unit]                          = Computation[F].void(fa)
    override def attempt[A](fa: F[A]): F[Either[Throwable, A]]       = Computation[F].attempt(fa)
    override def handleErrorWith[A](fa: F[A])(f: Throwable => F[A]): F[A] =
      Computation[F].handleErrorWith(fa)(f)
    override def redeem[A, B](fa: F[A])(recover: Throwable => B, f: A => B): F[B] =
      Computation[F].redeem(fa)(recover, f)
    override def redeemWith[A, B](fa: F[A])(recover: Throwable => F[B], bind: A => F[B]): F[B] =
      Computation[F].redeemWith(fa)(recover, bind)
    override def raiseError[A](e: Throwable): F[A] = Computation[F].raiseError[A](e)
  }

  private implicit def arbIntIO: Arbitrary[MyIO[Int]] = Arbitrary(
    Gen.choose(0, 100).map(MyIO.apply(_))
  )

  private implicit def arbUnitIO: Arbitrary[MyIO[Unit]] = Arbitrary(
    Gen.const(MyIO.apply(()))
  )

  private implicit def arbIOIntInt: Arbitrary[MyIO[Int => Int]] = Arbitrary(
    implicitly[Arbitrary[Int => Int]].arbitrary.map(MyIO.apply(_))
  )

  private implicit def eqIOInt[A: Eq]: Eq[MyIO[A]] = (x: MyIO[A], y: MyIO[A]) =>
    Try(x.unsafeRunSync).toEither.eqv(Try(y.unsafeRunSync).toEither)

  private implicit def eqThrowable: Eq[Throwable] = Eq.allEqual

}
