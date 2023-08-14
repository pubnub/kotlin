//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.eventengine](../index.md)/[EventEngineConf](index.md)

# EventEngineConf

[jvm]\
interface [EventEngineConf](index.md)

## Properties

| Name | Summary |
|---|---|
| [effectSink](effect-sink.md) | [jvm]<br>abstract val [effectSink](effect-sink.md): [Sink](../-sink/index.md)&lt;[SubscribeEffectInvocation](../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/index.md)&gt; |
| [effectSource](effect-source.md) | [jvm]<br>abstract val [effectSource](effect-source.md): [Source](../-source/index.md)&lt;[SubscribeEffectInvocation](../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/index.md)&gt; |
| [subscribeEventSink](subscribe-event-sink.md) | [jvm]<br>abstract val [subscribeEventSink](subscribe-event-sink.md): [Sink](../-sink/index.md)&lt;[SubscribeEvent](../../com.pubnub.api.subscribe.eventengine.event/-subscribe-event/index.md)&gt; |
| [subscribeEventSource](subscribe-event-source.md) | [jvm]<br>abstract val [subscribeEventSource](subscribe-event-source.md): [Source](../-source/index.md)&lt;[SubscribeEvent](../../com.pubnub.api.subscribe.eventengine.event/-subscribe-event/index.md)&gt; |

## Inheritors

| Name |
|---|
| [SubscribeEventEngineConfImpl](../../com.pubnub.api.subscribe.eventengine.configuration/-subscribe-event-engine-conf-impl/index.md) |
