package net.virtualvoid.sbt.graph

object Csv {
  def toCsv(graph: IvyGraphMLDependencies.ModuleGraph, excludeOrg:Option[String]):String = {
    graph.modules.toList.sortBy { case (id, module) =>
        (id.organisation, id.name)
      }
      .filterNot { case (id, module) =>
        Some(id.organisation) == excludeOrg
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
