scalaVersion := (CrossVersion partialVersion (sbtVersion in pluginCrossBuild).value match {
  case Some((0, 13)) => "2.10.6"
  case Some((1, _))  => "2.12.2"
  case _             => sys error s"Unhandled sbt version ${(sbtVersion in pluginCrossBuild).value}"
})

ScriptedPlugin.scriptedSettings

libraryDependencies += "com.github.mdr" % "ascii-graphs_2.10" % "0.0.3"

libraryDependencies += "org.specs2" %% "specs2-core" % "3.9.4" % "test"

crossSbtVersions := Vector("0.13.16", "1.0.0")

scalacOptions ++= Seq("-deprecation", "-unchecked")

ScalariformSupport.formatSettings
