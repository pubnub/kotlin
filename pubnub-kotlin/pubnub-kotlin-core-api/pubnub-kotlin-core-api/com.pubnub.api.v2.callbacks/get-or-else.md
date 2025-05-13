//[pubnub-kotlin-core-api](../../index.md)/[com.pubnub.api.v2.callbacks](index.md)/[getOrElse](get-or-else.md)

# getOrElse

[common]\
inline fun &lt;[R](get-or-else.md), [T](get-or-else.md) : [R](get-or-else.md)&gt; [Result](-result/index.md)&lt;[T](get-or-else.md)&gt;.[getOrElse](get-or-else.md)(onFailure: (exception: [PubNubException](../com.pubnub.api/-pub-nub-exception/index.md)) -&gt; [R](get-or-else.md)): [R](get-or-else.md)

Returns the encapsulated value if this instance represents [success](-result/is-success.md) or the result of [onFailure](get-or-else.md) function for the encapsulated [PubNubException](../com.pubnub.api/-pub-nub-exception/index.md) exception if it is [failure](-result/is-failure.md).

Note, that this function rethrows any [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html) exception thrown by [onFailure](get-or-else.md) function.

This function is a shorthand for `fold(onSuccess = { it }, onFailure = onFailure)` (see [fold](fold.md)).
