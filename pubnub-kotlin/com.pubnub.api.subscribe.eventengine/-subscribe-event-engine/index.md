//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.subscribe.eventengine](../index.md)/[SubscribeEventEngine](index.md)

# SubscribeEventEngine

[jvm]\
class [SubscribeEventEngine](index.md)(val effectSink: [Sink](../../com.pubnub.api.eventengine/-sink/index.md)&lt;[SubscribeEffectInvocation](../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/index.md)&gt;, eventSource: [Source](../../com.pubnub.api.eventengine/-source/index.md)&lt;[SubscribeEvent](../../com.pubnub.api.subscribe.eventengine.event/-subscribe-event/index.md)&gt;, currenState: [SubscribeState](../../com.pubnub.api.subscribe.eventengine.state/-subscribe-state/index.md) = SubscribeState.Unsubscribed, executorService: [ExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html) = Executors.newSingleThreadExecutor())

## Constructors

| | |
|---|---|
| [SubscribeEventEngine](-subscribe-event-engine.md) | [jvm]<br>fun [SubscribeEventEngine](-subscribe-event-engine.md)(effectSink: [Sink](../../com.pubnub.api.eventengine/-sink/index.md)&lt;[SubscribeEffectInvocation](../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/index.md)&gt;, eventSource: [Source](../../com.pubnub.api.eventengine/-source/index.md)&lt;[SubscribeEvent](../../com.pubnub.api.subscribe.eventengine.event/-subscribe-event/index.md)&gt;, currenState: [SubscribeState](../../com.pubnub.api.subscribe.eventengine.state/-subscribe-state/index.md) = SubscribeState.Unsubscribed, executorService: [ExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html) = Executors.newSingleThreadExecutor()) |

## Functions

| Name | Summary |
|---|---|
| [start](start.md) | [jvm]<br>fun [start](start.md)() |
| [stop](stop.md) | [jvm]<br>fun [stop](stop.md)() |

## Properties

| Name | Summary |
|---|---|
| [effectSink](effect-sink.md) | [jvm]<br>val [effectSink](effect-sink.md): [Sink](../../com.pubnub.api.eventengine/-sink/index.md)&lt;[SubscribeEffectInvocation](../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/index.md)&gt; |
