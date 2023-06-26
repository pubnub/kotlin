@file:Suppress("UNUSED_PARAMETER")

package com.pubnub.api.subscribe

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.pubsub.Subscribe
import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.eventengine.EventEngineConf
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.managers.EventEngineManager
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.subscribe.eventengine.SubscribeEventEngine
import com.pubnub.api.subscribe.eventengine.effect.ExponentialPolicy
import com.pubnub.api.subscribe.eventengine.effect.LinearPolicy
import com.pubnub.api.subscribe.eventengine.effect.MessagesConsumer
import com.pubnub.api.subscribe.eventengine.effect.NoRetriesPolicy
import com.pubnub.api.subscribe.eventengine.effect.RetryPolicy
import com.pubnub.api.subscribe.eventengine.effect.StatusConsumer
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectFactory
import com.pubnub.api.subscribe.eventengine.effect.effectprovider.HandshakeProvider
import com.pubnub.api.subscribe.eventengine.effect.effectprovider.HandshakeProviderImpl
import com.pubnub.api.subscribe.eventengine.effect.effectprovider.ReceiveMessagesProvider
import com.pubnub.api.subscribe.eventengine.effect.effectprovider.ReceiveMessagesProviderImpl
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.Event.SubscriptionChanged
import com.pubnub.api.subscribe.eventengine.event.Event.SubscriptionRestored
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import java.time.Duration
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class Subscribe(
    private val eventEngineManager: EventEngineManager
) {

    companion object {
        fun create(
            pubNub: PubNub,
            listenerManager: ListenerManager,
            eventEngineConf: EventEngineConf
        ): com.pubnub.api.subscribe.Subscribe {
            val subscribe = Subscribe(pubNub)
            val handshakeProvider: HandshakeProvider = HandshakeProviderImpl(subscribe)
            val receiveMessagesProvider: ReceiveMessagesProvider = ReceiveMessagesProviderImpl(subscribe, pubNub)
            val eventSink: Sink<Event> = eventEngineConf.getEventSink()
            val policy: RetryPolicy = getRetryPolicy(pubNub)
            val executorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
            val messagesConsumer: MessagesConsumer = listenerManager
            val statusConsumer: StatusConsumer = listenerManager

            val subscribeEffectFactory = SubscribeEffectFactory(
                handshakeProvider,
                receiveMessagesProvider,
                eventSink,
                policy,
                executorService,
                messagesConsumer,
                statusConsumer
            )

            val subscribeEventEngine = SubscribeEventEngine(
                effectSink = eventEngineConf.getEffectSink(),
                eventSource = eventEngineConf.getEventSource()
            )
            val effectDispatcher = EffectDispatcher(
                effectFactory = subscribeEffectFactory,
                effectSource = eventEngineConf.getEffectSource()
            )

            val eventEngineManager = EventEngineManager(
                subscribeEventEngine = subscribeEventEngine,
                effectDispatcher = effectDispatcher,
                eventSink = eventEngineConf.getEventSink()
            ).apply {
                if (pubNub.configuration.enableSubscribeBeta) {
                    start()
                }
            }

            return Subscribe(eventEngineManager)
        }

        private fun getRetryPolicy(pubNub: PubNub): RetryPolicy {
            return when (pubNub.configuration.reconnectionPolicy) {
                PNReconnectionPolicy.NONE -> NoRetriesPolicy
                PNReconnectionPolicy.LINEAR -> LinearPolicy(
                    maxRetries = pubNub.configuration.maximumReconnectionRetries,
                    fixedDelay = Duration.ofSeconds(3)
                )
                PNReconnectionPolicy.EXPONENTIAL -> ExponentialPolicy(maxRetries = pubNub.configuration.maximumReconnectionRetries)
            }
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
            ) // todo skąd wziąć region?
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
