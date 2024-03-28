//[pubnub-core-api](../../index.md)/[com.pubnub.api.v2.callbacks](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [BaseEventEmitter](-base-event-emitter/index.md) | [jvm]<br>interface [BaseEventEmitter](-base-event-emitter/index.md)&lt;[T](-base-event-emitter/index.md) : [Listener](../com.pubnub.api.callbacks/-listener/index.md)&gt; |
| [BaseEventListener](-base-event-listener/index.md) | [jvm]<br>interface [BaseEventListener](-base-event-listener/index.md) : [Listener](../com.pubnub.api.callbacks/-listener/index.md) |
| [BaseStatusEmitter](-base-status-emitter/index.md) | [jvm]<br>interface [BaseStatusEmitter](-base-status-emitter/index.md)&lt;[T](-base-status-emitter/index.md) : [BaseStatusListener](-base-status-listener/index.md)&gt; |
| [BaseStatusListener](-base-status-listener/index.md) | [jvm]<br>interface [BaseStatusListener](-base-status-listener/index.md) : [Listener](../com.pubnub.api.callbacks/-listener/index.md) |
| [Result](-result/index.md) | [jvm]<br>class [Result](-result/index.md)&lt;out [T](-result/index.md)&gt;<br>A discriminated union that encapsulates a successful outcome with a value of type [T](-result/index.md) or a failure with a [PubNubException](../com.pubnub.api/-pub-nub-exception/index.md). |

## Functions

| Name | Summary |
|---|---|
| [getOrDefault](get-or-default.md) | [jvm]<br>inline fun &lt;[R](get-or-default.md), [T](get-or-default.md) : [R](get-or-default.md)&gt; [Result](-result/index.md)&lt;[T](get-or-default.md)&gt;.[getOrDefault](get-or-default.md)(defaultValue: [R](get-or-default.md)): [R](get-or-default.md)<br>Returns the encapsulated value if this instance represents [success](-result/is-success.md) or the [defaultValue](get-or-default.md) if it is [failure](-result/is-failure.md). |
| [getOrElse](get-or-else.md) | [jvm]<br>inline fun &lt;[R](get-or-else.md), [T](get-or-else.md) : [R](get-or-else.md)&gt; [Result](-result/index.md)&lt;[T](get-or-else.md)&gt;.[getOrElse](get-or-else.md)(onFailure: (exception: [PubNubException](../com.pubnub.api/-pub-nub-exception/index.md)) -&gt; [R](get-or-else.md)): [R](get-or-else.md)<br>Returns the encapsulated value if this instance represents [success](-result/is-success.md) or the result of [onFailure](get-or-else.md) function for the encapsulated [PubNubException](../com.pubnub.api/-pub-nub-exception/index.md) exception if it is [failure](-result/is-failure.md). |
| [getOrThrow](get-or-throw.md) | [jvm]<br>fun &lt;[T](get-or-throw.md)&gt; [Result](-result/index.md)&lt;[T](get-or-throw.md)&gt;.[getOrThrow](get-or-throw.md)(): [T](get-or-throw.md)<br>Returns the encapsulated value if this instance represents [success](-result/is-success.md) or throws the encapsulated [PubNubException](../com.pubnub.api/-pub-nub-exception/index.md) if it is [failure](-result/is-failure.md). |
