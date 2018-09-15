libraryDependencies += "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value

// code formatting
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.2")

// releasing
addSbtPlugin("com.dwijnand"     % "sbt-dynver"      % "2.0.0")

// docs
addSbtPlugin("com.lightbend.paradox" % "sbt-paradox" % "0.4.2")
addSbtPlugin("io.github.jonas" % "sbt-paradox-material-theme" % "0.4.0")
