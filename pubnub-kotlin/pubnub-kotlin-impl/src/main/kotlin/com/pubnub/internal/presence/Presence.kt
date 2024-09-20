package com.pubnub.internal.presence

import com.pubnub.api.enums.PNHeartbeatNotificationOptions
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
import org.slf4j.LoggerFactory
import java.util.concurrent.ScheduledExecutorService
import kotlin.time.Duration

internal interface Presence {
    companion object {
        internal fun create(
            heartbeatProvider: HeartbeatProvider,
            leaveProvider: LeaveProvider,
            heartbeatInterval: Duration,
            suppressLeaveEvents: Boolean,
            heartbeatNotificationOptions: PNHeartbeatNotificationOptions,
            listenerManager: ListenerManager,
            eventEngineConf: EventEngineConf<PresenceEffectInvocation, PresenceEvent>,
            presenceData: PresenceData = PresenceData(),
            sendStateWithHeartbeat: Boolean,
            executorService: ScheduledExecutorService,
        ): Presence {
            if (heartbeatInterval <= Duration.ZERO) {
                return PresenceNoOp(suppressLeaveEvents, leaveProvider)
            }

            val effectFactory =
                PresenceEffectFactory(
                    heartbeatProvider = heartbeatProvider,
                    leaveProvider = leaveProvider,
                    presenceEventSink = eventEngineConf.eventSink,
                    executorService = executorService,
                    heartbeatInterval = heartbeatInterval,
                    suppressLeaveEvents = suppressLeaveEvents,
                    heartbeatNotificationOptions = heartbeatNotificationOptions,
                    statusConsumer = listenerManager,
                    presenceData = presenceData,
                    sendStateWithHeartbeat = sendStateWithHeartbeat,
                )

            val eventEngineManager =
                PresenceEventEngineManager(
                    eventEngine =
                        PresenceEventEngine(
                            effectSink = eventEngineConf.effectSink,
                            eventSource = eventEngineConf.eventSource,
                        ),
                    eventSink = eventEngineConf.eventSink,
                    effectDispatcher =
                        EffectDispatcher(
                            effectFactory = effectFactory,
                            effectSource = eventEngineConf.effectSource,
                        ),
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
        channelGroups: Set<String> = emptySet(),
    )

    fun leftAll()

    fun presence(
        channels: Set<String> = emptySet(),
        channelGroups: Set<String> = emptySet(),
        connected: Boolean = false,
    ) {
        if (connected) {
            joined(channels, channelGroups)
        } else {
            left(channels, channelGroups)
        }
    }

    fun reconnect()

    fun disconnect()

    fun destroy()
}

internal class PresenceNoOp(
    private val suppressLeaveEvents: Boolean = false,
    private val leaveProvider: LeaveProvider,
) : Presence {
    private val log = LoggerFactory.getLogger(PresenceNoOp::class.java)
    private val channels = mutableSetOf<String>()
    private val channelGroups = mutableSetOf<String>()

    @Synchronized
    override fun joined(
        channels: Set<String>,
        channelGroups: Set<String>,
    ) {
        this.channels.addAll(channels)
        this.channelGroups.addAll(channelGroups)
    }

    @Synchronized
    override fun left(
        channels: Set<String>,
        channelGroups: Set<String>,
    ) {
        if (!suppressLeaveEvents && (channels.isNotEmpty() || channelGroups.isNotEmpty())) {
            leaveProvider.getLeaveRemoteAction(channels, channelGroups).async { result ->
                result.onFailure {
                    log.error("LeaveEffect failed", it)
                }
            }
        }
        this.channels.removeAll(channels)
        this.channelGroups.removeAll(channelGroups)
    }

    @Synchronized
    override fun leftAll() {
        if (!suppressLeaveEvents && (channels.isNotEmpty() || channelGroups.isNotEmpty())) {
            leaveProvider.getLeaveRemoteAction(channels, channelGroups).async { result ->
                result.onFailure {
                    log.error("LeaveEffect failed", it)
                }
            }
        }
        channels.clear()
        channelGroups.clear()
    }

    override fun reconnect() = noAction()

    override fun disconnect() = noAction()

    override fun destroy() = noAction()

    private fun noAction() {
        // Presence Event Engine is not initialized so no action here
    }
}

internal class EnabledPresence(
    private val presenceEventEngineManager: PresenceEventEngineManager,
) : Presence {
    override fun joined(
        channels: Set<String>,
        channelGroups: Set<String>,
    ) {
        presenceEventEngineManager.addEventToQueue(PresenceEvent.Joined(channels, channelGroups))
    }

    override fun left(
        channels: Set<String>,
        channelGroups: Set<String>,
    ) {
        presenceEventEngineManager.addEventToQueue(PresenceEvent.Left(channels, channelGroups))
    }

    override fun leftAll() {
        presenceEventEngineManager.addEventToQueue(PresenceEvent.LeftAll)
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
