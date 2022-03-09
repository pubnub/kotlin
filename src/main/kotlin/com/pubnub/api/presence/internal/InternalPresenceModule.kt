package com.pubnub.api.presence.internal

import com.pubnub.api.PubNub
import com.pubnub.api.presence.NewPresenceModule
import com.pubnub.api.state.EffectEngine
import com.pubnub.api.state.QueuedEventEngine
import java.util.concurrent.*

internal class InternalPresenceModule(
    private val eventEngine: QueuedEventEngine<PresenceState, PresenceEvent, PresenceEffect>,
    private val effectEngine: EffectEngine<PresenceEffect>
) : NewPresenceModule {

    companion object {
        fun create(pubnub: PubNub): NewPresenceModule {
            val eventQueue = LinkedBlockingQueue<PresenceEvent>(100)
            val effectQueue = LinkedBlockingQueue<PresenceEffect>(100)
            val eventEngine = queuedPresenceEventEngine(eventQueue = eventQueue, effectQueue = effectQueue)
            val effectEngine = PresenceEffectEngine.create(
                pubnub = pubnub, eventQueue = eventQueue, effectQueue = effectQueue
            )
            return InternalPresenceModule(
                eventEngine = eventEngine, effectEngine = effectEngine
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
        effectEngine.cancel()
    }
}
