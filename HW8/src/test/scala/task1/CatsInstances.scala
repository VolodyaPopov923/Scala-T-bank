package task1

import cats._
import cats.syntax.all._
import hierarchy.{Monad => MyMonad}
import hierarchy.{Apply => MyApply}
import hierarchy.{Functor => MyFunctor}
import hierarchy.{FlatMap => MyFlatMap}
import hierarchy.{Applicative => MyApplicative}
import org.scalacheck.{Arbitrary, Gen}

object CatsInstances {
  implicit def catsApply[F[_]](implicit myApply: MyApply[F]): Apply[F] = new Apply[F] {
    override def ap[A, B](ff: F[A => B])(fa: F[A]): F[B] = myApply.ap(ff)(fa)

    override def map[A, B](fa: F[A])(f: A => B): F[B] = myApply.map(fa)(f)
  }

  implicit def catsTreeMonad(implicit myMonad: MyMonad[Tree]): Monad[Tree] = new Monad[Tree] {
    override def pure[A](x: A): Tree[A] = myMonad.pure(x)

    override def ap[A, B](ff: Tree[A => B])(fa: Tree[A]): Tree[B] = myMonad.ap(ff)(fa)

    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = myMonad.map(fa)(f)

    override def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] = myMonad.flatMap(fa)(f)

    override def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] = myMonad.tailRecM(a)(f)
  }
  implicit def catsFunctor[F[_]](implicit myFunctor: MyFunctor[F]): Functor[F] = new Functor[F] {
    override def map[A, B](fa: F[A])(f: A => B): F[B] = myFunctor.map(fa)(f)
  }

  implicit def catsFlatMap[F[_]](implicit myFlatMap: MyFlatMap[F]): FlatMap[F] = new FlatMap[F] {
    override def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B] = myFlatMap.flatMap(fa)(f)

    override def tailRecM[A, B](a: A)(f: A => F[Either[A, B]]): F[B] = myFlatMap.tailRecM(a)(f)

    override def ap[A, B](ff: F[A => B])(fa: F[A]): F[B] = myFlatMap.ap(ff)(fa)

    override def map[A, B](fa: F[A])(f: A => B): F[B] = myFlatMap.map(fa)(f)
  }

  implicit def catsApplicative[F[_]](implicit myApplicative: MyApplicative[F]): Applicative[F] = new Applicative[F] {
    override def pure[A](x: A): F[A] = myApplicative.pure(x)

    override def ap[A, B](ff: F[A => B])(fa: F[A]): F[B] = myApplicative.ap(ff)(fa)

    override def map[A, B](fa: F[A])(f: A => B): F[B] = myApplicative.map(fa)(f)
  }

  // instances required by cats-laws
  private def compareTrees[A](a: Tree[A], b: Tree[A])(implicit eqA: Eq[A]): Boolean =
    (a, b) match {
      case (Leaf(va), Leaf(vb)) => eqA.eqv(va, vb)
      case (Branch(leftA, rightA), Branch(leftB, rightB)) =>
        compareTrees(leftA, leftB) && compareTrees(rightA, rightB)
      case (_, _) => false
    }

  implicit def catsEqTree[A](implicit eqA: Eq[A]): Eq[Tree[A]] =
    (x: Tree[A], y: Tree[A]) => compareTrees(x, y)

  implicit def catsArbitraryForTree[A](implicit arbA: Arbitrary[A]): Arbitrary[Tree[A]] = {
    def scalacheckGenForTree(depth: Int)(implicit arbA: Arbitrary[A]): Gen[Tree[A]] = {
      if (depth == 0) arbA.arbitrary.map(Leaf(_))
      else
        Gen.oneOf(
          arbA.arbitrary.map(Leaf(_)),
          for {
            left <- scalacheckGenForTree(depth - 1)
            right <- scalacheckGenForTree(depth - 1)
          } yield Branch(left = left, right = right)
        )
    }

    Arbitrary(scalacheckGenForTree(6))
  }

}
