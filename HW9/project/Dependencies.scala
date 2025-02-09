import sbt.*

object Dependencies {

  val scalaTest = "org.scalatest" %% "scalatest" % "3.2.19"

  val catsEffect = Seq(
    "org.typelevel" %% "cats-effect"          % "3.5.5",
    "org.typelevel" %% "cats-laws"            % "2.12.0" % Test,
    "org.typelevel" %% "discipline-scalatest" % "2.3.0"  % Test
  )

}
