//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[Result](index.md)/[getOrNull](get-or-null.md)

# getOrNull

[common]\
inline fun [getOrNull](get-or-null.md)(): [T](index.md)?

Returns the encapsulated value if this instance represents [success](is-success.md) or `null` if it is [failure](is-failure.md).

This function is a shorthand for `getOrElse { null }` (see [getOrElse](../get-or-else.md)) or `fold(onSuccess = { it }, onFailure = { null })` (see [fold](../fold.md)).
