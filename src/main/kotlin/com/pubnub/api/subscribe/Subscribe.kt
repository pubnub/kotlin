package com.pubnub.api.subscribe

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.managers.SubscribeEventEngineManager
import com.pubnub.api.subscribe.eventengine.SubscribeEventEngine
import com.pubnub.api.subscribe.eventengine.configuration.EventEnginesConf
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

            return Subscribe(subscribeEventEngineManager)
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
                subscribeEventSink = eventEnginesConf.subscribe.eventSink,
                policy = retryPolicy,
                executorService = Executors.newSingleThreadScheduledExecutor(),
                messagesConsumer = listenerManager,
                statusConsumer = listenerManager
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
                SubscriptionCursor(withTimetoken, "42") // todo handle region should be 0 or null?
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
        subscribeEventEngineManager.addEventToQueue(SubscribeEvent.UnsubscribeAll)
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
    }

    fun reconnect() {
        subscribeEventEngineManager.addEventToQueue(SubscribeEvent.Reconnect)
    }

    @Synchronized
    fun destroy() {
        // todo do we want to have "force" flag?
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
}
