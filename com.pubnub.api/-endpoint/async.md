[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [Endpoint](index.md) / [async](./async.md)

# async

`open fun async(callback: (result: Output?, status: `[`PNStatus`](../../com.pubnub.api.models.consumer/-p-n-status/index.md)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Executes the call asynchronously. This function does not block the thread.

### Parameters

`callback` - The callback to receive the response in.