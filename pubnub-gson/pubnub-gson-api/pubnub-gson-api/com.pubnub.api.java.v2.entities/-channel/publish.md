//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.v2.entities](../index.md)/[Channel](index.md)/[publish](publish.md)

# publish

[jvm]\
abstract fun [publish](publish.md)(message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [PublishBuilder](../../com.pubnub.api.java.v2.endpoints.pubsub/-publish-builder/index.md)

Send a message to all subscribers of the channel.

To publish a message you must first specify a valid PNConfiguration.publishKey. A successfully published message is replicated across the PubNub Real-Time Network and sent simultaneously to all subscribed clients on the channel.

Messages in transit can be secured from potential eavesdroppers with SSL/TLS by setting PNConfiguration.secure to `true` during initialization.

**Publish Anytime**

It is not required to be subscribed to a channel in order to publish to that channel.

**Message Data:**

The message argument can contain any JSON serializable data, including: Objects, Arrays, Integers and Strings. Data should not contain special Java/Kotlin classes or functions as these will not serialize. String content can include any single-byte or multi-byte UTF-8 character.

#### Parameters

jvm

| | |
|---|---|
| message | The payload.     **Warning:** It is important to note that you should not serialize JSON     when sending signals/messages via PubNub.     Why? Because the serialization is done for you automatically.     Instead just pass the full object as the message payload.     PubNub takes care of everything for you. |
| meta | Metadata object which can be used with the filtering ability. |
| shouldStore | Store in history.     If not specified, then the history configuration of the key is used. |
| usePost | Use HTTP POST to publish. Default is `false` |
| replicate | Replicate the message. Is set to `true` by default. |
| ttl | Set a per message time to live in storage.     - If `shouldStore = true`, and `ttl = 0`, the message is stored       with no expiry time.     - If `shouldStore = true` and `ttl = X` (`X` is an Integer value),       the message is stored with an expiry time of `X` hours.     - If `shouldStore = false`, the `ttl` parameter is ignored.     - If ttl isn't specified, then expiration of the message defaults       back to the expiry value for the key. |
| customMessageType | The custom type associated with the message. |
