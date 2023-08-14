//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.subscribe.eventengine.event](../index.md)/[SubscribeEvent](index.md)

# SubscribeEvent

[jvm]\
sealed class [SubscribeEvent](index.md) : [Event](../../com.pubnub.api.eventengine/-event/index.md)

## Types

| Name | Summary |
|---|---|
| [Disconnect](-disconnect/index.md) | [jvm]<br>object [Disconnect](-disconnect/index.md) : [SubscribeEvent](index.md) |
| [HandshakeFailure](-handshake-failure/index.md) | [jvm]<br>data class [HandshakeFailure](-handshake-failure/index.md)(val reason: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)) : [SubscribeEvent](index.md) |
| [HandshakeReconnectFailure](-handshake-reconnect-failure/index.md) | [jvm]<br>data class [HandshakeReconnectFailure](-handshake-reconnect-failure/index.md)(val reason: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)) : [SubscribeEvent](index.md) |
| [HandshakeReconnectGiveup](-handshake-reconnect-giveup/index.md) | [jvm]<br>data class [HandshakeReconnectGiveup](-handshake-reconnect-giveup/index.md)(val reason: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)) : [SubscribeEvent](index.md) |
| [HandshakeReconnectRetry](-handshake-reconnect-retry/index.md) | [jvm]<br>object [HandshakeReconnectRetry](-handshake-reconnect-retry/index.md) : [SubscribeEvent](index.md) |
| [HandshakeReconnectSuccess](-handshake-reconnect-success/index.md) | [jvm]<br>data class [HandshakeReconnectSuccess](-handshake-reconnect-success/index.md)(val subscriptionCursor: [SubscriptionCursor](../-subscription-cursor/index.md)) : [SubscribeEvent](index.md) |
| [HandshakeSuccess](-handshake-success/index.md) | [jvm]<br>data class [HandshakeSuccess](-handshake-success/index.md)(val subscriptionCursor: [SubscriptionCursor](../-subscription-cursor/index.md)) : [SubscribeEvent](index.md) |
| [ReceiveFailure](-receive-failure/index.md) | [jvm]<br>data class [ReceiveFailure](-receive-failure/index.md)(val reason: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)) : [SubscribeEvent](index.md) |
| [ReceiveReconnectFailure](-receive-reconnect-failure/index.md) | [jvm]<br>data class [ReceiveReconnectFailure](-receive-reconnect-failure/index.md)(val reason: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)) : [SubscribeEvent](index.md) |
| [ReceiveReconnectGiveup](-receive-reconnect-giveup/index.md) | [jvm]<br>data class [ReceiveReconnectGiveup](-receive-reconnect-giveup/index.md)(val reason: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)) : [SubscribeEvent](index.md) |
| [ReceiveReconnectRetry](-receive-reconnect-retry/index.md) | [jvm]<br>object [ReceiveReconnectRetry](-receive-reconnect-retry/index.md) : [SubscribeEvent](index.md) |
| [ReceiveReconnectSuccess](-receive-reconnect-success/index.md) | [jvm]<br>data class [ReceiveReconnectSuccess](-receive-reconnect-success/index.md)(val messages: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PNEvent](../../com.pubnub.api.models.consumer.pubsub/-p-n-event/index.md)&gt;, val subscriptionCursor: [SubscriptionCursor](../-subscription-cursor/index.md)) : [SubscribeEvent](index.md) |
| [ReceiveSuccess](-receive-success/index.md) | [jvm]<br>data class [ReceiveSuccess](-receive-success/index.md)(val messages: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PNEvent](../../com.pubnub.api.models.consumer.pubsub/-p-n-event/index.md)&gt;, val subscriptionCursor: [SubscriptionCursor](../-subscription-cursor/index.md)) : [SubscribeEvent](index.md) |
| [Reconnect](-reconnect/index.md) | [jvm]<br>object [Reconnect](-reconnect/index.md) : [SubscribeEvent](index.md) |
| [SubscriptionChanged](-subscription-changed/index.md) | [jvm]<br>data class [SubscriptionChanged](-subscription-changed/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;) : [SubscribeEvent](index.md) |
| [SubscriptionRestored](-subscription-restored/index.md) | [jvm]<br>data class [SubscriptionRestored](-subscription-restored/index.md)(val channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, val subscriptionCursor: [SubscriptionCursor](../-subscription-cursor/index.md)) : [SubscribeEvent](index.md) |
| [UnsubscribeAll](-unsubscribe-all/index.md) | [jvm]<br>object [UnsubscribeAll](-unsubscribe-all/index.md) : [SubscribeEvent](index.md) |

## Inheritors

| Name |
|---|
| [SubscriptionChanged](-subscription-changed/index.md) |
| [SubscriptionRestored](-subscription-restored/index.md) |
| [Disconnect](-disconnect/index.md) |
| [Reconnect](-reconnect/index.md) |
| [UnsubscribeAll](-unsubscribe-all/index.md) |
| [HandshakeSuccess](-handshake-success/index.md) |
| [HandshakeFailure](-handshake-failure/index.md) |
| [HandshakeReconnectSuccess](-handshake-reconnect-success/index.md) |
| [HandshakeReconnectFailure](-handshake-reconnect-failure/index.md) |
| [HandshakeReconnectRetry](-handshake-reconnect-retry/index.md) |
| [HandshakeReconnectGiveup](-handshake-reconnect-giveup/index.md) |
| [ReceiveSuccess](-receive-success/index.md) |
| [ReceiveFailure](-receive-failure/index.md) |
| [ReceiveReconnectSuccess](-receive-reconnect-success/index.md) |
| [ReceiveReconnectFailure](-receive-reconnect-failure/index.md) |
| [ReceiveReconnectRetry](-receive-reconnect-retry/index.md) |
| [ReceiveReconnectGiveup](-receive-reconnect-giveup/index.md) |
