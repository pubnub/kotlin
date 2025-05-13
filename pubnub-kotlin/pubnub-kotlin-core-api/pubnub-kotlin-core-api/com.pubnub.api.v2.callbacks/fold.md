//[pubnub-kotlin-core-api](../../index.md)/[com.pubnub.api.v2.callbacks](index.md)/[fold](fold.md)

# fold

[common]\
inline fun &lt;[R](fold.md), [T](fold.md)&gt; [Result](-result/index.md)&lt;[T](fold.md)&gt;.[fold](fold.md)(onSuccess: (value: [T](fold.md)) -&gt; [R](fold.md), onFailure: (exception: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)) -&gt; [R](fold.md)): [R](fold.md)

Returns the result of [onSuccess](fold.md) for the encapsulated value if this instance represents [success](-result/is-success.md) or the result of [onFailure](fold.md) function for the encapsulated [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html) exception if it is [failure](-result/is-failure.md).

Note, that this function rethrows any [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html) exception thrown by [onSuccess](fold.md) or by [onFailure](fold.md) function.
