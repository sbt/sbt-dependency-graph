# dependencyStats

`dependencyStats` shows a table with each module a row with (transitive) Jar sizes and number of dependencies

## Notes

 * `dependencyStats` supports the @ref[toFile](index.md#tofile) subtask.
 * It will not show the jar size of the original module itself (because a jar might not have been built yet).

## Example Output

@@snip[Example output]($root$/src/sbt-test/sbt-dependency-graph/toFileSubTask/expected/stats.txt)