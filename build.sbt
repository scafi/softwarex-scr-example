import sbt.librarymanagement

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.4"

lazy val root = (project in file("."))
  .settings(
    name := "scafi-softwarex-program-showcase",
    libraryDependencies ++= Seq(
      ("it.unibo.scafi" %% "simulator-gui-new" % "1.1.5"),
      "org.scala-lang" % "scala-compiler" % scalaVersion.value,
      "org.scalanlp" %% "breeze" % "2.0.1-RC1",
      "org.scalanlp" %% "breeze-viz" % "2.0.1-RC1"
    )
  )
