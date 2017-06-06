package net.virtualvoid.sbt.graph.rendering

import net.virtualvoid.sbt.graph.{ Module, ModuleGraph }

import scala.util.parsing.json.{ JSONArray, JSONObject }

object JsonTree {

  def jsonModule(module: Module)(children: Seq[JSONObject]): JSONObject =
    new JSONObject(Map(
      "module" -> module.id.name,
      "group" -> module.id.organisation,
      "version" -> module.id.version,
      "evicted" -> module.isEvicted,
      "errors" -> module.error.getOrElse("None"),
      "license" -> module.license.getOrElse("Unknown"),
      "children" -> new JSONArray(children.toList)))

  private def postOrder[T](p: Module ⇒ Boolean)(f: Module ⇒ Seq[T] ⇒ T)(graph: ModuleGraph)(module: Module): T = {
    val children = graph.dependencyMap.getOrElse(module.id, Seq()).filter(p).map(postOrder(p)(f)(graph))
    f(module)(children)
  }

  def json(graph: ModuleGraph, filter: Module ⇒ Boolean): JSONArray =
    new JSONArray(graph.roots.map(postOrder(filter)(jsonModule)(graph)(_)).toList)
}
