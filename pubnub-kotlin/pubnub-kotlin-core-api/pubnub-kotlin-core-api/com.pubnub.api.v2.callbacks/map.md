//[pubnub-kotlin-core-api](../../index.md)/[com.pubnub.api.v2.callbacks](index.md)/[map](map.md)

# map

[common]\
inline fun &lt;[R](map.md), [T](map.md)&gt; [Result](-result/index.md)&lt;[T](map.md)&gt;.[map](map.md)(transform: (value: [T](map.md)) -&gt; [R](map.md)): [Result](-result/index.md)&lt;[R](map.md)&gt;

Returns the encapsulated result of the given [transform](map.md) function applied to the encapsulated value if this instance represents [success](-result/is-success.md) or the original encapsulated [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html) exception if it is [failure](-result/is-failure.md).

Note, that this function rethrows any [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html) exception thrown by [transform](map.md) function. See [mapCatching](map-catching.md) for an alternative that encapsulates exceptions.
