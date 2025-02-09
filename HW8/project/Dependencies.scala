import sbt.*

object Dependencies {

  object core {
    lazy val catsCore = "org.typelevel" %% "cats-core" % "2.12.0"

    lazy val all: Seq[ModuleID] = Seq(catsCore)
  }

  object test {
    lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.19" % Test
    lazy val catsLaws = "org.typelevel" %% "cats-laws" % "2.12.0" % Test
    lazy val disciplineScalatest = "org.typelevel" %% "discipline-scalatest" % "2.3.0" % Test

    lazy val all: Seq[ModuleID] = Seq(scalaTest, catsLaws, disciplineScalatest)
  }
}
