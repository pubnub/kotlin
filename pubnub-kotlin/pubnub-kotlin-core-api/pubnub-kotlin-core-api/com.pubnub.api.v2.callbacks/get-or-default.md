//[pubnub-kotlin-core-api](../../index.md)/[com.pubnub.api.v2.callbacks](index.md)/[getOrDefault](get-or-default.md)

# getOrDefault

[common]\
inline fun &lt;[R](get-or-default.md), [T](get-or-default.md) : [R](get-or-default.md)&gt; [Result](-result/index.md)&lt;[T](get-or-default.md)&gt;.[getOrDefault](get-or-default.md)(defaultValue: [R](get-or-default.md)): [R](get-or-default.md)

Returns the encapsulated value if this instance represents [success](-result/is-success.md) or the [defaultValue](get-or-default.md) if it is [failure](-result/is-failure.md).

This function is a shorthand for `getOrElse { defaultValue }` (see [getOrElse](get-or-else.md)).
