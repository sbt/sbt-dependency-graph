package net.virtualvoid.sbt.graph.model

import net.virtualvoid.sbt.graph.Edge
import net.virtualvoid.sbt.graph.rendering.AsciiTree
import org.specs2.mutable.Specification

class ModuleGraphTest extends Specification {

  // Sample nodes with mostly-distinct group-, name-, and version-strings
  val abc: Module = ("ABC", "abc", "1.1.1")
  val de: Module = ("DE", "de", "2.2.2")

  val fff: Module = ("FGH", "fff", "3.1.1")
  val ggg: Module = ("FGH", "ggg", "3.2.2")
  val hhh: Module = ("FGH", "hhh", "3.3.3")

  val ijk: Module = ("IJK", "ijk", "4.4.4")
  val lmn: Module = ("LMN", "lmn", "5.5.5")
  val opq: Module = ("OPQ", "opq", "6.6.6")
  val rst: Module = ("RST", "rst", "7.7.7")
  val uvw: Module = ("UVW", "uvw", "8.8.8")
  val xyz: Module = ("XYZ", "xyz", "9.9.9")

  val nodes = Seq(abc, de, fff, ggg, hhh, ijk, lmn, opq, rst, uvw, xyz)

  val edges =
    Seq[Edge](
      abc → de,
      abc → fff,
      abc → ggg,

      de → ijk,
      de → lmn,

      fff → opq,

      ijk → rst,
      ijk → uvw,

      opq → hhh,

      uvw → xyz
    )

  // Will test many inclusion/exclusion rules on this graph
  val graph = ModuleGraph(nodes, edges)

  "ModuleGraph" should {

    "show full tree" in {
      check()(
        """ABC:abc:1.1.1
          |  +-DE:de:2.2.2
          |  | +-IJK:ijk:4.4.4
          |  | | +-RST:rst:7.7.7
          |  | | +-UVW:uvw:8.8.8
          |  | |   +-XYZ:xyz:9.9.9
          |  | |   
          |  | +-LMN:lmn:5.5.5
          |  | 
          |  +-FGH:fff:3.1.1
          |  | +-OPQ:opq:6.6.6
          |  |   +-FGH:hhh:3.3.3
          |  |   
          |  +-FGH:ggg:3.2.2
          |  """
      )
    }

    "handle single include" in {
      check(
        "FGH" // "ggg" and "hhh" (including "fff") ancestries
      )(
          """ABC:abc:1.1.1
          |  +-FGH:fff:3.1.1
          |  | +-OPQ:opq:6.6.6
          |  |   +-FGH:hhh:3.3.3
          |  |   
          |  +-FGH:ggg:3.2.2
          |  """
        )
    }

    "handle multiple/partial includes" in {
      check(
        "FG", // "ggg" and "hhh" (including "fff") ancestries
        "+5.5" // "lmn" (and parent "de")
      )(
          """ABC:abc:1.1.1
          |  +-DE:de:2.2.2
          |  | +-LMN:lmn:5.5.5
          |  | 
          |  +-FGH:fff:3.1.1
          |  | +-OPQ:opq:6.6.6
          |  |   +-FGH:hhh:3.3.3
          |  |   
          |  +-FGH:ggg:3.2.2
          |  """
        )
    }

    "exclude all" in {
      check(
        "-*"
      )(
          "ABC:abc:1.1.1" // root (populated with current project) is always preserved
        )
    }

    "exclude nested" in {
      check(
        "-FGH" // only excludes "ggg"; other "FGH"-org nodes have non-excluded descendents
      )(
          """ABC:abc:1.1.1
          |  +-DE:de:2.2.2
          |  | +-IJK:ijk:4.4.4
          |  | | +-RST:rst:7.7.7
          |  | | +-UVW:uvw:8.8.8
          |  | |   +-XYZ:xyz:9.9.9
          |  | |   
          |  | +-LMN:lmn:5.5.5
          |  | 
          |  +-FGH:fff:3.1.1
          |    +-OPQ:opq:6.6.6
          |    """
        )
    }

    "exclude multiple by segments" in {
      check(
        "-FGH:", // excludes "ggg", which has no non-excluded descendents
        "-::4*", // fails to exclude "ijk", which has non-excluded descendents
        "-::9*" // excludes "xyz"
      )(
          """ABC:abc:1.1.1
          |  +-DE:de:2.2.2
          |  | +-IJK:ijk:4.4.4
          |  | | +-RST:rst:7.7.7
          |  | | +-UVW:uvw:8.8.8
          |  | | 
          |  | +-LMN:lmn:5.5.5
          |  | 
          |  +-FGH:fff:3.1.1
          |    +-OPQ:opq:6.6.6
          |    """
        )
    }

    "includes and excludes" in {
      check(
        "+:*j*", // include "ijk" (and "de" parent)
        ":*o*", // include "opq" (and "fff" parent)
        "-de", // attempt to exclude "de" (parent of included "ijk"): no-op
        "-O*:" // exclude "opq" (also excluding parent "fff")
      )(
          """ABC:abc:1.1.1
          |  +-DE:de:2.2.2
          |    +-IJK:ijk:4.4.4
          |    """
        )
    }
  }

  /**
   * Convenience conversions for specifying test-cases
   */
  implicit def moduleToId(m: Module): ModuleId = m.id
  implicit def modulesToIds(t: (Module, Module)): (ModuleId, ModuleId) = (t._1, t._2)
  implicit def tripleToModule(t: (String, String, String)): Module = Module(ModuleId(t._1, t._2, t._3))

  /**
   * Test-case helper; takes filter rules and verifies expected ASCII-output for [[graph]]
   */
  def check(filters: String*)(expected: String) = {
    AsciiTree(graph, filters.map(r ⇒ r: FilterRule): _*) === expected.stripMargin
  }
}
