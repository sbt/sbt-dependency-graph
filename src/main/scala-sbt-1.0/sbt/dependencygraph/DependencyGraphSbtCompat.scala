package sbt
package dependencygraph

object DependencyGraphSbtCompat {
  object Implicits

  object Settings {
    val asciiGraphMaxColumnWidth = Def.setting(sbt.Keys.asciiGraphWidth.value)
  }
}
