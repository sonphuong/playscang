name := "playscang"

version := "2.6.0-SNAPSHOT"

scalaVersion := "2.12.4"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies += guice
libraryDependencies += jdbc
libraryDependencies += evolutions
libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1201-jdbc41"
libraryDependencies += "com.typesafe.play" %% "anorm" % "2.5.3"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.0" % Test
libraryDependencies += "javax.xml.bind" % "jaxb-api" % "2.1"