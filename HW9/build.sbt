import Dependencies.*
import sbt.Keys.libraryDependencies

ThisBuild / scalaVersion := "2.13.15"
ThisBuild / version      := "0.1.0-SNAPSHOT"

Compile / scalacOptions ++= Seq(
  "-Werror",
  "-Wdead-code",
  "-Wextra-implicit",
  "-Wnumeric-widen",
  "-Wunused",
  "-Wvalue-discard",
  "-Xlint",
  "-Xlint:-byname-implicit",
  "-Xlint:-implicit-recursion",
  "-unchecked"
)

lazy val root = (project in file("."))
  .settings(
    name := "hw9",
    libraryDependencies ++= Seq(
      scalaTest % Test
    ),
    libraryDependencies ++= catsEffect
  )
