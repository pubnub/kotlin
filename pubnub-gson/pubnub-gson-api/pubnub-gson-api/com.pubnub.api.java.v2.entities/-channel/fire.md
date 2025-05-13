//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.v2.entities](../index.md)/[Channel](index.md)/[fire](fire.md)

# fire

[jvm]\
abstract fun [fire](fire.md)(message: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)): [PublishBuilder](../../com.pubnub.api.java.v2.endpoints.pubsub/-publish-builder/index.md)

Send a message to PubNub Functions Event Handlers.

These messages will go directly to any Event Handlers registered on the channel that you fire to and will trigger their execution. The content of the fired request will be available for processing within the Event Handler.

The message sent via `fire()` isn't replicated, and so won't be received by any subscribers to the channel. The message is also not stored in history.

#### Parameters

jvm

| | |
|---|---|
| message | The payload.     **Warning:** It is important to note that you should not serialize JSON     when sending signals/messages via PubNub.     Why? Because the serialization is done for you automatically.     Instead just pass the full object as the message payload.     PubNub takes care of everything for you. |
| meta | Metadata object which can be used with the filtering ability.     If not specified, then the history configuration of the key is used. |
| usePost | Use HTTP POST to publish. Default is `false` |
| ttl | Set a per message time to live in storage.     - If `shouldStore = true`, and `ttl = 0`, the message is stored       with no expiry time.     - If `shouldStore = true` and `ttl = X` (`X` is an Integer value),       the message is stored with an expiry time of `X` hours.     - If `shouldStore = false`, the `ttl` parameter is ignored.     - If ttl isn't specified, then expiration of the message defaults       back to the expiry value for the key. |
