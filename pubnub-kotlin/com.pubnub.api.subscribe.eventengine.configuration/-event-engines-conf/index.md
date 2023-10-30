//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.subscribe.eventengine.configuration](../index.md)/[EventEnginesConf](index.md)

# EventEnginesConf

[jvm]\
class [EventEnginesConf](index.md)(val subscribe: [EventEngineConf](../../com.pubnub.api.eventengine/-event-engine-conf/index.md)&lt;[SubscribeEffectInvocation](../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/index.md), [SubscribeEvent](../../com.pubnub.api.subscribe.eventengine.event/-subscribe-event/index.md)&gt; = QueueEventEngineConf(), val presence: [EventEngineConf](../../com.pubnub.api.eventengine/-event-engine-conf/index.md)&lt;[PresenceEffectInvocation](../../com.pubnub.api.presence.eventengine.effect/-presence-effect-invocation/index.md), [PresenceEvent](../../com.pubnub.api.presence.eventengine.event/-presence-event/index.md)&gt; = QueueEventEngineConf())

## Constructors

| | |
|---|---|
| [EventEnginesConf](-event-engines-conf.md) | [jvm]<br>fun [EventEnginesConf](-event-engines-conf.md)(subscribe: [EventEngineConf](../../com.pubnub.api.eventengine/-event-engine-conf/index.md)&lt;[SubscribeEffectInvocation](../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/index.md), [SubscribeEvent](../../com.pubnub.api.subscribe.eventengine.event/-subscribe-event/index.md)&gt; = QueueEventEngineConf(), presence: [EventEngineConf](../../com.pubnub.api.eventengine/-event-engine-conf/index.md)&lt;[PresenceEffectInvocation](../../com.pubnub.api.presence.eventengine.effect/-presence-effect-invocation/index.md), [PresenceEvent](../../com.pubnub.api.presence.eventengine.event/-presence-event/index.md)&gt; = QueueEventEngineConf()) |

## Properties

| Name | Summary |
|---|---|
| [presence](presence.md) | [jvm]<br>val [presence](presence.md): [EventEngineConf](../../com.pubnub.api.eventengine/-event-engine-conf/index.md)&lt;[PresenceEffectInvocation](../../com.pubnub.api.presence.eventengine.effect/-presence-effect-invocation/index.md), [PresenceEvent](../../com.pubnub.api.presence.eventengine.event/-presence-event/index.md)&gt; |
| [subscribe](subscribe.md) | [jvm]<br>val [subscribe](subscribe.md): [EventEngineConf](../../com.pubnub.api.eventengine/-event-engine-conf/index.md)&lt;[SubscribeEffectInvocation](../../com.pubnub.api.subscribe.eventengine.effect/-subscribe-effect-invocation/index.md), [SubscribeEvent](../../com.pubnub.api.subscribe.eventengine.event/-subscribe-event/index.md)&gt; |
