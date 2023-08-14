//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.endpoints.remoteaction](../index.md)/[RemoteAction](index.md)

# RemoteAction

[jvm]\
interface [RemoteAction](index.md)&lt;[Output](index.md)&gt; : [Cancelable](../-cancelable/index.md)

## Functions

| Name | Summary |
|---|---|
| [async](async.md) | [jvm]<br>abstract fun [async](async.md)(callback: (result: [Output](index.md)?, status: [PNStatus](../../com.pubnub.api.models.consumer/-p-n-status/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [silentCancel](../-cancelable/silent-cancel.md) | [jvm]<br>abstract fun [silentCancel](../-cancelable/silent-cancel.md)() |
| [sync](sync.md) | [jvm]<br>abstract fun [sync](sync.md)(): [Output](index.md)? |

## Inheritors

| Name |
|---|
| [ExtendedRemoteAction](../-extended-remote-action/index.md) |
