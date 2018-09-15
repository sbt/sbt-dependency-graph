# Tasks

## General Information

### Scoping by Configuration

All of the tasks are scoped by configuration. E.g. you can try `test:dependencyTree` to show only test dependencies or
`runtime:dependencyBrowseGraph` to open a browser window with the runtime dependencies.

If no configuration is specified the default configuration is `compile`.

### Subtasks

Some tasks that would normally print information to the console may also have a subtask that allows alternative
outputs. Currently, only printing to to a file is supported.

Tasks that support the subtask syntax are:

 * @ref[dependencyTree](dependencyTree.md)
 * @ref[dependencyList](dependencyList.md)
 * @ref[dependencyStats](dependencyStats.md)
 * @ref[dependencyLicenseInfo](dependencyLicenseInfo.md)
 
On the sbt console the syntax is `<configuration>:<task>::<subtask>`.


#### toFile
 
The `toFile` subtask allows saving the output directly to a file.

The syntax is `<configuration>:<task>::toFile <target filename> <-f|--force>?`. You can add the `--force`
parameter if you want to overwrite the target file in case it already exists.

Example:

```
dependencyTree::toFile target/tree.txt
```

will write the output of `dependencyTree` directly to `target/tree.txt`.

### Full List of Tasks

@@toc { depth=1 }

@@@ index

 * [a](dependencyBrowseGraph.md)
 * [a](dependencyTree.md)
 * [a](dependencyBrowseTree.md)
 * [a](dependencyList.md)
 * [a](dependencyStats.md)
 * [a](dependencyLicenseInfo.md)
 * [a](dependencyDot.md)
 * [a](dependencyGraphML.md)
 * [a](whatDependsOn.md)
 * [a](deprecated-tasks.md)

@@@