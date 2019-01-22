package sbt
package dependencygraph

import scala.util.Try
import scala.language.implicitConversions

object DependencyGraphSbtCompat {
  object Implicits {
    implicit def convertConfig(config: sbt.Configuration): String = config.toString

    implicit class RichUpdateConfiguration(val updateConfig: UpdateConfiguration) extends AnyVal {
      def withMissingOk(missingOk: Boolean): UpdateConfiguration =
        updateConfig.copy(missingOk = missingOk)
    }
  }

  object Settings {
    private val sbtAsciiGraphWidth = "SBT_ASCII_GRAPH_WIDTH"

    val asciiGraphMaxColumnWidth = Def.setting(defaultColumnSize)

    private def defaultColumnSize: Int = {
      val envAsciiWidth = sys.env.get(sbtAsciiGraphWidth).flatMap(s ⇒ Try(s.toInt).toOption)
      val termWidth = envAsciiWidth.getOrElse(SbtAccess.getTerminalWidth)
      if (termWidth > 20) termWidth - 8
      else 80 // ignore termWidth
    }

  }
}
