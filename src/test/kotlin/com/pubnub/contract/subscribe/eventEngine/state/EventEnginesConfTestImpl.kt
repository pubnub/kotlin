package com.pubnub.contract.subscribe.eventEngine.state

import com.pubnub.api.eventengine.EventEnginesConf
import com.pubnub.api.eventengine.QueueSinkSource
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.eventengine.Source
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent

class EventEnginesConfTestImpl(
    subscribeQueuedElements: MutableList<Pair<String, String>>,
    presenceQueuedElements: MutableList<Pair<String, String>>,
) : EventEnginesConf {

    private val subscribeEventSinkSource = TestSinkSource<SubscribeEvent>(subscribeQueuedElements, QueueSinkSource())
    private val subscribeEffectSinkSource = TestSinkSource<SubscribeEffectInvocation>(subscribeQueuedElements, QueueSinkSource())
    private val presenceEventSinkSource = TestSinkSource<PresenceEvent>(presenceQueuedElements, QueueSinkSource())
    private val presenceEffectSinkSource = TestSinkSource<PresenceEffectInvocation>(presenceQueuedElements, QueueSinkSource())

    override val subscribeEventSink: Sink<SubscribeEvent> = subscribeEventSinkSource
    override val subscribeEventSource: Source<SubscribeEvent> = subscribeEventSinkSource
    override val subscribeEffectSink: Sink<SubscribeEffectInvocation> = subscribeEffectSinkSource
    override val subscribeEffectSource: Source<SubscribeEffectInvocation> = subscribeEffectSinkSource
    override val presenceEventSink: Sink<PresenceEvent> = presenceEventSinkSource
    override val presenceEventSource: Source<PresenceEvent> = presenceEventSinkSource
    override val presenceEffectSink: Sink<PresenceEffectInvocation> = presenceEffectSinkSource
    override val presenceEffectSource: Source<PresenceEffectInvocation> = presenceEffectSinkSource
}
