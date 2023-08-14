//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.endpoints.remoteaction](../index.md)/[MappingRemoteAction](index.md)

# MappingRemoteAction

[jvm]\
class [MappingRemoteAction](index.md)&lt;[T](index.md), [U](index.md)&gt;(remoteAction: [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[T](index.md)&gt;, function: ([T](index.md)) -&gt; [U](index.md)) : [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[U](index.md)&gt;

## Constructors

| | |
|---|---|
| [MappingRemoteAction](-mapping-remote-action.md) | [jvm]<br>fun &lt;[T](index.md), [U](index.md)&gt; [MappingRemoteAction](-mapping-remote-action.md)(remoteAction: [ExtendedRemoteAction](../-extended-remote-action/index.md)&lt;[T](index.md)&gt;, function: ([T](index.md)) -&gt; [U](index.md)) |

## Functions

| Name | Summary |
|---|---|
| [async](async.md) | [jvm]<br>open override fun [async](async.md)(callback: (result: [U](index.md)?, status: [PNStatus](../../com.pubnub.api.models.consumer/-p-n-status/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [operationType](operation-type.md) | [jvm]<br>open override fun [operationType](operation-type.md)(): [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](retry.md) | [jvm]<br>open override fun [retry](retry.md)() |
| [silentCancel](silent-cancel.md) | [jvm]<br>open override fun [silentCancel](silent-cancel.md)() |
| [sync](sync.md) | [jvm]<br>open override fun [sync](sync.md)(): [U](index.md)? |
