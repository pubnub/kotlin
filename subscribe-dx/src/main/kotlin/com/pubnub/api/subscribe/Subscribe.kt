package com.pubnub.api.subscribe

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.eventengine.EventEngineConf
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.managers.EventEngineManager
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.subscribe.eventengine.SubscribeEventEngine
import com.pubnub.api.subscribe.eventengine.data.SubscriptionData
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
import com.pubnub.core.PubNubException
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

private const val PRESENCE_CHANNEL_SUFFIX = "-pnpres"

class Subscribe(
    private val eventEngineManager: EventEngineManager,
    private val subscriptionData: SubscriptionData = SubscriptionData()
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

    @Synchronized
    fun subscribe(
        channels: List<String>,
        channelGroups: List<String>,
        withPresence: Boolean,
        withTimetoken: Long = 0L,
    ) {
        throwExceptionIfChannelAndChannelGroupIsMissing(channels, channelGroups)
        addChannelsToSubscriptionData(channels, withPresence)
        addChannelGroupsToSubscriptionData(channelGroups, withPresence)
        val channelsInLocalStorage = subscriptionData.channels.toList()
        val channelGroupsInLocalStorage = subscriptionData.channelGroups.toList()
        if (withTimetoken != 0L) {
            createAndPassForHandlingSubscriptionRestoredEvent(
                channelsInLocalStorage,
                channelGroupsInLocalStorage,
                withTimetoken
            )
        } else {
            createAndPassForHandlingSubscriptionChangedEvent(channelsInLocalStorage, channelGroupsInLocalStorage)
        }
    }

    @Synchronized
    fun unsubscribe(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList()
    ) {
        throwExceptionIfChannelAndChannelGroupIsMissing(channels, channelGroups)
        removeChannelsFromSubscriptionData(channels)
        removeChannelGroupsFromSubscriptionData(channelGroups)

        if (subscriptionData.channels.size > 0 || subscriptionData.channelGroups.size > 0) {
            val channelsInLocalStorage = subscriptionData.channels.toList()
            val channelGroupsInLocalStorage = subscriptionData.channelGroups.toList()
            eventEngineManager.addEventToQueue(SubscriptionChanged(channelsInLocalStorage, channelGroupsInLocalStorage))
        } else {
            eventEngineManager.addEventToQueue(Event.UnsubscribeAll)
        }
    }

    @Synchronized
    fun unsubscribeAll() {
        removeAllChannelsFromLocalStorage()
        removeAllChannelGroupsFromLocalStorage()
        eventEngineManager.addEventToQueue(Event.UnsubscribeAll)
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
        eventEngineManager.addEventToQueue(Event.Disconnect)
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

    private fun addChannelsToSubscriptionData(channels: List<String>, withPresence: Boolean) {
        subscriptionData.channels.addAll(channels)
        if (withPresence) {
            channels.forEach {
                val presenceChannel = "$it$PRESENCE_CHANNEL_SUFFIX"
                subscriptionData.channels.add(presenceChannel)
            }
        }
    }

    private fun addChannelGroupsToSubscriptionData(channelGroups: List<String>, withPresence: Boolean) {
        subscriptionData.channelGroups.addAll(channelGroups)
        if (withPresence) {
            channelGroups.forEach {
                val presenceChannelGroup = "$it$PRESENCE_CHANNEL_SUFFIX"
                subscriptionData.channelGroups.add(presenceChannelGroup)
            }
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

    private fun removeChannelGroupsFromSubscriptionData(channelGroups: List<String>) {
        channelGroups.forEach {
            subscriptionData.channelGroups.remove(it)
            val presenceChannelGroup = "$it$PRESENCE_CHANNEL_SUFFIX"
            subscriptionData.channels.remove(presenceChannelGroup)
        }
    }

    private fun removeChannelsFromSubscriptionData(channels: List<String>) {
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
