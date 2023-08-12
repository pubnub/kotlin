@file:Suppress("UNUSED_PARAMETER")

package com.pubnub.api.subscribe

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.eventengine.Event
import com.pubnub.api.eventengine.EventEnginesConf
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.managers.PresenceEventEngineManager
import com.pubnub.api.managers.SubscribeEventEngineManager
import com.pubnub.api.presence.eventengine.PresenceEventEngine
import com.pubnub.api.presence.eventengine.effect.PresenceEffectFactory
import com.pubnub.api.presence.eventengine.effect.effectprovider.HeartbeatProviderImpl
import com.pubnub.api.presence.eventengine.effect.effectprovider.LeaveProviderImpl
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.subscribe.eventengine.SubscribeEventEngine
import com.pubnub.api.subscribe.eventengine.data.SubscriptionData
import com.pubnub.api.subscribe.eventengine.effect.RetryPolicy
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectFactory
import com.pubnub.api.subscribe.eventengine.effect.effectprovider.HandshakeProviderImpl
import com.pubnub.api.subscribe.eventengine.effect.effectprovider.ReceiveMessagesProviderImpl
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent.SubscriptionChanged
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent.SubscriptionRestored
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.workers.SubscribeMessageProcessor
import java.util.concurrent.Executors

private const val PRESENCE_CHANNEL_SUFFIX = "-pnpres"

class Subscribe(
    private val subscribeEventEngineManager: SubscribeEventEngineManager,
    private val presenceEventEngineManager: PresenceEventEngineManager,
    private val heartbeatInterval: Int,
    private val subscriptionData: SubscriptionData = SubscriptionData()
) {
    companion object {
        internal fun create(
            pubNub: PubNub,
            listenerManager: ListenerManager,
            retryPolicy: RetryPolicy,
            eventEnginesConf: EventEnginesConf,
            messageProcessor: SubscribeMessageProcessor
        ): Subscribe {
            val subscribeEventEngineManager = createAndStartSubscribeEventEngineManager(
                pubNub,
                messageProcessor,
                eventEnginesConf,
                retryPolicy,
                listenerManager
            )
            val presenceEventEngineManager =
                createAndStartPresenceEventEngineManager(pubNub, eventEnginesConf, retryPolicy)

            return Subscribe(
                subscribeEventEngineManager,
                presenceEventEngineManager,
                pubNub.configuration.heartbeatInterval
            )
        }

        private fun createAndStartSubscribeEventEngineManager(
            pubNub: PubNub,
            messageProcessor: SubscribeMessageProcessor,
            eventEnginesConf: EventEnginesConf,
            retryPolicy: RetryPolicy,
            listenerManager: ListenerManager
        ): SubscribeEventEngineManager {
            val subscribeEffectFactory = SubscribeEffectFactory(
                handshakeProvider = HandshakeProviderImpl(pubNub),
                receiveMessagesProvider = ReceiveMessagesProviderImpl(pubNub, messageProcessor),
                subscribeEventSink = eventEnginesConf.subscribeEventSink,
                policy = retryPolicy,
                executorService = Executors.newSingleThreadScheduledExecutor(),
                messagesConsumer = listenerManager,
                statusConsumer = listenerManager
            )

            val subscribeEventEngine = SubscribeEventEngine(
                effectSink = eventEnginesConf.subscribeEffectSink,
                eventSource = eventEnginesConf.subscribeEventSource
            )
            val subscribeEffectDispatcher = EffectDispatcher(
                effectFactory = subscribeEffectFactory,
                effectSource = eventEnginesConf.subscribeEffectSource
            )

            val subscribeEventEngineManager = SubscribeEventEngineManager(
                subscribeEventEngine = subscribeEventEngine,
                effectDispatcher = subscribeEffectDispatcher,
                eventSink = eventEnginesConf.subscribeEventSink
            ).apply {
                if (pubNub.configuration.enableSubscribeBeta) {
                    start()
                }
            }
            return subscribeEventEngineManager
        }

        private fun createAndStartPresenceEventEngineManager(
            pubNub: PubNub,
            eventEnginesConf: EventEnginesConf,
            retryPolicy: RetryPolicy
        ): PresenceEventEngineManager {
            val presenceEffectFactory = PresenceEffectFactory(
                heartbeatProvider = HeartbeatProviderImpl(pubNub),
                leaveProvider = LeaveProviderImpl(pubNub),
                presenceEventSink = eventEnginesConf.presenceEventSink,
                policy = retryPolicy,
                executorService = Executors.newSingleThreadScheduledExecutor(),
                heartbeatIntervalInSec = pubNub.configuration.heartbeatInterval
            )

            val presenceEventEngine = PresenceEventEngine(
                effectSink = eventEnginesConf.presenceEffectSink,
                eventSource = eventEnginesConf.presenceEventSource
            )

            val presenceEffectDispatcher = EffectDispatcher(
                effectFactory = presenceEffectFactory,
                effectSource = eventEnginesConf.presenceEffectSource
            )

            val presenceEventEngineManager = PresenceEventEngineManager(
                presenceEventEngine = presenceEventEngine,
                effectDispatcher = presenceEffectDispatcher,
                eventSink = eventEnginesConf.presenceEventSink
            ).apply {
                if (pubNub.configuration.enableSubscribeBeta) {
                    start()
                }
            }
            return presenceEventEngineManager
        }
    }

    @Synchronized
    fun subscribe(
        channels: Set<String>,
        channelGroups: Set<String>,
        withPresence: Boolean,
        withTimetoken: Long = 0L,
    ) {
        throwExceptionIfChannelAndChannelGroupIsMissing(channels, channelGroups)
        addChannelsToSubscriptionData(channels, withPresence)
        addChannelGroupsToSubscriptionData(channelGroups, withPresence)
        val channelsInLocalStorage = subscriptionData.channels
        val channelGroupsInLocalStorage = subscriptionData.channelGroups
        if (withTimetoken != 0L) {
            val subscriptionRestoredEvent = SubscriptionRestored(
                channelsInLocalStorage,
                channelGroupsInLocalStorage,
                SubscriptionCursor(withTimetoken, "42") // todo handle region should be 0 or null?
            )
            subscribeEventEngineManager.addEventToQueue(subscriptionRestoredEvent)
            addPresenceEventToQueueIfHeartbeatEnabled(PresenceEvent.Joined(channels, channelGroups))
        } else {
            subscribeEventEngineManager.addEventToQueue(
                SubscriptionChanged(
                    channelsInLocalStorage,
                    channelGroupsInLocalStorage
                )
            )
            addPresenceEventToQueueIfHeartbeatEnabled(PresenceEvent.Joined(channels, channelGroups))
        }
    }

    @Synchronized
    fun unsubscribe(
        channels: Set<String> = emptySet(),
        channelGroups: Set<String> = emptySet()
    ) {
        throwExceptionIfChannelAndChannelGroupIsMissing(channels, channelGroups)
        removeChannelsFromSubscriptionData(channels)
        removeChannelGroupsFromSubscriptionData(channelGroups)

        if (subscriptionData.channels.size > 0 || subscriptionData.channelGroups.size > 0) {
            val channelsInLocalStorage = subscriptionData.channels
            val channelGroupsInLocalStorage = subscriptionData.channelGroups
            subscribeEventEngineManager.addEventToQueue(
                SubscriptionChanged(
                    channelsInLocalStorage,
                    channelGroupsInLocalStorage
                )
            )
            addPresenceEventToQueueIfHeartbeatEnabled(PresenceEvent.Left(channels, channelGroups))
        } else {
            subscribeEventEngineManager.addEventToQueue(SubscribeEvent.UnsubscribeAll)
            addPresenceEventToQueueIfHeartbeatEnabled(PresenceEvent.LeftAll)
        }
    }

    @Synchronized
    fun unsubscribeAll() {
        removeAllChannelsFromLocalStorage()
        removeAllChannelGroupsFromLocalStorage()
        subscribeEventEngineManager.addEventToQueue(SubscribeEvent.UnsubscribeAll)
        addPresenceEventToQueueIfHeartbeatEnabled(PresenceEvent.LeftAll)
    }

    @Synchronized
    fun getSubscribedChannels(): List<String> {
        return subscriptionData.channels.toList()
    }

    @Synchronized
    fun getSubscribedChannelGroups(): List<String> {
        return subscriptionData.channelGroups.toList()
    }

    fun disconnect() {
        subscribeEventEngineManager.addEventToQueue(SubscribeEvent.Disconnect)
        addPresenceEventToQueueIfHeartbeatEnabled(PresenceEvent.Disconnect)
    }

    fun reconnect() {
        subscribeEventEngineManager.addEventToQueue(SubscribeEvent.Reconnect)
        addPresenceEventToQueueIfHeartbeatEnabled(PresenceEvent.Reconnect)
    }

    @Synchronized
    fun destroy() {
        // todo do we want to have "force" flag?
        disconnect()
        subscribeEventEngineManager.stop()
        presenceEventEngineManager.stop()
    }

    private fun throwExceptionIfChannelAndChannelGroupIsMissing(
        channels: Set<String>,
        channelGroups: Set<String>
    ) {
        if (channels.isEmpty() && channelGroups.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_OR_CHANNEL_GROUP_MISSING)
        }
    }

    private fun addChannelsToSubscriptionData(channels: Set<String>, withPresence: Boolean) {
        subscriptionData.channels.addAll(channels)
        if (withPresence) {
            channels.forEach {
                val presenceChannel = "$it$PRESENCE_CHANNEL_SUFFIX"
                subscriptionData.channels.add(presenceChannel)
            }
        }
    }

    private fun addChannelGroupsToSubscriptionData(channelGroups: Set<String>, withPresence: Boolean) {
        subscriptionData.channelGroups.addAll(channelGroups)
        if (withPresence) {
            channelGroups.forEach {
                val presenceChannelGroup = "$it$PRESENCE_CHANNEL_SUFFIX"
                subscriptionData.channelGroups.add(presenceChannelGroup)
            }
        }
    }

    private fun removeChannelGroupsFromSubscriptionData(channelGroups: Set<String>) {
        channelGroups.forEach {
            subscriptionData.channelGroups.remove(it)
            val presenceChannelGroup = "$it$PRESENCE_CHANNEL_SUFFIX"
            subscriptionData.channels.remove(presenceChannelGroup)
        }
    }

    private fun removeChannelsFromSubscriptionData(channels: Set<String>) {
        channels.forEach {
            subscriptionData.channels.remove(it)
            val presenceChannel = "$it$PRESENCE_CHANNEL_SUFFIX"
            subscriptionData.channels.remove(presenceChannel)
        }
    }

    private fun removeAllChannelsFromLocalStorage() {
        subscriptionData.channels.clear()
    }

    private fun removeAllChannelGroupsFromLocalStorage() {
        subscriptionData.channelGroups.clear()
    }

    private fun addPresenceEventToQueueIfHeartbeatEnabled(presenceEvent: Event) {
        if (heartbeatInterval > 0) {
            presenceEventEngineManager.addEventToQueue(presenceEvent)
        }
    }
}
