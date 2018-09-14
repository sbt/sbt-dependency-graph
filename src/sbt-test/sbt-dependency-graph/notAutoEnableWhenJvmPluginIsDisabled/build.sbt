disablePlugins(IvyPlugin)

TaskKey[Unit]("check") := Def.task {
  val autoPlugins = thisProject.value.autoPlugins
  require(
    !autoPlugins.contains(DependencyGraphPlugin),
    s"DependencyGraphPlugin is auto-enabled! All auto-enabled plugins: ${autoPlugins.mkString(", ")}"
  )
}.value
