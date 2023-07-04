@file:Suppress("UNUSED_PARAMETER")

package com.pubnub.api.subscribe

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.pubsub.Subscribe
import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.eventengine.EventEngineConf
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.managers.EventEngineManager
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.managers.SubscriptionState
import com.pubnub.api.models.SubscriptionItem
import com.pubnub.api.subscribe.eventengine.SubscribeEventEngine
import com.pubnub.api.subscribe.eventengine.effect.MessagesConsumer
import com.pubnub.api.subscribe.eventengine.effect.RetryPolicy
import com.pubnub.api.subscribe.eventengine.effect.StatusConsumer
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectFactory
import com.pubnub.api.subscribe.eventengine.effect.effectprovider.HandshakeProviderImpl
import com.pubnub.api.subscribe.eventengine.effect.effectprovider.ReceiveMessagesProviderImpl
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.Event.SubscriptionChanged
import com.pubnub.api.subscribe.eventengine.event.Event.SubscriptionRestored
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.workers.SubscribeMessageProcessor
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class Subscribe(
    private val eventEngineManager: EventEngineManager,
    private val stateManager: SubscriptionState = SubscriptionState()
) {

    companion object {
        internal fun create(
            pubNub: PubNub,
            listenerManager: ListenerManager,
            retryPolicy: RetryPolicy,
            eventEngineConf: EventEngineConf,
            messageProcessor: SubscribeMessageProcessor
        ): Subscribe {
            val handshakeProvider = HandshakeProviderImpl(pubNub)
            val receiveMessagesProvider = ReceiveMessagesProviderImpl(pubNub, messageProcessor)
            val eventSink: Sink<Event> = eventEngineConf.eventSink
            val executorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
            val messagesConsumer: MessagesConsumer = listenerManager
            val statusConsumer: StatusConsumer = listenerManager

            val subscribeEffectFactory = SubscribeEffectFactory(
                handshakeProvider,
                receiveMessagesProvider,
                eventSink,
                retryPolicy,
                executorService,
                messagesConsumer,
                statusConsumer
            )

            val subscribeEventEngine = SubscribeEventEngine(
                effectSink = eventEngineConf.effectSink,
                eventSource = eventEngineConf.eventSource
            )
            val effectDispatcher = EffectDispatcher(
                effectFactory = subscribeEffectFactory,
                effectSource = eventEngineConf.effectSource
            )

            val eventEngineManager = EventEngineManager(
                subscribeEventEngine = subscribeEventEngine,
                effectDispatcher = effectDispatcher,
                eventSink = eventEngineConf.eventSink
            ).apply {
                if (pubNub.configuration.enableSubscribeBeta) {
                    start()
                }
            }

            return Subscribe(eventEngineManager)
        }
    }

    fun subscribe(
        channels: List<String>,
        channelGroups: List<String>,
        withPresence: Boolean,
        withTimetoken: Long = 0L,
    ) {
        throwExceptionIfChannelAndChannelGroupIsMissing(channels, channelGroups)
        addChannelsToLocalStorage(channels)
        addChannelGroupsToLocalStorage(channelGroups)
        val channelsInLocalStorage = stateManager.channels.keys.toList()
        val channelGroupsInLocalStorage = stateManager.channelGroups.keys.toList()
        if (withTimetoken != 0L) {
            createAndPassForHandlingSubscriptionRestoredEvent(channelsInLocalStorage, channelGroupsInLocalStorage, withTimetoken)
        } else {
            createAndPassForHandlingSubscriptionChangedEvent(channelsInLocalStorage, channelGroupsInLocalStorage)
        }
    }

    fun unsubscribe(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList()
    ) {
        throwExceptionIfChannelAndChannelGroupIsMissing(channels, channelGroups)
        removeChannelsFromLocalStorage(channels)
        removeChannelGroupsFromLocalStorage(channelGroups)

        if (stateManager.channels.size > 0 || stateManager.channelGroups.size > 0) {
            val channelsInLocalStorage = stateManager.channels.keys.toList()
            val channelGroupsInLocalStorage = stateManager.channelGroups.keys.toList()
            eventEngineManager.addEventToQueue(SubscriptionChanged(channelsInLocalStorage, channelGroupsInLocalStorage))
        } else {
            eventEngineManager.addEventToQueue(Event.UnsubscribeAll)
        }
    }

    fun unsubscribeAll() {
        removeAllChannelsFromLocalStorage()
        removeAllChannelGroupsFromLocalStorage()
        eventEngineManager.addEventToQueue(Event.UnsubscribeAll)
    }

    fun getSubscribedChannels(): List<String> {
        return stateManager.channels.keys.toList()
    }

    fun getSubscribedChannelGroups(): List<String> {
        return stateManager.channelGroups.keys.toList()
    }

    fun disconnect() {
        TODO("Not yet implemented")
    }

    @Synchronized
    fun destroy() {
        // todo do we want to have "force" flag?
        disconnect()
        eventEngineManager.stop()
    }

    private fun throwExceptionIfChannelAndChannelGroupIsMissing(
        channels: List<String>,
        channelGroups: List<String>
    ) {
        if (channels.isEmpty() && channelGroups.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_OR_CHANNEL_GROUP_MISSING)
        }
    }

    private fun addChannelsToLocalStorage(channels: List<String>) {
        channels.forEach { channelName ->
            stateManager.channels.putIfAbsent(channelName, SubscriptionItem(channelName))
        }
    }

    private fun addChannelGroupsToLocalStorage(channelGroups: List<String>) {
        channelGroups.forEach { channelGroupName ->
            stateManager.channelGroups.putIfAbsent(channelGroupName, SubscriptionItem(channelGroupName))
        }
    }

    private fun createAndPassForHandlingSubscriptionRestoredEvent(
        channels: List<String>,
        channelGroups: List<String>,
        withTimetoken: Long
    ) {
        val subscriptionRestored = SubscriptionRestored(
            channels,
            channelGroups,
            SubscriptionCursor(withTimetoken, "42")
        ) // todo get region from somewhere
        eventEngineManager.addEventToQueue(subscriptionRestored)
    }

    private fun createAndPassForHandlingSubscriptionChangedEvent(
        channels: List<String>,
        channelGroups: List<String>
    ) {
        val subscriptionChanged = SubscriptionChanged(channels, channelGroups)
        eventEngineManager.addEventToQueue(subscriptionChanged)
    }

    private fun removeChannelGroupsFromLocalStorage(channelGroups: List<String>) {
        channelGroups.forEach { channelGroupName ->
            stateManager.channelGroups.remove(channelGroupName)
        }
    }

    private fun removeChannelsFromLocalStorage(channels: List<String>) {
        channels.forEach { channelName ->
            stateManager.channels.remove(channelName)
        }
    }

    private fun removeAllChannelsFromLocalStorage() {
        stateManager.channels = HashMap()
    }

    private fun removeAllChannelGroupsFromLocalStorage() {
        stateManager.channelGroups = HashMap()
    }
}
