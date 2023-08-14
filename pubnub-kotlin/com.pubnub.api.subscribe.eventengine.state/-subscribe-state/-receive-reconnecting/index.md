//[pubnub-kotlin](../../../../index.md)/[com.pubnub.api.subscribe.eventengine.state](../../index.md)/[SubscribeState](../index.md)/[ReceiveReconnecting](index.md)

# ReceiveReconnecting

[jvm]\
data class [ReceiveReconnecting](index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val subscriptionCursor: [SubscriptionCursor](../../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md), val attempts: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val reason: [PubNubException](../../../com.pubnub.api/-pub-nub-exception/index.md)?) : [SubscribeState](../index.md)

## Constructors

| | |
|---|---|
| [ReceiveReconnecting](-receive-reconnecting.md) | [jvm]<br>fun [ReceiveReconnecting](-receive-reconnecting.md)(channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, subscriptionCursor: [SubscriptionCursor](../../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md), attempts: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), reason: [PubNubException](../../../com.pubnub.api/-pub-nub-exception/index.md)?) |

## Functions

| Name | Summary |
|---|---|
| [onEntry](on-entry.md) | [jvm]<br>open override fun [onEntry](on-entry.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[SubscribeEffectInvocation.ReceiveReconnect](../../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/-receive-reconnect/index.md)&gt; |
| [onExit](on-exit.md) | [jvm]<br>open override fun [onExit](on-exit.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[SubscribeEffectInvocation.CancelReceiveReconnect](../../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/-cancel-receive-reconnect/index.md)&gt; |
| [transition](transition.md) | [jvm]<br>open override fun [transition](transition.md)(event: [SubscribeEvent](../../../com.pubnub.api.subscribe.eventengine.event/-subscribe-event/index.md)): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[SubscribeState](../index.md), [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[SubscribeEffectInvocation](../../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/index.md)&gt;&gt; |

## Properties

| Name | Summary |
|---|---|
| [attempts](attempts.md) | [jvm]<br>val [attempts](attempts.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [channelGroups](channel-groups.md) | [jvm]<br>val [channelGroups](channel-groups.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [channels](channels.md) | [jvm]<br>val [channels](channels.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [reason](reason.md) | [jvm]<br>val [reason](reason.md): [PubNubException](../../../com.pubnub.api/-pub-nub-exception/index.md)? |
| [subscriptionCursor](subscription-cursor.md) | [jvm]<br>val [subscriptionCursor](subscription-cursor.md): [SubscriptionCursor](../../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md) |
