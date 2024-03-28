//[pubnub-core-api](../../index.md)/[com.pubnub.api.endpoints.remoteaction](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [Cancelable](-cancelable/index.md) | [jvm]<br>interface [Cancelable](-cancelable/index.md) |
| [ComposableRemoteAction](-composable-remote-action/index.md) | [jvm]<br>class [ComposableRemoteAction](-composable-remote-action/index.md)&lt;[T](-composable-remote-action/index.md), [U](-composable-remote-action/index.md)&gt;(remoteAction: [ExtendedRemoteAction](-extended-remote-action/index.md)&lt;[T](-composable-remote-action/index.md)&gt;, createNextRemoteAction: ([T](-composable-remote-action/index.md)) -&gt; [ExtendedRemoteAction](-extended-remote-action/index.md)&lt;[U](-composable-remote-action/index.md)&gt;, checkpoint: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : [ExtendedRemoteAction](-extended-remote-action/index.md)&lt;[U](-composable-remote-action/index.md)&gt; |
| [ExtendedRemoteAction](-extended-remote-action/index.md) | [jvm]<br>interface [ExtendedRemoteAction](-extended-remote-action/index.md)&lt;[Output](-extended-remote-action/index.md)&gt; : [RemoteAction](-remote-action/index.md)&lt;[Output](-extended-remote-action/index.md)&gt; |
| [MappingRemoteAction](-mapping-remote-action/index.md) | [jvm]<br>class [MappingRemoteAction](-mapping-remote-action/index.md)&lt;[T](-mapping-remote-action/index.md), [U](-mapping-remote-action/index.md)&gt;(remoteAction: [ExtendedRemoteAction](-extended-remote-action/index.md)&lt;[T](-mapping-remote-action/index.md)&gt;, function: ([T](-mapping-remote-action/index.md)) -&gt; [U](-mapping-remote-action/index.md)) : [ExtendedRemoteAction](-extended-remote-action/index.md)&lt;[U](-mapping-remote-action/index.md)&gt; |
| [RemoteAction](-remote-action/index.md) | [jvm]<br>interface [RemoteAction](-remote-action/index.md)&lt;[Output](-remote-action/index.md)&gt; : [Cancelable](-cancelable/index.md) |

## Functions

| Name | Summary |
|---|---|
| [map](map.md) | [jvm]<br>fun &lt;[T](map.md), [U](map.md)&gt; [ExtendedRemoteAction](-extended-remote-action/index.md)&lt;[T](map.md)&gt;.[map](map.md)(function: ([T](map.md)) -&gt; [U](map.md)): [ExtendedRemoteAction](-extended-remote-action/index.md)&lt;[U](map.md)&gt; |
