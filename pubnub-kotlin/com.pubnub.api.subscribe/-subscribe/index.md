//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.subscribe](../index.md)/[Subscribe](index.md)

# Subscribe

[jvm]\
class [Subscribe](index.md)(eventEngineManager: [EventEngineManager](../../com.pubnub.api.managers/-event-engine-manager/index.md), subscriptionData: [SubscriptionData](../../com.pubnub.api.subscribe.eventengine.data/-subscription-data/index.md) = SubscriptionData())

## Constructors

| | |
|---|---|
| [Subscribe](-subscribe.md) | [jvm]<br>fun [Subscribe](-subscribe.md)(eventEngineManager: [EventEngineManager](../../com.pubnub.api.managers/-event-engine-manager/index.md), subscriptionData: [SubscriptionData](../../com.pubnub.api.subscribe.eventengine.data/-subscription-data/index.md) = SubscriptionData()) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [destroy](destroy.md) | [jvm]<br>@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)<br>fun [destroy](destroy.md)() |
| [disconnect](disconnect.md) | [jvm]<br>fun [disconnect](disconnect.md)() |
| [getSubscribedChannelGroups](get-subscribed-channel-groups.md) | [jvm]<br>@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)<br>fun [getSubscribedChannelGroups](get-subscribed-channel-groups.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [getSubscribedChannels](get-subscribed-channels.md) | [jvm]<br>@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)<br>fun [getSubscribedChannels](get-subscribed-channels.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [reconnect](reconnect.md) | [jvm]<br>fun [reconnect](reconnect.md)() |
| [subscribe](subscribe.md) | [jvm]<br>@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)<br>fun [subscribe](subscribe.md)(channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, withPresence: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), withTimetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0) |
| [unsubscribe](unsubscribe.md) | [jvm]<br>@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)<br>fun [unsubscribe](unsubscribe.md)(channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptySet(), channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptySet()) |
| [unsubscribeAll](unsubscribe-all.md) | [jvm]<br>@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)<br>fun [unsubscribeAll](unsubscribe-all.md)() |
