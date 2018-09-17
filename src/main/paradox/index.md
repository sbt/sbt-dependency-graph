# sbt-dependency-graph

[![Join the chat at https://gitter.im/jrudolph/sbt-dependency-graph](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/jrudolph/sbt-dependency-graph?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

An sbt plugin to visualize your project's dependencies.

@@ toc { depth=1 }

## Quickstart

Use this line in `~/.sbt/1.0/plugins/plugins.sbt` to install globally or in your project's `project/plugins.sbt`:

@@@vars
```scala
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "$project.version$")
```
@@@

## Example Output

@ref[dependencyBrowseGraph](tasks/dependencyBrowseGraph.md):
  ![dependencyBrowseGraph in action](https://gist.githubusercontent.com/jrudolph/941754bcf67a0fafe495/raw/7d80d766feb7af6ba2a69494e1f3ceb1fd40d4da/Screenshot%2520from%25202015-11-26%252014:18:19.png)

@ref[dependencyBrowseTree](tasks/dependencyBrowseTree.md):

![dependencyBrowseTree in action](img/dependencyBrowseTreeExample.png)

@ref[dependencyTree](tasks/dependencyTree.md):

@@snip[Example output]($root$/src/sbt-test/sbt-dependency-graph/toFileSubTask/expected/tree.txt)

@ref[whatDependsOn](tasks/whatDependsOn.md):

<script src="https://asciinema.org/a/uCm9gsowJAuCLRyRybnYHVFJ7.js" id="asciicast-uCm9gsowJAuCLRyRybnYHVFJ7" data-t="12" data-theme="solarized-dark" async></script>

@ref[dependencyList](tasks/dependencyList.md):

@@snip[Example output]($root$/src/sbt-test/sbt-dependency-graph/toFileSubTask/expected/list.txt)

@ref[dependencyStats](tasks/dependencyStats.md):

@@snip[Example output]($root$/src/sbt-test/sbt-dependency-graph/toFileSubTask/expected/stats.txt)

@ref[dependencyLicenseInfo](tasks/dependencyLicenseInfo.md):

@@snip[Example output]($root$/src/sbt-test/sbt-dependency-graph/toFileSubTask/expected/licenses.txt)



@@@ index

 * [Configuration](configuration.md)
 * [Tasks](tasks/index.md)
 * [Changelog](changelog.md)
 * [License](license.md)

@@@