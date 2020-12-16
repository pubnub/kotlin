[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.remoteaction](../index.md) / [MappingRemoteAction](./index.md)

# MappingRemoteAction

`class MappingRemoteAction<T, U> : ExtendedRemoteAction<U>`

### Functions

| Name | Summary |
|---|---|
| [async](async.md) | `fun async(callback: (result: U?, status: `[`PNStatus`](../../com.pubnub.api.models.consumer/-p-n-status/index.md)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [operationType](operation-type.md) | `fun operationType(): `[`PNOperationType`](../../com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](retry.md) | `fun retry(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [silentCancel](silent-cancel.md) | `fun silentCancel(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [sync](sync.md) | `fun sync(): U` |
