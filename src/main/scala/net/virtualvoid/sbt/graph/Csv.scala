package net.virtualvoid.sbt.graph

object Csv {
  def toCsv(graph: IvyGraphMLDependencies.ModuleGraph):String = {
    graph.modules.toList.sortBy { case (id, module) =>
        (id.organisation, id.name)
      }
      .filter { case (id, module) =>
        module.evictedByVersion.isEmpty
      }
      .map { case (id, module) =>
        val license = '"'+module.license.getOrElse("")+'"' // Surround with quotes because some have commas
        List(id.organisation, id.name, id.version, license)
          .mkString(",")
      }
      .mkString("\n")
  }
}
