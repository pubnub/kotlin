//[pubnub-kotlin](../../../../index.md)/[com.pubnub.api.subscribe.eventengine.state](../../index.md)/[SubscribeState](../index.md)/[HandshakeReconnecting](index.md)

# HandshakeReconnecting

[jvm]\
data class [HandshakeReconnecting](index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val attempts: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val reason: [PubNubException](../../../com.pubnub.api/-pub-nub-exception/index.md)?, val subscriptionCursor: [SubscriptionCursor](../../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md)? = null) : [SubscribeState](../index.md)

## Constructors

| | |
|---|---|
| [HandshakeReconnecting](-handshake-reconnecting.md) | [jvm]<br>fun [HandshakeReconnecting](-handshake-reconnecting.md)(channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, attempts: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), reason: [PubNubException](../../../com.pubnub.api/-pub-nub-exception/index.md)?, subscriptionCursor: [SubscriptionCursor](../../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md)? = null) |

## Functions

| Name | Summary |
|---|---|
| [onEntry](on-entry.md) | [jvm]<br>open override fun [onEntry](on-entry.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[SubscribeEffectInvocation.HandshakeReconnect](../../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/-handshake-reconnect/index.md)&gt; |
| [onExit](on-exit.md) | [jvm]<br>open override fun [onExit](on-exit.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[SubscribeEffectInvocation.CancelHandshakeReconnect](../../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/-cancel-handshake-reconnect/index.md)&gt; |
| [transition](transition.md) | [jvm]<br>open override fun [transition](transition.md)(event: [SubscribeEvent](../../../com.pubnub.api.subscribe.eventengine.event/-subscribe-event/index.md)): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[SubscribeState](../index.md), [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[SubscribeEffectInvocation](../../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/index.md)&gt;&gt; |

## Properties

| Name | Summary |
|---|---|
| [attempts](attempts.md) | [jvm]<br>val [attempts](attempts.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [channelGroups](channel-groups.md) | [jvm]<br>val [channelGroups](channel-groups.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [channels](channels.md) | [jvm]<br>val [channels](channels.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [reason](reason.md) | [jvm]<br>val [reason](reason.md): [PubNubException](../../../com.pubnub.api/-pub-nub-exception/index.md)? |
| [subscriptionCursor](subscription-cursor.md) | [jvm]<br>val [subscriptionCursor](subscription-cursor.md): [SubscriptionCursor](../../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md)? = null |
