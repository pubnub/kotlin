[pubnub-kotlin](../../../index.md) / [com.pubnub.api.endpoints.remoteaction](../../index.md) / [ComposableRemoteAction](../index.md) / [ComposableBuilder](./index.md)

# ComposableBuilder

`class ComposableBuilder<T>`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ComposableBuilder(remoteAction: `[`ExtendedRemoteAction`](../../-extended-remote-action/index.md)`<T>)` |

### Functions

| Name | Summary |
|---|---|
| [checkpoint](checkpoint.md) | `fun checkpoint(): ComposableBuilder<T>` |
| [then](then.md) | `fun <U> then(factory: (T) -> `[`ExtendedRemoteAction`](../../-extended-remote-action/index.md)`<U>): `[`ComposableRemoteAction`](../index.md)`<T, U>` |
