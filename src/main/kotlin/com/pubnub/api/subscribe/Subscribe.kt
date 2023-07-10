@file:Suppress("UNUSED_PARAMETER")

package com.pubnub.api.subscribe

import com.pubnub.api.PubNub
import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.eventengine.EventEngineConf
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.managers.EventEngineManager
import com.pubnub.api.managers.ListenerManager
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
    private val eventEngineManager: EventEngineManager
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
        if (withTimetoken != 0L) {
            val subscriptionRestored = SubscriptionRestored(
                channels,
                channelGroups,
                SubscriptionCursor(withTimetoken, "42")
            ) // todo get region from somewhere
            eventEngineManager.addEventToQueue(subscriptionRestored)
        } else {
            val subscriptionChanged = SubscriptionChanged(channels, channelGroups)
            eventEngineManager.addEventToQueue(subscriptionChanged)
        }
    }

    fun unsubscribe(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList()
    ) {
        TODO("Not yet implemented")
    }

    fun unsubscribeAll() {
        TODO("Not yet implemented")
    }

    fun getSubscribedChannels(): List<String> {
        TODO("Not yet implemented")
    }

    fun getSubscribedChannelGroups(): List<String> {
        TODO("Not yet implemented")
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
}
