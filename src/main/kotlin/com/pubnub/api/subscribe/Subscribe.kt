package com.pubnub.api.subscribe

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.managers.SubscribeEventEngineManager
import com.pubnub.api.presence.eventengine.data.PresenceData
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.subscribe.eventengine.SubscribeEventEngine
import com.pubnub.api.subscribe.eventengine.configuration.EventEnginesConf
import com.pubnub.api.subscribe.eventengine.data.SubscriptionData
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

internal class Subscribe(
    private val subscribeEventEngineManager: SubscribeEventEngineManager,
    private val presenceData: PresenceData,
    private val subscriptionData: SubscriptionData = SubscriptionData(),
) {
    companion object {
        internal fun create(
            pubNub: PubNub,
            listenerManager: ListenerManager,
            retryConfiguration: RetryConfiguration,
            eventEnginesConf: EventEnginesConf,
            messageProcessor: SubscribeMessageProcessor,
            presenceData: PresenceData,
            sendStateWithSubscribe: Boolean,
        ): Subscribe {
            val subscribeEventEngineManager = createAndStartSubscribeEventEngineManager(
                pubNub,
                messageProcessor,
                eventEnginesConf,
                retryConfiguration,
                listenerManager,
                presenceData,
                sendStateWithSubscribe = sendStateWithSubscribe,
            )

            return Subscribe(subscribeEventEngineManager, presenceData)
        }

        private fun createAndStartSubscribeEventEngineManager(
            pubNub: PubNub,
            messageProcessor: SubscribeMessageProcessor,
            eventEnginesConf: EventEnginesConf,
            retryConfiguration: RetryConfiguration,
            listenerManager: ListenerManager,
            presenceData: PresenceData,
            sendStateWithSubscribe: Boolean,
        ): SubscribeEventEngineManager {
            val subscribeEffectFactory = SubscribeEffectFactory(
                handshakeProvider = HandshakeProviderImpl(pubNub),
                receiveMessagesProvider = ReceiveMessagesProviderImpl(pubNub, messageProcessor),
                subscribeEventSink = eventEnginesConf.subscribe.eventSink,
                retryConfiguration = retryConfiguration,
                executorService = Executors.newSingleThreadScheduledExecutor(),
                messagesConsumer = listenerManager,
                statusConsumer = listenerManager,
                presenceData = presenceData,
                sendStateWithSubscribe = sendStateWithSubscribe,
            )

            val subscribeEventEngine = SubscribeEventEngine(
                effectSink = eventEnginesConf.subscribe.effectSink,
                eventSource = eventEnginesConf.subscribe.eventSource
            )
            val subscribeEffectDispatcher = EffectDispatcher(
                effectFactory = subscribeEffectFactory,
                effectSource = eventEnginesConf.subscribe.effectSource
            )

            val subscribeEventEngineManager = SubscribeEventEngineManager(
                eventEngine = subscribeEventEngine,
                effectDispatcher = subscribeEffectDispatcher,
                eventSink = eventEnginesConf.subscribe.eventSink
            ).apply {
                if (pubNub.configuration.enableEventEngine) {
                    start()
                }
            }
            return subscribeEventEngineManager
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
                SubscriptionCursor(
                    withTimetoken,
                    region = null
                ) // we don't know region here. Subscribe response will return region.
            )
            subscribeEventEngineManager.addEventToQueue(subscriptionRestoredEvent)
        } else {
            subscribeEventEngineManager.addEventToQueue(
                SubscriptionChanged(
                    channelsInLocalStorage,
                    channelGroupsInLocalStorage
                )
            )
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

        presenceData.channelStates.keys.removeAll(channels)

        if (subscriptionData.channels.size > 0 || subscriptionData.channelGroups.size > 0) {
            val channelsInLocalStorage = subscriptionData.channels
            val channelGroupsInLocalStorage = subscriptionData.channelGroups
            subscribeEventEngineManager.addEventToQueue(
                SubscriptionChanged(
                    channelsInLocalStorage,
                    channelGroupsInLocalStorage
                )
            )
        } else {
            subscribeEventEngineManager.addEventToQueue(SubscribeEvent.UnsubscribeAll)
        }
    }

    @Synchronized
    fun unsubscribeAll() {
        removeAllChannelsFromLocalStorage()
        removeAllChannelGroupsFromLocalStorage()
        presenceData.channelStates.clear()
        subscribeEventEngineManager.addEventToQueue(SubscribeEvent.UnsubscribeAll)
    }

    @Synchronized
    fun getSubscribedChannels(): List<String> {
        return subscriptionData.channels.toList().filter { !it.contains(PRESENCE_CHANNEL_SUFFIX) }
    }

    @Synchronized
    fun getSubscribedChannelGroups(): List<String> {
        return subscriptionData.channelGroups.toList().filter { !it.contains(PRESENCE_CHANNEL_SUFFIX) }
    }

    fun disconnect() {
        subscribeEventEngineManager.addEventToQueue(SubscribeEvent.Disconnect)
    }

    fun reconnect(timetoken: Long = 0L) {
        val event = if (timetoken != 0L) {
            SubscribeEvent.Reconnect(SubscriptionCursor(timetoken, region = null))
        } else {
            SubscribeEvent.Reconnect()
        }
        subscribeEventEngineManager.addEventToQueue(event)
    }

    @Synchronized
    fun destroy() {
        disconnect()
        subscribeEventEngineManager.stop()
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
            subscriptionData.channelGroups.remove(presenceChannelGroup)
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
}
