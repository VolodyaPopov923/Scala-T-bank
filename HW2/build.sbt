import Dependencies.*

ThisBuild / scalaVersion := "3.3.3"
ThisBuild / version      := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .settings(
    name := "hw2",
    libraryDependencies += scalatest,
    Test / javaOptions ++= Seq("-Xss1M"),
    Test / fork := true
  )
