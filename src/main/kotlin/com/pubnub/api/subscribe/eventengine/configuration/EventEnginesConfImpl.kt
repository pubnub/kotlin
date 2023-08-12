package com.pubnub.api.subscribe.eventengine.configuration

import com.pubnub.api.eventengine.EventEnginesConf
import com.pubnub.api.eventengine.QueueSinkSource
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.eventengine.Source
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent

class EventEnginesConfImpl : EventEnginesConf {
    private val subscribeEventSinkSourceQueue = QueueSinkSource<SubscribeEvent>()
    private val subscribeEffectSinkSourceQueue = QueueSinkSource<SubscribeEffectInvocation>()
    private val presenceEventSinkSourceQueue = QueueSinkSource<PresenceEvent>()
    private val presenceEffectSinkSourceQueue = QueueSinkSource<PresenceEffectInvocation>()

    override val subscribeEventSink: Sink<SubscribeEvent> = subscribeEventSinkSourceQueue
    override val subscribeEventSource: Source<SubscribeEvent> = subscribeEventSinkSourceQueue
    override val subscribeEffectSink: Sink<SubscribeEffectInvocation> = subscribeEffectSinkSourceQueue
    override val subscribeEffectSource: Source<SubscribeEffectInvocation> = subscribeEffectSinkSourceQueue

    override val presenceEventSink: Sink<PresenceEvent> = presenceEventSinkSourceQueue
    override val presenceEventSource: Source<PresenceEvent> = presenceEventSinkSourceQueue
    override val presenceEffectSink: Sink<PresenceEffectInvocation> = presenceEffectSinkSourceQueue
    override val presenceEffectSource: Source<PresenceEffectInvocation> = presenceEffectSinkSourceQueue
}
