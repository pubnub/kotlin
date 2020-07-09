[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.pubsub](../index.md) / [Publish](index.md) / [message](./message.md)

# message

`lateinit var message: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)

The payload.

**Warning:** It is important to note that you should not JSON serialize when sending signals/messages via PubNub.
Why? Because the serialization is done for you automatically.
Instead just pass the full object as the message payload.
PubNub takes care of everything for you.

