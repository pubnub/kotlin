package com.pubnub.api.presence.internal

import com.pubnub.api.PubNub
import com.pubnub.api.presence.NewPresenceModule
import com.pubnub.api.state.EffectDispatcher
import com.pubnub.api.state.internal.QueuedEventEngine
import java.util.concurrent.*

internal class InternalPresenceModule(
    private val eventEngine: QueuedEventEngine<PresenceState, PresenceEvent, PresenceEffectInvocation>,
    private val effectDispatcher: EffectDispatcher<PresenceEffectInvocation>
) : NewPresenceModule {

    companion object {
        fun create(pubnub: PubNub): NewPresenceModule {
            val eventQueue = LinkedBlockingQueue<PresenceEvent>(100)
            val effectQueue = LinkedBlockingQueue<PresenceEffectInvocation>(100)
            val eventEngine = queuedPresenceEventEngine(eventQueue = eventQueue, effectQueue = effectQueue)
            val effectEngine = PresenceEffectDispatcher.create(
                pubnub = pubnub, eventQueue = eventQueue, effectQueue = effectQueue
            )
            return InternalPresenceModule(
                eventEngine = eventEngine, effectDispatcher = effectEngine
            )
        }
    }

    override fun presence(
        channels: List<String>, channelGroups: List<String>, connected: Boolean
    ) {
        if (connected) {
            eventEngine.handle(Commands.SubscribeIssued(channels = channels, groups = channelGroups))
        } else {
            eventEngine.handle(Commands.UnsubscribeIssued(channels = channels, groups = channelGroups))
        }
    }

    override fun unsubscribeAll() {
        eventEngine.handle(Commands.UnsubscribeAllIssued)
    }

    override fun cancel() {
        eventEngine.cancel()
        effectDispatcher.cancel()
    }
}
