//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.subscribe.eventengine.state](../index.md)/[SubscribeState](index.md)

# SubscribeState

[jvm]\
sealed class [SubscribeState](index.md) : [State](../../com.pubnub.api.eventengine/-state/index.md)&lt;[SubscribeEffectInvocation](../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/index.md), [SubscribeEvent](../../com.pubnub.api.subscribe.eventengine.event/-subscribe-event/index.md), [SubscribeState](index.md)&gt;

## Types

| Name | Summary |
|---|---|
| [HandshakeFailed](-handshake-failed/index.md) | [jvm]<br>data class [HandshakeFailed](-handshake-failed/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val reason: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md), val subscriptionCursor: [SubscriptionCursor](../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md)? = null) : [SubscribeState](index.md) |
| [HandshakeReconnecting](-handshake-reconnecting/index.md) | [jvm]<br>data class [HandshakeReconnecting](-handshake-reconnecting/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val attempts: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val reason: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)?, val subscriptionCursor: [SubscriptionCursor](../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md)? = null) : [SubscribeState](index.md) |
| [HandshakeStopped](-handshake-stopped/index.md) | [jvm]<br>data class [HandshakeStopped](-handshake-stopped/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val reason: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)?) : [SubscribeState](index.md) |
| [Handshaking](-handshaking/index.md) | [jvm]<br>data class [Handshaking](-handshaking/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val subscriptionCursor: [SubscriptionCursor](../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md)? = null) : [SubscribeState](index.md) |
| [ReceiveFailed](-receive-failed/index.md) | [jvm]<br>data class [ReceiveFailed](-receive-failed/index.md)(channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val subscriptionCursor: [SubscriptionCursor](../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md), val reason: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)) : [SubscribeState](index.md) |
| [ReceiveReconnecting](-receive-reconnecting/index.md) | [jvm]<br>data class [ReceiveReconnecting](-receive-reconnecting/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val subscriptionCursor: [SubscriptionCursor](../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md), val attempts: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val reason: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)?) : [SubscribeState](index.md) |
| [ReceiveStopped](-receive-stopped/index.md) | [jvm]<br>data class [ReceiveStopped](-receive-stopped/index.md)(channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val subscriptionCursor: [SubscriptionCursor](../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md)) : [SubscribeState](index.md) |
| [Receiving](-receiving/index.md) | [jvm]<br>data class [Receiving](-receiving/index.md)(channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, subscriptionCursor: [SubscriptionCursor](../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md)) : [SubscribeState](index.md) |
| [Unsubscribed](-unsubscribed/index.md) | [jvm]<br>object [Unsubscribed](-unsubscribed/index.md) : [SubscribeState](index.md) |

## Functions

| Name | Summary |
|---|---|
| [onEntry](../../com.pubnub.api.eventengine/-state/on-entry.md) | [jvm]<br>open fun [onEntry](../../com.pubnub.api.eventengine/-state/on-entry.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[SubscribeEffectInvocation](../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/index.md)&gt; |
| [onExit](../../com.pubnub.api.eventengine/-state/on-exit.md) | [jvm]<br>open fun [onExit](../../com.pubnub.api.eventengine/-state/on-exit.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[SubscribeEffectInvocation](../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/index.md)&gt; |
| [transition](index.md#-280315663%2FFunctions%2F-1216412040) | [jvm]<br>abstract fun [transition](index.md#-280315663%2FFunctions%2F-1216412040)(event: [SubscribeEvent](../../com.pubnub.api.subscribe.eventengine.event/-subscribe-event/index.md)): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[SubscribeState](index.md), [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[SubscribeEffectInvocation](../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/index.md)&gt;&gt; |

## Inheritors

| Name |
|---|
| [Unsubscribed](-unsubscribed/index.md) |
| [Handshaking](-handshaking/index.md) |
| [HandshakeReconnecting](-handshake-reconnecting/index.md) |
| [HandshakeStopped](-handshake-stopped/index.md) |
| [HandshakeFailed](-handshake-failed/index.md) |
| [Receiving](-receiving/index.md) |
| [ReceiveReconnecting](-receive-reconnecting/index.md) |
| [ReceiveStopped](-receive-stopped/index.md) |
| [ReceiveFailed](-receive-failed/index.md) |
