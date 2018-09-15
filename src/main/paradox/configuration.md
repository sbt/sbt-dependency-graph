# Usage and Configuration

## Supported sbt versions

The plugin currently supports sbt versions >= 0.13.10 and sbt 1.0.x. For versions supporting older versions of sbt see
the notes of version [0.8.2].

## Setup as global plugin

sbt-dependency-graph is an informational tool rather than one that changes your build, so you will more than likely wish to
install it as a [global plugin] so that you can use it in any SBT project without the need to explicitly add it to each one. To do
this, add the plugin dependency to 

 * `~/.sbt/0.13/plugins/plugins.sbt` for sbt 0.13, or
 * `~/.sbt/1.0/plugins/plugins.sbt` for sbt 1.0

@@@vars
```scala
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "$project.version$")
```
@@@

## Setup per project

To add the plugin only to a single project, put the above line into `project/plugins.sbt` of your project, instead.

## Configuration Settings

 * `filterScalaLibrary`: Defines if the scala library should be excluded from the output of the dependency-* functions.
   If `true`, instead of showing the dependency `"[S]"` is appended to the artifact name. Set to `false` if
   you want the scala-library dependency to appear in the output. (default: true)
 * `dependencyGraphMLFile`: a setting which allows configuring the output path of `dependencyGraphML`.
 * `dependencyDotFile`: a setting which allows configuring the output path of `dependencyDot`.
 * `dependencyDotHeader`: a setting to customize the header of the dot file (e.g. to set your preferred node shapes).
 * `dependencyDotNodeLabel`: defines the format of a node label
   (default set to `[organisation]<BR/><B>[name]</B><BR/>[version]`)

In `build.sbt` you can change configuration settings like this:

```scala
filterScalaLibrary := false // include scala library in output

dependencyDotFile := file("dependencies.dot") //render dot file to `./dependencies.dot`
```

[global plugin]: https://www.scala-sbt.org/1.x/docs/Using-Plugins.html#Global+plugins
[0.8.2]: https://github.com/jrudolph/sbt-dependency-graph/tree/v0.8.2#compatibility-notes