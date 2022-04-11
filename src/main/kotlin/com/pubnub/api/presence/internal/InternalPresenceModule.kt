package com.pubnub.api.presence.internal

import com.pubnub.api.PubNub
import com.pubnub.api.presence.NewPresenceModule
import com.pubnub.api.state.internal.IntModule
import java.util.concurrent.LinkedBlockingQueue

internal class InternalPresenceModule(
    private val eventQueue: LinkedBlockingQueue<PresenceEvent>,
    val moduleInternals: IntModule<PresenceState, PresenceEvent, PresenceEffectInvocation>
) : NewPresenceModule {

    companion object {
        fun create(pubnub: PubNub): NewPresenceModule {
            val eventQueue = LinkedBlockingQueue<PresenceEvent>(100)
            val engineAndEffects = presenceEventEngine()
            val httpCallExecutor = HttpCallExecutor(pubnub = pubnub, eventQueue = eventQueue)
            val effectDispatcher = PresenceEffectDispatcher(
                eventQueue = eventQueue,
                httpExecutor = httpCallExecutor
            )
            val effectQueue = LinkedBlockingQueue<PresenceEffectInvocation>(100).apply {
                engineAndEffects.second.forEach { put(it) }
            }

            val moduleInternals = IntModule(
                engine = engineAndEffects.first,
                effectQueue = effectQueue,
                eventQueue = eventQueue,
                effectDispatcher = effectDispatcher
            )
            return InternalPresenceModule(
                eventQueue = eventQueue,
                moduleInternals = moduleInternals
            )
        }
    }

    override fun presence(
        channels: List<String>,
        channelGroups: List<String>,
        connected: Boolean
    ) {
        if (connected) {
            eventQueue.put(Commands.SubscribeIssued(channels = channels, groups = channelGroups))
        } else {
            eventQueue.put(Commands.UnsubscribeIssued(channels = channels, groups = channelGroups))
        }
    }

    override fun unsubscribeAll() {
        eventQueue.put(Commands.UnsubscribeAllIssued)
    }

    override fun cancel() {
        moduleInternals.cancel()
    }
}
