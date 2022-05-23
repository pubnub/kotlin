[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.remoteaction](../index.md) / [ExtendedRemoteAction](./index.md)

# ExtendedRemoteAction

`interface ExtendedRemoteAction<Output> : `[`RemoteAction`](../-remote-action/index.md)`<Output>`

### Functions

| Name | Summary |
|---|---|
| [operationType](operation-type.md) | `abstract fun operationType(): `[`PNOperationType`](../../com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](retry.md) | `abstract fun retry(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [ComposableRemoteAction](../-composable-remote-action/index.md) | `class ComposableRemoteAction<T, U> : `[`ExtendedRemoteAction`](./index.md)`<U>` |
| [Endpoint](../../com.pubnub.api/-endpoint/index.md) | Base class for all PubNub API operation implementations.`abstract class Endpoint<Input, Output> : `[`ExtendedRemoteAction`](./index.md)`<Output>` |
| [MappingRemoteAction](../-mapping-remote-action/index.md) | `class MappingRemoteAction<T, U> : `[`ExtendedRemoteAction`](./index.md)`<U>` |
| [SendFile](../../com.pubnub.api.endpoints.files/-send-file/index.md) | `class SendFile : `[`ExtendedRemoteAction`](./index.md)`<`[`PNFileUploadResult`](../../com.pubnub.api.models.consumer.files/-p-n-file-upload-result/index.md)`>` |
