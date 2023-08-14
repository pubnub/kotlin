//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.endpoints.remoteaction](../index.md)/[ExtendedRemoteAction](index.md)

# ExtendedRemoteAction

[jvm]\
interface [ExtendedRemoteAction](index.md)&lt;[Output](index.md)&gt; : [RemoteAction](../-remote-action/index.md)&lt;[Output](index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [async](../-remote-action/async.md) | [jvm]<br>abstract fun [async](../-remote-action/async.md)(callback: (result: [Output](index.md)?, status: [PNStatus](../../com.pubnub.api.models.consumer/-p-n-status/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [operationType](operation-type.md) | [jvm]<br>abstract fun [operationType](operation-type.md)(): [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](retry.md) | [jvm]<br>abstract fun [retry](retry.md)() |
| [silentCancel](../-cancelable/silent-cancel.md) | [jvm]<br>abstract fun [silentCancel](../-cancelable/silent-cancel.md)() |
| [sync](../-remote-action/sync.md) | [jvm]<br>abstract fun [sync](../-remote-action/sync.md)(): [Output](index.md)? |

## Inheritors

| Name |
|---|
| [Endpoint](../../com.pubnub.api/-endpoint/index.md) |
| [SendFile](../../com.pubnub.api.endpoints.files/-send-file/index.md) |
| [ComposableRemoteAction](../-composable-remote-action/index.md) |
| [MappingRemoteAction](../-mapping-remote-action/index.md) |

## Extensions

| Name | Summary |
|---|---|
| [map](../map.md) | [jvm]<br>fun &lt;[T](../map.md), [U](../map.md)&gt; [ExtendedRemoteAction](index.md)&lt;[T](../map.md)&gt;.[map](../map.md)(function: ([T](../map.md)) -&gt; [U](../map.md)): [ExtendedRemoteAction](index.md)&lt;[U](../map.md)&gt; |
