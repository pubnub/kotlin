package com.pubnub.internal.presence

import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.internal.PubNub
import com.pubnub.internal.eventengine.EffectDispatcher
import com.pubnub.internal.eventengine.EventEngineConf
import com.pubnub.internal.managers.ListenerManager
import com.pubnub.internal.managers.PresenceEventEngineManager
import com.pubnub.internal.presence.eventengine.PresenceEventEngine
import com.pubnub.internal.presence.eventengine.data.PresenceData
import com.pubnub.internal.presence.eventengine.effect.PresenceEffectFactory
import com.pubnub.internal.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.internal.presence.eventengine.effect.effectprovider.HeartbeatProvider
import com.pubnub.internal.presence.eventengine.effect.effectprovider.LeaveProvider
import com.pubnub.internal.presence.eventengine.event.PresenceEvent
import java.util.concurrent.Executors
import kotlin.time.Duration

internal interface Presence {
    companion object {
        internal fun create(
            heartbeatProvider: HeartbeatProvider,
            leaveProvider: LeaveProvider,
            heartbeatInterval: Duration,
            enableEventEngine: Boolean,
            retryConfiguration: RetryConfiguration,
            suppressLeaveEvents: Boolean,
            heartbeatNotificationOptions: PNHeartbeatNotificationOptions,
            listenerManager: ListenerManager<PubNub>,
            eventEngineConf: EventEngineConf<PresenceEffectInvocation, PresenceEvent>,
            presenceData: PresenceData = PresenceData(),
            sendStateWithHeartbeat: Boolean,
        ): Presence {
            if (heartbeatInterval <= Duration.ZERO || !enableEventEngine) {
                return PresenceNoOp()
            }

            val effectFactory = PresenceEffectFactory(
                heartbeatProvider = heartbeatProvider,
                leaveProvider = leaveProvider,
                presenceEventSink = eventEngineConf.eventSink,
                retryConfiguration = retryConfiguration,
                executorService = Executors.newSingleThreadScheduledExecutor(),
                heartbeatInterval = heartbeatInterval,
                suppressLeaveEvents = suppressLeaveEvents,
                heartbeatNotificationOptions = heartbeatNotificationOptions,
                statusConsumer = listenerManager,
                presenceData = presenceData,
                sendStateWithHeartbeat = sendStateWithHeartbeat,
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

            return EnabledPresence(eventEngineManager, presenceData)
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

    fun reconnect()

    fun disconnect()

    fun destroy()
}

internal class PresenceNoOp : Presence {
    override fun joined(channels: Set<String>, channelGroups: Set<String>) = noAction()

    override fun left(channels: Set<String>, channelGroups: Set<String>) = noAction()

    override fun leftAll() = noAction()

    override fun presence(channels: Set<String>, channelGroups: Set<String>, connected: Boolean) = noAction()

    override fun reconnect() = noAction()

    override fun disconnect() = noAction()

    override fun destroy() = noAction()

    private fun noAction() {
        // Presence Event Engine is not initialized so no action here
    }
}

internal class EnabledPresence(
    private val presenceEventEngineManager: PresenceEventEngineManager,
    private val presenceData: PresenceData = PresenceData()
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
        presenceData.channelStates.keys.removeAll(channels)
        presenceEventEngineManager.addEventToQueue(PresenceEvent.Left(channels, channelGroups))
    }

    override fun leftAll() {
        presenceData.channelStates.clear()
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

    override fun reconnect() {
        presenceEventEngineManager.addEventToQueue(PresenceEvent.Reconnect)
    }

    override fun disconnect() {
        presenceEventEngineManager.addEventToQueue(PresenceEvent.Disconnect)
    }

    override fun destroy() {
        disconnect()
        presenceEventEngineManager.stop()
    }
}
