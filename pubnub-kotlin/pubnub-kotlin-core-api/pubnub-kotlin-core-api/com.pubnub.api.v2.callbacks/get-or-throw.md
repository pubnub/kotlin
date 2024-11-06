//[pubnub-kotlin-core-api](../../index.md)/[com.pubnub.api.v2.callbacks](index.md)/[getOrThrow](get-or-throw.md)

# getOrThrow

[common]\
fun &lt;[T](get-or-throw.md)&gt; [Result](-result/index.md)&lt;[T](get-or-throw.md)&gt;.[getOrThrow](get-or-throw.md)(): [T](get-or-throw.md)

Returns the encapsulated value if this instance represents [success](-result/is-success.md) or throws the encapsulated [PubNubException](../com.pubnub.api/-pub-nub-exception/index.md) if it is [failure](-result/is-failure.md).

This function is a shorthand for `getOrElse { throw it }` (see [getOrElse](get-or-else.md)).
