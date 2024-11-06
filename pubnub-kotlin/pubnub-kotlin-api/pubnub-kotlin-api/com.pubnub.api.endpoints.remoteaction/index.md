//[pubnub-kotlin-api](../../index.md)/[com.pubnub.api.endpoints.remoteaction](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [ComposableRemoteAction](-composable-remote-action/index.md) | [jvm]<br>class [ComposableRemoteAction](-composable-remote-action/index.md)&lt;[T](-composable-remote-action/index.md), [U](-composable-remote-action/index.md)&gt;(remoteAction: [ExtendedRemoteAction](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;[T](-composable-remote-action/index.md)&gt;, createNextRemoteAction: ([T](-composable-remote-action/index.md)) -&gt; [ExtendedRemoteAction](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;[U](-composable-remote-action/index.md)&gt;, checkpoint: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : [ExtendedRemoteAction](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;[U](-composable-remote-action/index.md)&gt; |
| [MappingRemoteAction](-mapping-remote-action/index.md) | [jvm]<br>class [MappingRemoteAction](-mapping-remote-action/index.md)&lt;[T](-mapping-remote-action/index.md), [U](-mapping-remote-action/index.md)&gt;(remoteAction: [ExtendedRemoteAction](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;[T](-mapping-remote-action/index.md)&gt;, function: ([T](-mapping-remote-action/index.md)) -&gt; [U](-mapping-remote-action/index.md)) : [ExtendedRemoteAction](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;[U](-mapping-remote-action/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [map](map.md) | [jvm]<br>fun &lt;[T](map.md), [U](map.md)&gt; [ExtendedRemoteAction](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;[T](map.md)&gt;.[map](map.md)(function: ([T](map.md)) -&gt; [U](map.md)): [ExtendedRemoteAction](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;[U](map.md)&gt; |
