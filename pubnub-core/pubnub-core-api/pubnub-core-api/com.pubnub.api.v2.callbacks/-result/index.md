//[pubnub-core-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[Result](index.md)

# Result

[jvm]\
class [Result](index.md)&lt;out [T](index.md)&gt;

A discriminated union that encapsulates a successful outcome with a value of type [T](index.md) or a failure with a [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md).

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md)<br>Companion object for [Result](index.md) class that contains its constructor functions [success](-companion/success.md) and [failure](-companion/failure.md). |

## Properties

| Name | Summary |
|---|---|
| [isFailure](is-failure.md) | [jvm]<br>val [isFailure](is-failure.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns `true` if this instance represents a failed outcome. In this case [isSuccess](is-success.md) returns `false`. |
| [isSuccess](is-success.md) | [jvm]<br>val [isSuccess](is-success.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns `true` if this instance represents a successful outcome. In this case [isFailure](is-failure.md) returns `false`. |

## Functions

| Name | Summary |
|---|---|
| [exceptionOrNull](exception-or-null.md) | [jvm]<br>fun [exceptionOrNull](exception-or-null.md)(): [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)?<br>Returns the encapsulated [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md) exception if this instance represents [failure](is-failure.md) or `null` if it is [success](is-success.md). |
| [getOrDefault](../get-or-default.md) | [jvm]<br>inline fun &lt;[R](../get-or-default.md), [T](../get-or-default.md) : [R](../get-or-default.md)&gt; [Result](index.md)&lt;[T](../get-or-default.md)&gt;.[getOrDefault](../get-or-default.md)(defaultValue: [R](../get-or-default.md)): [R](../get-or-default.md)<br>Returns the encapsulated value if this instance represents [success](is-success.md) or the [defaultValue](../get-or-default.md) if it is [failure](is-failure.md). |
| [getOrElse](../get-or-else.md) | [jvm]<br>inline fun &lt;[R](../get-or-else.md), [T](../get-or-else.md) : [R](../get-or-else.md)&gt; [Result](index.md)&lt;[T](../get-or-else.md)&gt;.[getOrElse](../get-or-else.md)(onFailure: (exception: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)) -&gt; [R](../get-or-else.md)): [R](../get-or-else.md)<br>Returns the encapsulated value if this instance represents [success](is-success.md) or the result of [onFailure](../get-or-else.md) function for the encapsulated [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md) exception if it is [failure](is-failure.md). |
| [getOrNull](get-or-null.md) | [jvm]<br>inline fun [getOrNull](get-or-null.md)(): [T](index.md)?<br>Returns the encapsulated value if this instance represents [success](is-success.md) or `null` if it is [failure](is-failure.md). |
| [getOrThrow](../get-or-throw.md) | [jvm]<br>fun &lt;[T](../get-or-throw.md)&gt; [Result](index.md)&lt;[T](../get-or-throw.md)&gt;.[getOrThrow](../get-or-throw.md)(): [T](../get-or-throw.md)<br>Returns the encapsulated value if this instance represents [success](is-success.md) or throws the encapsulated [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md) if it is [failure](is-failure.md). |
| [onFailure](on-failure.md) | [jvm]<br>inline fun [onFailure](on-failure.md)(action: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)&gt;): [Result](index.md)&lt;[T](index.md)&gt; |
| [onSuccess](on-success.md) | [jvm]<br>inline fun [onSuccess](on-success.md)(action: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in [T](index.md)&gt;): [Result](index.md)&lt;[T](index.md)&gt; |
| [toString](to-string.md) | [jvm]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Returns a string `Success(v)` if this instance represents [success](is-success.md) where `v` is a string representation of the value or a string `Failure(x)` if it is [failure](is-failure.md) where `x` is a string representation of the exception. |
