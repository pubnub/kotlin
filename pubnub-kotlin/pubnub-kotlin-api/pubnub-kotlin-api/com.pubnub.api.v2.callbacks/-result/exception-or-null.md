//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[Result](index.md)/[exceptionOrNull](exception-or-null.md)

# exceptionOrNull

[common]\
fun [exceptionOrNull](exception-or-null.md)(): [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)?

Returns the encapsulated [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md) exception if this instance represents [failure](is-failure.md) or `null` if it is [success](is-success.md).

This function is a shorthand for `fold(onSuccess = { null }, onFailure = { it })` (see [fold](../fold.md)).
