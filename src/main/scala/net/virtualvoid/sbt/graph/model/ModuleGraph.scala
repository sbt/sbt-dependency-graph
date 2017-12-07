/*
 * Copyright 2015 Johannes Rudolph
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.virtualvoid.sbt.graph.model

import java.io.File

import net.virtualvoid.sbt.graph.model.ModuleGraph.DepMap
import net.virtualvoid.sbt.graph.{ Edge, ModuleGraphProtocolCompat }
import sbinary.Format

import scala.collection.mutable
import scala.collection.mutable.{ HashMap, MultiMap, Set }

case class ModuleId(organisation: String,
                    name: String,
                    version: String) {
  def idString: String = organisation + ":" + name + ":" + version
}
case class Module(id: ModuleId,
                  license: Option[String] = None,
                  extraInfo: String = "",
                  evictedByVersion: Option[String] = None,
                  jarFile: Option[File] = None,
                  error: Option[String] = None) {
  def hadError: Boolean = error.isDefined
  def isUsed: Boolean = !isEvicted
  def isEvicted: Boolean = evictedByVersion.isDefined
}

object ModuleGraph {
  val empty = ModuleGraph(Seq.empty, Seq.empty)

  type DepMap = Map[ModuleId, Seq[Module]]
}

case class ModuleGraph(nodes: Seq[Module], edges: Seq[Edge]) {
  lazy val modules: Map[ModuleId, Module] =
    nodes.map(n ⇒ (n.id, n)).toMap

  def module(id: ModuleId): Module = modules(id)

  lazy val dependencyMap: DepMap =
    createMap(identity)

  lazy val reverseDependencyMap: DepMap =
    createMap { case (a, b) ⇒ (b, a) }

  def createMap(bindingFor: ((ModuleId, ModuleId)) ⇒ (ModuleId, ModuleId)): DepMap = {
    val m = new HashMap[ModuleId, Set[Module]] with MultiMap[ModuleId, Module]
    edges.foreach { entry ⇒
      val (f, t) = bindingFor(entry)
      m.addBinding(f, module(t))
    }
    m.toMap.mapValues(_.toSeq.sortBy(_.id.idString)).withDefaultValue(Nil)
  }

  def roots: Seq[Module] =
    nodes.filter(n ⇒ !edges.exists(_._2 == n.id)).sortBy(_.id.idString)

  def filter(rules: FilterRule*): DepMap = {
    val map = mutable.Map[ModuleId, Option[Seq[Module]]]()

    // Are any include- (resp. exclude-) rules present in `rules`?
    var (hasIncludes, hasExcludes) = (false, false)
    rules foreach {
      case _: Include ⇒ hasIncludes = true
      case _: Exclude ⇒ hasExcludes = true
    }

    // Does a given ID satisfy at least one include (if any are present?)
    def matchesInclude(id: ModuleId): Boolean =
      !hasIncludes ||
        rules.exists {
          case inc: Include if inc(id) ⇒ true
          case _                       ⇒ false
        }

    // Is a given ID excluded? 
    def matchesExcludes(id: ModuleId): Boolean =
      !hasExcludes ||
        !rules.exists {
          case exc: Exclude if exc(id) ⇒ true
          case _                       ⇒ false
        }

    // Keep an ID iff it satisfies at least one include (if any have been provided), and is not excluded
    def keep(id: ModuleId): Boolean = matchesInclude(id) && matchesExcludes(id)

    def filtered(id: ModuleId): Option[Seq[Module]] =
      map.getOrElseUpdate(
        id,
        dependencyMap(id)
          .filter {
            dep ⇒
              filtered(dep.id).isDefined
          } match {
            case Seq() if !keep(id) ⇒
              // none of this module's deps passed all rules (or depend on anything that does), nor did this module
              // itself; drop it
              None
            case filteredDeps ⇒
              // Keep this module and its valid deps (which may be empty
              Some(filteredDeps)
          })

    dependencyMap foreach {
      case (id, _) ⇒ filtered(id)
    }

    map
      .flatMap {
        case (id, Some(deps)) ⇒ Some(id → deps)
        case _                ⇒ None
      }
      .toMap
  }
}

object ModuleGraphProtocol extends ModuleGraphProtocolCompat {
  import sbinary.DefaultProtocol._

  implicit def seqFormat[T: Format]: Format[Seq[T]] = wrap[Seq[T], List[T]](_.toList, _.toSeq)
  implicit val ModuleIdFormat: Format[ModuleId] = asProduct3(ModuleId)(ModuleId.unapply(_).get)
  implicit val ModuleFormat: Format[Module] = asProduct6(Module)(Module.unapply(_).get)
  implicit val ModuleGraphFormat: Format[ModuleGraph] = asProduct2(ModuleGraph.apply)(ModuleGraph.unapply(_).get)
}
