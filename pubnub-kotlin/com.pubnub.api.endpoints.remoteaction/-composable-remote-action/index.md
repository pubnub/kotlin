//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.endpoints.remoteaction](../index.md)/[ComposableRemoteAction](index.md)

# ComposableRemoteAction

[jvm]\
class [ComposableRemoteAction](index.md)&lt;[T](index.md), [U](index.md)&gt;(remoteAction: [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[T](index.md)&gt;, createNextRemoteAction: ([T](index.md)) -&gt; [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[U](index.md)&gt;, checkpoint: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[U](index.md)&gt;

## Constructors

| | |
|---|---|
| [ComposableRemoteAction](-composable-remote-action.md) | [jvm]<br>fun &lt;[T](index.md), [U](index.md)&gt; [ComposableRemoteAction](-composable-remote-action.md)(remoteAction: [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[T](index.md)&gt;, createNextRemoteAction: ([T](index.md)) -&gt; [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[U](index.md)&gt;, checkpoint: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |
| [ComposableBuilder](-composable-builder/index.md) | [jvm]<br>class [ComposableBuilder](-composable-builder/index.md)&lt;[T](-composable-builder/index.md)&gt;(remoteAction: [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[T](-composable-builder/index.md)&gt;) |

## Functions

| Name | Summary |
|---|---|
| [async](async.md) | [jvm]<br>open override fun [async](async.md)(callback: (result: [U](index.md)?, status: [PNStatus](../../com.pubnub.api.models.consumer/-p-n-status/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [checkpoint](checkpoint.md) | [jvm]<br>@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)<br>fun [checkpoint](checkpoint.md)(): [ComposableRemoteAction](index.md)&lt;[T](index.md), [U](index.md)&gt; |
| [operationType](operation-type.md) | [jvm]<br>open override fun [operationType](operation-type.md)(): [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](retry.md) | [jvm]<br>@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)<br>open override fun [retry](retry.md)() |
| [silentCancel](silent-cancel.md) | [jvm]<br>@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)<br>open override fun [silentCancel](silent-cancel.md)() |
| [sync](sync.md) | [jvm]<br>open override fun [sync](sync.md)(): [U](index.md)? |
| [then](then.md) | [jvm]<br>fun &lt;[Y](then.md)&gt; [then](then.md)(factory: ([U](index.md)) -&gt; [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[Y](then.md)&gt;): [ComposableRemoteAction](index.md)&lt;[U](index.md), [Y](then.md)&gt; |
