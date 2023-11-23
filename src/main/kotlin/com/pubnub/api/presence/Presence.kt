package com.pubnub.api.presence

import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.eventengine.EventEngineConf
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.managers.PresenceEventEngineManager
import com.pubnub.api.presence.eventengine.PresenceEventEngine
import com.pubnub.api.presence.eventengine.data.PresenceData
import com.pubnub.api.presence.eventengine.effect.PresenceEffectFactory
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.effect.effectprovider.HeartbeatProvider
import com.pubnub.api.presence.eventengine.effect.effectprovider.LeaveProvider
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.retry.RetryConfiguration
import org.slf4j.LoggerFactory
import java.util.concurrent.ScheduledExecutorService
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
            listenerManager: ListenerManager,
            eventEngineConf: EventEngineConf<PresenceEffectInvocation, PresenceEvent>,
            presenceData: PresenceData = PresenceData(),
            sendStateWithHeartbeat: Boolean,
            executorService: ScheduledExecutorService
        ): Presence {
            if (heartbeatInterval <= Duration.ZERO || !enableEventEngine) {
                return PresenceNoOp(suppressLeaveEvents, leaveProvider)
            }

            val effectFactory = PresenceEffectFactory(
                heartbeatProvider = heartbeatProvider,
                leaveProvider = leaveProvider,
                presenceEventSink = eventEngineConf.eventSink,
                retryConfiguration = retryConfiguration,
                executorService = executorService,
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

    fun reconnect()

    fun disconnect()

    fun destroy()
}

internal class PresenceNoOp(private val suppressLeaveEvents: Boolean = false, private val leaveProvider: LeaveProvider) : Presence {
    private val log = LoggerFactory.getLogger(PresenceNoOp::class.java)
    private val channels = mutableSetOf<String>()
    private val channelGroups = mutableSetOf<String>()

    @Synchronized
    override fun joined(channels: Set<String>, channelGroups: Set<String>) {
        this.channels.addAll(channels)
        this.channelGroups.addAll(channelGroups)
    }

    @Synchronized
    override fun left(channels: Set<String>, channelGroups: Set<String>) {
        if (!suppressLeaveEvents) {
            leaveProvider.getLeaveRemoteAction(channels, channelGroups).async { _, status ->
                if (status.error) {
                    log.error("LeaveEffect failed", status.exception)
                }
            }
        }
        this.channels.removeAll(channels)
        this.channelGroups.removeAll(channelGroups)
    }

    @Synchronized
    override fun leftAll() {
        if (!suppressLeaveEvents && (channels.isNotEmpty() || channelGroups.isNotEmpty())) {
            leaveProvider.getLeaveRemoteAction(channels, channelGroups).async { _, status ->
                if (status.error) {
                    log.error("LeaveEffect failed", status.exception)
                }
            }
        }
        channels.clear()
        channelGroups.clear()
    }

    override fun presence(channels: Set<String>, channelGroups: Set<String>, connected: Boolean) = noAction()

    override fun reconnect() = noAction()

    override fun disconnect() = noAction()

    override fun destroy() = noAction()

    private fun noAction() {
        // Presence Event Engine is not initialized so no action here
    }
}

internal class EnabledPresence(
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
