//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.entities](../index.md)/[Channel](index.md)

# Channel

[jvm]\
interface [Channel](index.md) : [BaseChannel](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.entities/-base-channel/index.md)&lt;[EventListener](../../com.pubnub.api.v2.callbacks/-event-listener/index.md), [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md)&gt; 

A representation of a PubNub channel identified by its [name](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.v2.entities/-channel/name.md).

You can get a [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) to this channel through [Subscribable.subscription](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.entities/-subscribable/subscription.md).

Use the [com.pubnub.api.PubNub.channel](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api/-pub-nub/channel.md) factory method to create instances of this interface.

## Properties

| Name | Summary |
|---|---|
| [name](index.md#-1931444638%2FProperties%2F1262999440) | [jvm]<br>abstract val [name](index.md#-1931444638%2FProperties%2F1262999440): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

## Functions

| Name | Summary |
|---|---|
| [deleteFile](delete-file.md) | [jvm]<br>abstract fun [deleteFile](delete-file.md)(fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), fileId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [DeleteFile](../../com.pubnub.api.endpoints.files/-delete-file/index.md)<br>Delete file from the Channel. |
| [fire](fire.md) | [jvm]<br>abstract fun [fire](fire.md)(message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null, usePost: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, ttl: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null): [Publish](../../com.pubnub.api.endpoints.pubsub/-publish/index.md)<br>Send a message to PubNub Functions Event Handlers. |
| [publish](publish.md) | [jvm]<br>abstract fun [publish](publish.md)(message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null, shouldStore: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? = null, usePost: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, replicate: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true, ttl: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null): [Publish](../../com.pubnub.api.endpoints.pubsub/-publish/index.md)<br>Send a message to all subscribers of the channel. |
| [sendFile](send-file.md) | [jvm]<br>abstract fun [sendFile](send-file.md)(fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), inputStream: [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html), message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null, meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null, ttl: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null, shouldStore: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? = null, cipherKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null): [SendFile](../../com.pubnub.api.endpoints.files/-send-file/index.md)<br>Upload file / data to the Channel. |
| [signal](signal.md) | [jvm]<br>abstract fun [signal](signal.md)(message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [Signal](../../com.pubnub.api.endpoints.pubsub/-signal/index.md)<br>Send a signal to all subscribers of the channel. |
| [subscription](index.md#1711906756%2FFunctions%2F1262999440) | [jvm]<br>abstract override fun [subscription](index.md#1711906756%2FFunctions%2F1262999440)(options: [SubscriptionOptions](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.subscriptions/-subscription-options/index.md)): [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) |
