[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.remoteaction](../index.md) / [ComposableRemoteAction](./index.md)

# ComposableRemoteAction

`class ComposableRemoteAction<T, U> : `[`ExtendedRemoteAction`](../-extended-remote-action/index.md)`<U>`

### Types

| Name | Summary |
|---|---|
| [ComposableBuilder](-composable-builder/index.md) | `class ComposableBuilder<T>` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ComposableRemoteAction(remoteAction: `[`ExtendedRemoteAction`](../-extended-remote-action/index.md)`<T>, createNextRemoteAction: (T) -> `[`ExtendedRemoteAction`](../-extended-remote-action/index.md)`<U>, checkpoint: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`)` |

### Functions

| Name | Summary |
|---|---|
| [async](async.md) | `fun async(callback: (result: U?, status: `[`PNStatus`](../../com.pubnub.api.models.consumer/-p-n-status/index.md)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [checkpoint](checkpoint.md) | `fun checkpoint(): `[`ComposableRemoteAction`](./index.md)`<T, U>` |
| [operationType](operation-type.md) | `fun operationType(): `[`PNOperationType`](../../com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](retry.md) | `fun retry(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [silentCancel](silent-cancel.md) | `fun silentCancel(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [sync](sync.md) | `fun sync(): U?` |
| [then](then.md) | `fun <Y> then(factory: (U) -> `[`ExtendedRemoteAction`](../-extended-remote-action/index.md)`<Y>): `[`ComposableRemoteAction`](./index.md)`<U, Y>` |

### Companion Object Functions

| Name | Summary |
|---|---|
| [firstDo](first-do.md) | `fun <T> firstDo(remoteAction: `[`ExtendedRemoteAction`](../-extended-remote-action/index.md)`<T>): ComposableBuilder<T>` |
