import sbt.*

object Dependencies {

  val scalaTest  = "org.scalatest"     %% "scalatest"                     % "3.2.19"
  val catsTest   = "org.typelevel"     %% "cats-effect-testing-scalatest" % "1.5.0"
  val catsEffect = "org.typelevel"     %% "cats-effect"                   % "3.5.4"
  val scalaCheck = "org.scalatestplus" %% "scalacheck-1-18"               % "3.2.19.0"
  val shapeless  = "com.chuusai"       %% "shapeless"                     % "2.3.12"

}
