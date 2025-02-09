package task2

import cats.Eval

object EvalTricks {

  def fib(n: Int): Eval[BigInt] = ???

  def foldRight[A, B](as: List[A], acc: Eval[B])(fn: (A, Eval[B]) => Eval[B]): Eval[B] = ???

}
