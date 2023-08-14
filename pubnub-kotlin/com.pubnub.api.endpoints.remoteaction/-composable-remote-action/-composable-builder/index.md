//[pubnub-kotlin](../../../../index.md)/[com.pubnub.api.endpoints.remoteaction](../../index.md)/[ComposableRemoteAction](../index.md)/[ComposableBuilder](index.md)

# ComposableBuilder

[jvm]\
class [ComposableBuilder](index.md)&lt;[T](index.md)&gt;(remoteAction: [ExtendedRemoteAction](../../-extended-remote-action/index.md)&lt;[T](index.md)&gt;)

## Constructors

| | |
|---|---|
| [ComposableBuilder](-composable-builder.md) | [jvm]<br>fun &lt;[T](index.md)&gt; [ComposableBuilder](-composable-builder.md)(remoteAction: [ExtendedRemoteAction](../../-extended-remote-action/index.md)&lt;[T](index.md)&gt;) |

## Functions

| Name | Summary |
|---|---|
| [checkpoint](checkpoint.md) | [jvm]<br>fun [checkpoint](checkpoint.md)(): [ComposableRemoteAction.ComposableBuilder](index.md)&lt;[T](index.md)&gt; |
| [then](then.md) | [jvm]<br>fun &lt;[U](then.md)&gt; [then](then.md)(factory: ([T](index.md)) -&gt; [ExtendedRemoteAction](../../-extended-remote-action/index.md)&lt;[U](then.md)&gt;): [ComposableRemoteAction](../index.md)&lt;[T](index.md), [U](then.md)&gt; |
