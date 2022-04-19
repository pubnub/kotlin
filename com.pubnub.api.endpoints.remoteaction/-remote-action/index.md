[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.remoteaction](../index.md) / [RemoteAction](./index.md)

# RemoteAction

`interface RemoteAction<Output> : `[`Cancelable`](../-cancelable/index.md)

### Functions

| Name | Summary |
|---|---|
| [async](async.md) | `abstract fun async(callback: (result: Output?, status: `[`PNStatus`](../../com.pubnub.api.models.consumer/-p-n-status/index.md)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [sync](sync.md) | `abstract fun sync(): Output?` |
