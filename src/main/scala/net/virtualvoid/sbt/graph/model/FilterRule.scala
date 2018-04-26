package net.virtualvoid.sbt.graph.model

import scala.language.implicitConversions
import scala.util.matching.Regex

/**
 * Interface for [[Include]] and [[Exclude]] rules for filtering the output of the
 * [[net.virtualvoid.sbt.graph.DependencyGraphKeys.dependencyTree dependencyTree]] task
 */
sealed trait FilterRule {
  def pattern: ModuleIdPattern
  def apply(node: ModuleId): Boolean = pattern(node)
}

object FilterRule {
  implicit def apply(s: String): FilterRule =
    if (s.startsWith("-"))
      Exclude(s.substring(1))
    else if (s.startsWith("+"))
      Include(s.substring(1))
    else
      Include(s)
}

/**
 * If a [[ModuleId]] matches [[pattern]], include it in results
 */
case class Include(pattern: ModuleIdPattern) extends FilterRule
object Include {
  def apply(pattern: String): Include = Include(ModuleIdPattern(pattern))
}

/**
 * If a [[ModuleId]] matches [[pattern]], exclude it from results
 */
case class Exclude(pattern: ModuleIdPattern) extends FilterRule
object Exclude {
  def apply(pattern: String): Exclude = Exclude(ModuleIdPattern(pattern))
}

/**
 * Interface for patterns that can be matched against [[ModuleId]]s, either based on patterns that fully match one or
 * more of the (group, artifact, version) segments ([[PerSegmentPatterns]]), or that partially match any of them
 * ([[AnySegmentPattern]])
 */
sealed trait ModuleIdPattern {
  /**
   * Does this [[ModuleId]] match this pattern?
   */
  def apply(moduleId: ModuleId): Boolean
}

object ModuleIdPattern {

  /**
   * Build a [[Regex]] from a [[String]] specifying a module-id pattern:
   *
   * - escape "dot" (`.`) characters
   * - replace "glob"/stars with `.*`
   *
   * This is more ergonomic than PCRE for Maven coordinates, since literal dots are used frequently, and other
   * regex-special-chars (e.g. `[`/`]`, `{`/`}`, `+`) are not.
   */
  def regex(s: String): Regex =
    if (s.isEmpty)
      any
    else
      s
        .replace(".", "\\.")
        .replace("*", ".*")
        .r

  private val any = ".*".r

  /**
   * Generate a [[ModuleIdPattern]] from a loose Maven-coordinate-style specification:
   *
   * - "foo": coordinates whose group, artifact, or version contains "foo"
   * - "foo:": coordinates whose group is exactly "foo"
   * - "foo*": coordinates whose group begins with "foo"
   * - "*foo": coordinates whose group ends with "foo"
   * - "*foo*": coordinates whose group contains "foo"
   * - ":foo", ":foo:": coordinates whose name is "foo"
   * - ":foo*": coordinates whose name begins with "foo"
   * - "::*foo": coordinates whose version ends with "foo"
   * - "foo*::*bar*": coordinates whose group begins with "foo" and version contains "bar"
   */
  def apply(s: String): ModuleIdPattern =
    s.split(":", -1) match {
      // encodes a partial-match against any segment
      case Array(_)                    ⇒ AnySegmentPattern(regex(s))

      // encode full-matches against all segments with non-empty patterns
      case Array(group, name)          ⇒ PerSegmentPatterns(regex(group), regex(name), any)
      case Array(group, name, version) ⇒ PerSegmentPatterns(regex(group), regex(name), regex(version))
    }
}

/**
 * Match [[ModuleId]]s whose group, name, and version each fully-match the corresponding provided [[Regex]]
 */
case class PerSegmentPatterns(group: Regex,
                              name: Regex,
                              version: Regex)
    extends ModuleIdPattern {
  def apply(moduleId: ModuleId): Boolean =
    moduleId match {
      case ModuleId(group(), name(), version()) ⇒
        true
      case _ ⇒
        false
    }
}

/**
 * Match [[ModuleId]]s where at least one of their {group, name, version} partially-match [[regex]]
 */
case class AnySegmentPattern(regex: Regex)
    extends ModuleIdPattern {
  def apply(m: ModuleId): Boolean =
    Seq(
      m.organisation,
      m.name,
      m.version
    )
      .exists(
        regex
          .findFirstIn(_)
          .isDefined
      )
}
