package net.virtualvoid.sbt.graph

object Csv {
  def toCsv(graph: IvyGraphMLDependencies.ModuleGraph):String = {
    graph.modules.toList.sortBy { case (id, module) =>
        (id.organisation, id.name)
      }
//      .map { case (id, module) =>
//
//      }
      .map(_._1.idString).mkString("\n")
  }
}
