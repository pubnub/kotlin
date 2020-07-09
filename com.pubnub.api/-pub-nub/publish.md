[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNub](index.md) / [publish](./publish.md)

# publish

`fun publish(): `[`Publish`](../../com.pubnub.api.endpoints.pubsub/-publish/index.md)

Send a message to all subscribers of a channel.

To publish a message you must first specify a valid [PNConfiguration.publishKey](../-p-n-configuration/publish-key.md).
A successfully published message is replicated across the PubNub Real-Time Network and sent
simultaneously to all subscribed clients on a channel.

Messages in transit can be secured from potential eavesdroppers with SSL/TLS by setting
[PNConfiguration.secure](../-p-n-configuration/secure.md) to `true` during initialization.

**Publish Anytime**

It is not required to be subscribed to a channel in order to publish to that channel.

**Message Data:**

The message argument can contain any JSON serializable data, including: Objects, Arrays, Integers and Strings.
Data should not contain special Java/Kotlin classes or functions as these will not serialize.
String content can include any single-byte or multi-byte UTF-8 character.

