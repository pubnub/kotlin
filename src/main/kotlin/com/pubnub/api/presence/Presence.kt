package com.pubnub.api.presence

import com.pubnub.api.PubNub
import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.eventengine.EventEngineConf
import com.pubnub.api.managers.PresenceEventEngineManager
import com.pubnub.api.presence.eventengine.PresenceEventEngine
import com.pubnub.api.presence.eventengine.effect.PresenceEffectFactory
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.effect.effectprovider.HeartbeatProviderImpl
import com.pubnub.api.presence.eventengine.effect.effectprovider.LeaveProviderImpl
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.subscribe.eventengine.effect.RetryPolicy
import java.time.Duration
import java.util.concurrent.Executors

interface Presence {
    companion object {
        internal fun create(
            pubNub: PubNub,
            retryPolicy: RetryPolicy,
            eventEngineConf: EventEngineConf<PresenceEffectInvocation, PresenceEvent>,
        ): Presence {
            if (pubNub.configuration.heartbeatInterval <= 0 || !pubNub.configuration.enableSubscribeBeta) {
                return PresenceNoOp()
            }

            val effectFactory = PresenceEffectFactory(
                heartbeatProvider = HeartbeatProviderImpl(pubNub),
                leaveProvider = LeaveProviderImpl(pubNub),
                presenceEventSink = eventEngineConf.eventSink,
                policy = retryPolicy,
                executorService = Executors.newSingleThreadScheduledExecutor(),
                heartbeatInterval = Duration.ofSeconds(pubNub.configuration.heartbeatInterval.toLong())
            )

            val eventEngineManager = PresenceEventEngineManager(
                eventEngine = PresenceEventEngine(
                    effectSink = eventEngineConf.effectSink,
                    eventSource = eventEngineConf.eventSource,
                ),
                eventSink = eventEngineConf.eventSink,
                effectDispatcher = EffectDispatcher(
                    effectFactory = effectFactory, effectSource = eventEngineConf.effectSource
                )
            ).also { it.start() }

            return EnabledPresence(eventEngineManager)
        }
    }

    fun joined(
        channels: Set<String> = emptySet(),
        channelGroups: Set<String> = emptySet(),
    )

    fun left(
        channels: Set<String> = emptySet(),
        channelGroups: Set<String> = emptySet()
    )

    fun leftAll()

    fun presence(
        channels: Set<String> = emptySet(),
        channelGroups: Set<String> = emptySet(),
        connected: Boolean = false
    )

    fun destroy()
}

class PresenceNoOp : Presence {
    override fun joined(channels: Set<String>, channelGroups: Set<String>) {}

    override fun left(channels: Set<String>, channelGroups: Set<String>) {}

    override fun leftAll() {}

    override fun presence(channels: Set<String>, channelGroups: Set<String>, connected: Boolean) {}

    override fun destroy() {}
}

class EnabledPresence(
    private val presenceEventEngineManager: PresenceEventEngineManager
) : Presence {

    override fun joined(
        channels: Set<String>,
        channelGroups: Set<String>,
    ) {
        presenceEventEngineManager.addEventToQueue(PresenceEvent.Joined(channels, channelGroups))
    }

    override fun left(
        channels: Set<String>,
        channelGroups: Set<String>
    ) {
        presenceEventEngineManager.addEventToQueue(PresenceEvent.Left(channels, channelGroups))
    }

    override fun leftAll() {
        presenceEventEngineManager.addEventToQueue(PresenceEvent.LeftAll)
    }

    override fun presence(
        channels: Set<String>,
        channelGroups: Set<String>,
        connected: Boolean
    ) {
        if (connected) {
            joined(channels, channelGroups)
        } else {
            left(channels, channelGroups)
        }
    }

    override fun destroy() {
        presenceEventEngineManager.stop()
    }
}
