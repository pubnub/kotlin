//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[fire](fire.md)

# fire

[common, native]\
[common]\
expect abstract fun [fire](fire.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null, usePost: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false): [Publish](../../com.pubnub.api.endpoints.pubsub/-publish/index.md)

[native]\
actual abstract fun [fire](fire.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, usePost: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [Publish](../../com.pubnub.api.endpoints.pubsub/-publish/index.md)

[jvm]\
actual abstract fun [fire](fire.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, usePost: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [Publish](../../com.pubnub.api.endpoints.pubsub/-publish/index.md)

Send a message to PubNub Functions Event Handlers.

These messages will go directly to any Event Handlers registered on the channel that you fire to and will trigger their execution. The content of the fired request will be available for processing within the Event Handler.

The message sent via `fire()` isn't replicated, and so won't be received by any subscribers to the channel. The message is also not stored in history.

#### Parameters

jvm

| | |
|---|---|
| message | The payload.     **Warning:** It is important to note that you should not serialize JSON     when sending signals/messages via PubNub.     Why? Because the serialization is done for you automatically.     Instead just pass the full object as the message payload.     PubNub takes care of everything for you. |
| channel | Destination of the message. |
| meta | Metadata object which can be used with the filtering ability.     If not specified, then the history configuration of the key is used. |
| usePost | Use HTTP POST to publish. Default is `false` |

[jvm]\
abstract fun [~~fire~~](fire.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, usePost: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), ttl: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?): [Publish](../../com.pubnub.api.endpoints.pubsub/-publish/index.md)

---

### Deprecated

`fire()` never used the `ttl` parameter, please use the version without `ttl`.

#### Replace with

```kotlin
fire(channel, message, meta, usePost)
```
---

Send a message to PubNub Functions Event Handlers.

These messages will go directly to any Event Handlers registered on the channel that you fire to and will trigger their execution. The content of the fired request will be available for processing within the Event Handler.

The message sent via `fire()` isn't replicated, and so won't be received by any subscribers to the channel. The message is also not stored in history.

#### Parameters

jvm

| | |
|---|---|
| message | The payload.     **Warning:** It is important to note that you should not serialize JSON     when sending signals/messages via PubNub.     Why? Because the serialization is done for you automatically.     Instead just pass the full object as the message payload.     PubNub takes care of everything for you. |
| channel | Destination of the message. |
| meta | Metadata object which can be used with the filtering ability.     If not specified, then the history configuration of the key is used. |
| usePost | Use HTTP POST to publish. Default is `false` |
