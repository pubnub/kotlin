//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[publish](publish.md)

# publish

[jvm]\
abstract fun [publish](publish.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null, shouldStore: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? = null, usePost: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, replicate: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true, ttl: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null): [Publish](../../com.pubnub.api.endpoints.pubsub/-publish/index.md)

Send a message to all subscribers of a channel.

To publish a message you must first specify a valid [PNConfiguration.publishKey](../-p-n-configuration/publish-key.md). A successfully published message is replicated across the PubNub Real-Time Network and sent simultaneously to all subscribed clients on a channel.

Messages in transit can be secured from potential eavesdroppers with SSL/TLS by setting [PNConfiguration.secure](../-p-n-configuration/secure.md) to `true` during initialization.

**Publish Anytime**

It is not required to be subscribed to a channel in order to publish to that channel.

**Message Data:**

The message argument can contain any JSON serializable data, including: Objects, Arrays, Integers and Strings. Data should not contain special Java/Kotlin classes or functions as these will not serialize. String content can include any single-byte or multi-byte UTF-8 character.

#### Parameters

jvm

| | |
|---|---|
| message | The payload.     **Warning:** It is important to note that you should not serialize JSON     when sending signals/messages via PubNub.     Why? Because the serialization is done for you automatically.     Instead just pass the full object as the message payload.     PubNub takes care of everything for you. |
| channel | Destination of the message. |
| meta | Metadata object which can be used with the filtering ability. |
| shouldStore | Store in history.     If not specified, then the history configuration of the key is used. |
| usePost | Use HTTP POST to publish. Default is `false` |
| replicate | Replicate the message. Is set to `true` by default. |
| ttl | Set a per message time to live in storage.     - If `shouldStore = true`, and `ttl = 0`, the message is stored       with no expiry time.     - If `shouldStore = true` and `ttl = X` (`X` is an Integer value),       the message is stored with an expiry time of `X` hours.     - If `shouldStore = false`, the `ttl` parameter is ignored.     - If ttl isn't specified, then expiration of the message defaults       back to the expiry value for the key. |
