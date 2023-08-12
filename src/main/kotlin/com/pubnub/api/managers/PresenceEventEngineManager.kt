package com.pubnub.api.managers

import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.eventengine.Event
import com.pubnub.api.eventengine.EventEngineManager
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.presence.eventengine.PresenceEventEngine
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent

class PresenceEventEngineManager(
    private val presenceEventEngine: PresenceEventEngine,
    private val effectDispatcher: EffectDispatcher<PresenceEffectInvocation>,
    private val eventSink: Sink<PresenceEvent>
) : EventEngineManager {

    override fun addEventToQueue(event: Event) {
        eventSink.add(event as PresenceEvent)
    }

    override fun start() {
        presenceEventEngine.start()
        effectDispatcher.start()
    }

    override fun stop() {
        presenceEventEngine.stop()
        effectDispatcher.stop()
    }
}
