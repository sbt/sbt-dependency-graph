# dependencyBrowseGraph

`dependencyBrowseGraph` will create a local web page that shows a zoomable graph representation of your dependencies. It will
automatically open a browser on this page.

## Example Output

![dependencyBrowseGraph in action](https://gist.githubusercontent.com/jrudolph/941754bcf67a0fafe495/raw/7d80d766feb7af6ba2a69494e1f3ceb1fd40d4da/Screenshot%2520from%25202015-11-26%252014:18:19.png)

It is implemented by creating `dot` output for the dependency graph and then uses [graphlib-dot] and [dagre-d3] to
render it in a web page.

[graphlib-dot]: https://github.com/dagrejs/graphlib-dot
[dagre-d3]: https://github.com/dagrejs/dagre-d3