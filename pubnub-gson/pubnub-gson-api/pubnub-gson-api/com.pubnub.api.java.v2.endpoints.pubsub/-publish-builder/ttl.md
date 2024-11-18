//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.v2.endpoints.pubsub](../index.md)/[PublishBuilder](index.md)/[ttl](ttl.md)

# ttl

[jvm]\
abstract fun [ttl](ttl.md)(ttl: [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)): [PublishBuilder](index.md)

Sets the time-to-live (TTL) in Message Persistence. If shouldStore = true, and ttl = 0, the message is stored with no expiry time. If shouldStore = true and ttl = X (X is an Integer value), the message is stored with an expiry time of X hours. If shouldStore = false, the ttl parameter is ignored. If ttl is not specified, then expiration of the message defaults back to the expiry value for the key.

#### Return

The current instance of `PublishBuilder` for method chaining.

#### Parameters

jvm

| | |
|---|---|
| ttl | The TTL value in minutes for the message. |
