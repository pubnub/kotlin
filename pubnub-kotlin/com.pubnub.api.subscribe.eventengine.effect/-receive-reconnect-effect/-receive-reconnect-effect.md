//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.subscribe.eventengine.effect](../index.md)/[ReceiveReconnectEffect](index.md)/[ReceiveReconnectEffect](-receive-reconnect-effect.md)

# ReceiveReconnectEffect

[jvm]\
fun [ReceiveReconnectEffect](-receive-reconnect-effect.md)(receiveMessagesRemoteAction: [RemoteAction](../../com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&lt;[ReceiveMessagesResult](../-receive-messages-result/index.md)&gt;, subscribeEventSink: [Sink](../../com.pubnub.api.eventengine/-sink/index.md)&lt;[SubscribeEvent](../../com.pubnub.api.subscribe.eventengine.event/-subscribe-event/index.md)&gt;, policy: [RetryPolicy](../-retry-policy/index.md), executorService: [ScheduledExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledExecutorService.html), attempts: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), reason: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)?)
