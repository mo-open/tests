name := "ss_collector"

version := "1.0"

lazy val `ss_collector` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(jdbc, anorm, cache, ws)

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")