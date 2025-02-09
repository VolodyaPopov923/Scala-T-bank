package io

import scala.util.Try

/** Класс типов, позволяющий комбинировать описания вычислений, которые могут либо успешно
  * завершиться с некоторым значением, либо завершиться неуспешно, выбросив исключение Throwable.
  * @tparam F
  *   тип вычисления
  */
trait Computation[F[_]] {

  def map[A, B](fa: F[A])(f: A => B): F[B]
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
  def tailRecM[A, B](a: A)(f: A => F[Either[A, B]]): F[B]
  def pure[A](a: A): F[A]
  def *>[A, B](fa: F[A])(another: F[B]): F[B]
  def as[A, B](fa: F[A])(newValue: => B): F[B]
  def void[A](fa: F[A]): F[Unit]
  def attempt[A](fa: F[A]): F[Either[Throwable, A]]
  def option[A](fa: F[A]): F[Option[A]]

  /** Если вычисление fa выбрасывает ошибку, то обрабатывает ее функцией f, без изменения типа
    * выходного значения.
    * @return
    *   результат вычисления fa или результат функции f
    */
  def handleErrorWith[A, AA >: A](fa: F[A])(f: Throwable => F[AA]): F[AA]

  /** Обрабатывает ошибку вычисления чистой функцией recover или преобразует результат вычисления
    * чистой функцией.
    * @return
    *   результат вычисления преобразованный функцией map или результат функции recover
    */
  def redeem[A, B](fa: F[A])(recover: Throwable => B, map: A => B): F[B]
  def redeemWith[A, B](fa: F[A])(recover: Throwable => F[B], bind: A => F[B]): F[B]

  /** Выполняет вычисление. "unsafe", потому что при неуспешном завершении может выбросить
    * исключение.
    * @param fa
    *   еще не начавшееся вычисление
    * @tparam A
    *   тип результата вычисления
    * @return
    *   результат вычисления, если оно завершится успешно.
    */
  def unsafeRunSync[A](fa: F[A]): A

  /** Оборачивает ошибку в контекст вычисления.
    * @param error
    *   ошибка
    * @tparam A
    *   тип результата вычисления. Т.к. вычисление сразу завершится ошибкой при выполнении, то может
    *   быть любым.
    * @return
    *   создает описание вычисления, которое сразу же завершается с поданной ошибкой.
    */
  def raiseError[A](error: Throwable): F[A]

}

object Computation {
  def apply[F[_]: Computation]: Computation[F] = implicitly[Computation[F]]
}

final class MyIO[A]( /* ??? */ ) {

  def map[B](f: A => B)(implicit
    comp: Computation[MyIO]
  ): MyIO[B] = ???

  def flatMap[B](f: A => MyIO[B])(implicit
    comp: Computation[MyIO]
  ): MyIO[B] = ???

  def tailRecM[B](f: A => MyIO[Either[A, B]])(implicit
    comp: Computation[MyIO]
  ): MyIO[B] = ???

  def *>[B](another: MyIO[B])(implicit
    comp: Computation[MyIO]
  ): MyIO[B] = ???

  def as[B](newValue: => B)(implicit
    comp: Computation[MyIO]
  ): MyIO[B] = ???

  def void(implicit
    comp: Computation[MyIO]
  ): MyIO[Unit] = ???

  def attempt(implicit
    comp: Computation[MyIO]
  ): MyIO[Either[Throwable, A]] = ???

  def option(implicit
    comp: Computation[MyIO]
  ): MyIO[Option[A]] = ???

  def handleErrorWith[AA >: A](f: Throwable => MyIO[AA])(implicit
    comp: Computation[MyIO]
  ): MyIO[AA] = ???

  def redeem[B](recover: Throwable => B, map: A => B)(implicit
    comp: Computation[MyIO]
  ): MyIO[B] = ???

  def redeemWith[B](recover: Throwable => MyIO[B], bind: A => MyIO[B])(implicit
    comp: Computation[MyIO]
  ): MyIO[B] = ???

  def unsafeRunSync(implicit
    comp: Computation[MyIO]
  ): A = ???

}

object MyIO {

  implicit val computationInstanceForIO: Computation[MyIO] = new Computation[MyIO] {
    override def map[A, B](fa: MyIO[A])(f: A => B): MyIO[B]                                   = ???
    override def flatMap[A, B](fa: MyIO[A])(f: A => MyIO[B]): MyIO[B]                         = ???
    override def tailRecM[A, B](a: A)(f: A => MyIO[Either[A, B]]): MyIO[B]                    = ???
    override def pure[A](a: A): MyIO[A]                                                       = ???
    override def *>[A, B](fa: MyIO[A])(another: MyIO[B]): MyIO[B]                             = ???
    override def as[A, B](fa: MyIO[A])(newValue: => B): MyIO[B]                               = ???
    override def void[A](fa: MyIO[A]): MyIO[Unit]                                             = ???
    override def attempt[A](fa: MyIO[A]): MyIO[Either[Throwable, A]]                          = ???
    override def option[A](fa: MyIO[A]): MyIO[Option[A]]                                      = ???
    override def handleErrorWith[A, AA >: A](fa: MyIO[A])(f: Throwable => MyIO[AA]): MyIO[AA] = ???
    override def redeem[A, B](fa: MyIO[A])(recover: Throwable => B, map: A => B): MyIO[B]     = ???
    override def redeemWith[A, B](
      fa: MyIO[A]
    )(recover: Throwable => MyIO[B], bind: A => MyIO[B]): MyIO[B] = ???
    override def unsafeRunSync[A](fa: MyIO[A]): A         = ???
    override def raiseError[A](error: Throwable): MyIO[A] = ???
  }

  def apply[A](body: => A): MyIO[A] = ???

  def suspend[A](thunk: => MyIO[A])(implicit
    comp: Computation[MyIO]
  ): MyIO[A] = ???

  def delay[A](body: => A): MyIO[A] = ???
  def pure[A](a: A): MyIO[A]        = ???

  def fromEither[A](e: Either[Throwable, A])(implicit
    comp: Computation[MyIO]
  ): MyIO[A] = ???

  def fromOption[A](option: Option[A])(orElse: => Throwable)(implicit
    comp: Computation[MyIO]
  ): MyIO[A] = ???

  def fromTry[A](t: Try[A])(implicit
    comp: Computation[MyIO]
  ): MyIO[A] = ???

  def none[A]: MyIO[Option[A]] = ???

  def raiseUnless(cond: Boolean)(e: => Throwable)(implicit
    comp: Computation[MyIO]
  ): MyIO[Unit] = ???

  def raiseWhen(cond: Boolean)(e: => Throwable)(implicit
    comp: Computation[MyIO]
  ): MyIO[Unit] = ???

  def raiseError[A](error: Throwable)(implicit
    comp: Computation[MyIO]
  ): MyIO[A] = comp.raiseError(error)

  def unlessA(cond: Boolean)(action: => MyIO[Unit]): MyIO[Unit] = ???
  def whenA(cond: Boolean)(action: => MyIO[Unit]): MyIO[Unit]   = ???
  val unit: MyIO[Unit]                                          = ???

}
