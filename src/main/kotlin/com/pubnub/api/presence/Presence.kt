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
import com.pubnub.api.subscribe.eventengine.effect.RetryPolicy
import java.time.Duration
import java.util.concurrent.Executors

internal interface Presence {
    companion object {
        internal fun create(
            heartbeatProvider: HeartbeatProvider,
            leaveProvider: LeaveProvider,
            heartbeatInterval: Duration,
            enableEventEngine: Boolean,
            retryPolicy: RetryPolicy,
            suppressLeaveEvents: Boolean,
            heartbeatNotificationOptions: PNHeartbeatNotificationOptions,
            listenerManager: ListenerManager,
            eventEngineConf: EventEngineConf<PresenceEffectInvocation, PresenceEvent>,
            presenceData: PresenceData = PresenceData()
        ): Presence {
            if (heartbeatInterval <= Duration.ZERO || !enableEventEngine) {
                return PresenceNoOp()
            }

            val effectFactory = PresenceEffectFactory(
                heartbeatProvider = heartbeatProvider,
                leaveProvider = leaveProvider,
                presenceEventSink = eventEngineConf.eventSink,
                policy = retryPolicy,
                executorService = Executors.newSingleThreadScheduledExecutor(),
                heartbeatInterval = heartbeatInterval,
                suppressLeaveEvents = suppressLeaveEvents,
                heartbeatNotificationOptions = heartbeatNotificationOptions,
                statusConsumer = listenerManager
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

    @Synchronized
    override fun joined(
        channels: Set<String>,
        channelGroups: Set<String>,
    ) {
        addChannelsToPresenceData(channels)
        addChannelGroupsToPresenceData(channelGroups)
        presenceEventEngineManager.addEventToQueue(PresenceEvent.Joined(channels, channelGroups))
    }

    @Synchronized
    override fun left(
        channels: Set<String>,
        channelGroups: Set<String>
    ) {
        removeChannelsFromPresenceData(channels)
        removeChannelGroupsFromPresenceData(channelGroups)
        if (presenceData.channels.size > 0 || presenceData.channelGroups.size > 0) {
            presenceEventEngineManager.addEventToQueue(PresenceEvent.Left(channels, channelGroups))
        } else {
            presenceEventEngineManager.addEventToQueue(PresenceEvent.LeftAll)
        }
    }

    @Synchronized
    override fun leftAll() {
        removeAllChannelsFromPresenceData()
        removeAllChannelGroupsFromPresenceData()
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

    @Synchronized
    override fun destroy() {
        disconnect()
        presenceEventEngineManager.stop()
    }

    private fun addChannelsToPresenceData(channels: Set<String>) {
        presenceData.channels.addAll(channels)
    }

    private fun addChannelGroupsToPresenceData(channelGroups: Set<String>) {
        presenceData.channelGroups.addAll(channelGroups)
    }

    private fun removeChannelsFromPresenceData(channels: Set<String>) {
        presenceData.channels.removeAll(channels)
    }

    private fun removeChannelGroupsFromPresenceData(channelGroups: Set<String>) {
        presenceData.channelGroups.removeAll(channelGroups)
    }

    private fun removeAllChannelsFromPresenceData() {
        presenceData.channels.clear()
    }

    private fun removeAllChannelGroupsFromPresenceData() {
        presenceData.channelGroups.clear()
    }
}
