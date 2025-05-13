//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[Result](index.md)

# Result

[common]\
class [Result](index.md)&lt;out [T](index.md)&gt;

A discriminated union that encapsulates a successful outcome with a value of type [T](index.md) or a failure with a [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md).

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md)<br>Companion object for [Result](index.md) class that contains its constructor functions [success](-companion/success.md) and [failure](-companion/failure.md). |

## Properties

| Name | Summary |
|---|---|
| [isFailure](is-failure.md) | [common]<br>val [isFailure](is-failure.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Returns `true` if this instance represents a failed outcome. In this case [isSuccess](is-success.md) returns `false`. |
| [isSuccess](is-success.md) | [common]<br>val [isSuccess](is-success.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Returns `true` if this instance represents a successful outcome. In this case [isFailure](is-failure.md) returns `false`. |

## Functions

| Name | Summary |
|---|---|
| [exceptionOrNull](exception-or-null.md) | [common]<br>fun [exceptionOrNull](exception-or-null.md)(): [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)?<br>Returns the encapsulated [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md) exception if this instance represents [failure](is-failure.md) or `null` if it is [success](is-success.md). |
| [fold](../fold.md) | [common]<br>inline fun &lt;[R](../fold.md), [T](../fold.md)&gt; [Result](index.md)&lt;[T](../fold.md)&gt;.[fold](../fold.md)(onSuccess: (value: [T](../fold.md)) -&gt; [R](../fold.md), onFailure: (exception: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)) -&gt; [R](../fold.md)): [R](../fold.md)<br>Returns the result of [onSuccess](../fold.md) for the encapsulated value if this instance represents [success](is-success.md) or the result of [onFailure](../fold.md) function for the encapsulated [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html) exception if it is [failure](is-failure.md). |
| [getOrDefault](../get-or-default.md) | [common]<br>inline fun &lt;[R](../get-or-default.md), [T](../get-or-default.md) : [R](../get-or-default.md)&gt; [Result](index.md)&lt;[T](../get-or-default.md)&gt;.[getOrDefault](../get-or-default.md)(defaultValue: [R](../get-or-default.md)): [R](../get-or-default.md)<br>Returns the encapsulated value if this instance represents [success](is-success.md) or the [defaultValue](../get-or-default.md) if it is [failure](is-failure.md). |
| [getOrElse](../get-or-else.md) | [common]<br>inline fun &lt;[R](../get-or-else.md), [T](../get-or-else.md) : [R](../get-or-else.md)&gt; [Result](index.md)&lt;[T](../get-or-else.md)&gt;.[getOrElse](../get-or-else.md)(onFailure: (exception: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)) -&gt; [R](../get-or-else.md)): [R](../get-or-else.md)<br>Returns the encapsulated value if this instance represents [success](is-success.md) or the result of [onFailure](../get-or-else.md) function for the encapsulated [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md) exception if it is [failure](is-failure.md). |
| [getOrNull](get-or-null.md) | [common]<br>inline fun [getOrNull](get-or-null.md)(): [T](index.md)?<br>Returns the encapsulated value if this instance represents [success](is-success.md) or `null` if it is [failure](is-failure.md). |
| [getOrThrow](../get-or-throw.md) | [common]<br>fun &lt;[T](../get-or-throw.md)&gt; [Result](index.md)&lt;[T](../get-or-throw.md)&gt;.[getOrThrow](../get-or-throw.md)(): [T](../get-or-throw.md)<br>Returns the encapsulated value if this instance represents [success](is-success.md) or throws the encapsulated [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md) if it is [failure](is-failure.md). |
| [map](../map.md) | [common]<br>inline fun &lt;[R](../map.md), [T](../map.md)&gt; [Result](index.md)&lt;[T](../map.md)&gt;.[map](../map.md)(transform: (value: [T](../map.md)) -&gt; [R](../map.md)): [Result](index.md)&lt;[R](../map.md)&gt;<br>Returns the encapsulated result of the given [transform](../map.md) function applied to the encapsulated value if this instance represents [success](is-success.md) or the original encapsulated [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html) exception if it is [failure](is-failure.md). |
| [mapCatching](../map-catching.md) | [common]<br>inline fun &lt;[R](../map-catching.md), [T](../map-catching.md)&gt; [Result](index.md)&lt;[T](../map-catching.md)&gt;.[mapCatching](../map-catching.md)(transform: (value: [T](../map-catching.md)) -&gt; [R](../map-catching.md)): [Result](index.md)&lt;[R](../map-catching.md)&gt; |
| [onFailure](on-failure.md) | [common]<br>inline fun [onFailure](on-failure.md)(action: [Consumer](../-consumer/index.md)&lt;[PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)&gt;): [Result](index.md)&lt;[T](index.md)&gt; |
| [onSuccess](on-success.md) | [common]<br>inline fun [onSuccess](on-success.md)(action: [Consumer](../-consumer/index.md)&lt;in [T](index.md)&gt;): [Result](index.md)&lt;[T](index.md)&gt; |
| [toString](to-string.md) | [common]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>Returns a string `Success(v)` if this instance represents [success](is-success.md) where `v` is a string representation of the value or a string `Failure(x)` if it is [failure](is-failure.md) where `x` is a string representation of the exception. |
| [wrapException](../wrap-exception.md) | [common]<br>inline fun &lt;[T](../wrap-exception.md)&gt; [Result](index.md)&lt;[T](../wrap-exception.md)&gt;.[wrapException](../wrap-exception.md)(block: ([PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)) -&gt; [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)): [Result](index.md)&lt;[T](../wrap-exception.md)&gt; |
