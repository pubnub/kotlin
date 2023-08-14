//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.subscribe.eventengine.effect](../index.md)/[SubscribeEffectInvocation](index.md)

# SubscribeEffectInvocation

[jvm]\
sealed class [SubscribeEffectInvocation](index.md) : [EffectInvocation](../../com.pubnub.api.eventengine/-effect-invocation/index.md)

## Types

| Name | Summary |
|---|---|
| [CancelHandshake](-cancel-handshake/index.md) | [jvm]<br>object [CancelHandshake](-cancel-handshake/index.md) : [SubscribeEffectInvocation](index.md) |
| [CancelHandshakeReconnect](-cancel-handshake-reconnect/index.md) | [jvm]<br>object [CancelHandshakeReconnect](-cancel-handshake-reconnect/index.md) : [SubscribeEffectInvocation](index.md) |
| [CancelReceiveMessages](-cancel-receive-messages/index.md) | [jvm]<br>object [CancelReceiveMessages](-cancel-receive-messages/index.md) : [SubscribeEffectInvocation](index.md) |
| [CancelReceiveReconnect](-cancel-receive-reconnect/index.md) | [jvm]<br>object [CancelReceiveReconnect](-cancel-receive-reconnect/index.md) : [SubscribeEffectInvocation](index.md) |
| [EmitMessages](-emit-messages/index.md) | [jvm]<br>data class [EmitMessages](-emit-messages/index.md)(val messages: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PNEvent](../../com.pubnub.api.models.consumer.pubsub/-p-n-event/index.md)&gt;) : [SubscribeEffectInvocation](index.md) |
| [EmitStatus](-emit-status/index.md) | [jvm]<br>data class [EmitStatus](-emit-status/index.md)(val status: [PNStatus](../../com.pubnub.api.models.consumer/-p-n-status/index.md)) : [SubscribeEffectInvocation](index.md) |
| [Handshake](-handshake/index.md) | [jvm]<br>data class [Handshake](-handshake/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;) : [SubscribeEffectInvocation](index.md) |
| [HandshakeReconnect](-handshake-reconnect/index.md) | [jvm]<br>data class [HandshakeReconnect](-handshake-reconnect/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val attempts: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val reason: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)?) : [SubscribeEffectInvocation](index.md) |
| [ReceiveMessages](-receive-messages/index.md) | [jvm]<br>data class [ReceiveMessages](-receive-messages/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val subscriptionCursor: [SubscriptionCursor](../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md)) : [SubscribeEffectInvocation](index.md) |
| [ReceiveReconnect](-receive-reconnect/index.md) | [jvm]<br>data class [ReceiveReconnect](-receive-reconnect/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val subscriptionCursor: [SubscriptionCursor](../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md), val attempts: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val reason: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)?) : [SubscribeEffectInvocation](index.md) |

## Properties

| Name | Summary |
|---|---|
| [id](id.md) | [jvm]<br>open override val [id](id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [type](type.md) | [jvm]<br>open override val [type](type.md): [EffectInvocationType](../../com.pubnub.api.eventengine/-effect-invocation-type/index.md) |

## Inheritors

| Name |
|---|
| [ReceiveMessages](-receive-messages/index.md) |
| [CancelReceiveMessages](-cancel-receive-messages/index.md) |
| [ReceiveReconnect](-receive-reconnect/index.md) |
| [CancelReceiveReconnect](-cancel-receive-reconnect/index.md) |
| [Handshake](-handshake/index.md) |
| [CancelHandshake](-cancel-handshake/index.md) |
| [HandshakeReconnect](-handshake-reconnect/index.md) |
| [CancelHandshakeReconnect](-cancel-handshake-reconnect/index.md) |
| [EmitStatus](-emit-status/index.md) |
| [EmitMessages](-emit-messages/index.md) |
