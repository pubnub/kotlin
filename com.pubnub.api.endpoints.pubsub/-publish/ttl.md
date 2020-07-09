[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.pubsub](../index.md) / [Publish](index.md) / [ttl](./ttl.md)

# ttl

`var ttl: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?`

Set a per message time to live in storage.

* If `shouldStore = true`, and `ttl = 0`, the message is stored with no expiry time.
* If `shouldStore = true` and `ttl = X` (`X` is an Integer value),
the message is stored with an expiry time of `X` hours.
* If `shouldStore = false`, the `ttl` parameter is ignored.
* If ttl isn't specified, then expiration of the message defaults back to the expiry value for the key.
