@file:Suppress("UNUSED_PARAMETER")

package com.pubnub.api.subscribe

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.pubsub.Subscribe
import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.api.eventengine.EffectFactory
import com.pubnub.api.eventengine.EventEngineConf
import com.pubnub.api.eventengine.EventSink
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.managers.EventEngineManager
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.subscribe.eventengine.configuration.EventEngineConfImpl
import com.pubnub.api.subscribe.eventengine.effect.ExponentialPolicy
import com.pubnub.api.subscribe.eventengine.effect.LinearPolicy
import com.pubnub.api.subscribe.eventengine.effect.MessagesConsumer
import com.pubnub.api.subscribe.eventengine.effect.NoRetriesPolicy
import com.pubnub.api.subscribe.eventengine.effect.RetryPolicy
import com.pubnub.api.subscribe.eventengine.effect.StatusConsumer
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectFactory
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.effect.effectprovider.HandshakeProvider
import com.pubnub.api.subscribe.eventengine.effect.effectprovider.HandshakeProviderImpl
import com.pubnub.api.subscribe.eventengine.effect.effectprovider.ReceiveMessagesProvider
import com.pubnub.api.subscribe.eventengine.effect.effectprovider.ReceiveMessagesProviderImpl
import com.pubnub.api.subscribe.eventengine.event.Event.SubscriptionChanged
import com.pubnub.api.subscribe.eventengine.event.Event.SubscriptionRestored
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class Subscribe(
    private val pubnub: PubNub,
    private val listenerManager: ListenerManager,
    private val eventEngineConf: EventEngineConf = EventEngineConfImpl()
) {
    private val eventSink = eventEngineConf.getEventSink()
    private val eventEngineManager: EventEngineManager = createEventEngineManager()

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

    private fun createEventEngineManager(): EventEngineManager {
        val currenState: SubscribeState = SubscribeState.Unsubscribed
        val effectFactory = createSubscribeEffectFactory()
        val managedEffects: ConcurrentHashMap<String, ManagedEffect> = ConcurrentHashMap()

        return EventEngineManager(
            currenState = currenState,
            eventEngineConf = eventEngineConf,
            effectFactory = effectFactory,
            managedEffects = managedEffects,
        )
    }

    private fun createSubscribeEffectFactory(): EffectFactory<SubscribeEffectInvocation> {
        val subscribe = Subscribe(pubnub)
        val handshakeProvider: HandshakeProvider = HandshakeProviderImpl(subscribe)
        val receiveMessagesProvider: ReceiveMessagesProvider = ReceiveMessagesProviderImpl(subscribe, pubnub)
        val eventSink: EventSink = eventSink
        val policy: RetryPolicy = getRetryPolicy()
        val executorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
        val messagesConsumer: MessagesConsumer = listenerManager
        val statusConsumer: StatusConsumer = listenerManager

        return SubscribeEffectFactory(
            handshakeProvider,
            receiveMessagesProvider,
            eventSink,
            policy,
            executorService,
            messagesConsumer,
            statusConsumer
        )
    }

    private fun getRetryPolicy(): RetryPolicy {
        return when (pubnub.configuration.reconnectionPolicy) {
            PNReconnectionPolicy.NONE -> NoRetriesPolicy
            PNReconnectionPolicy.LINEAR -> LinearPolicy(
                maxRetries = pubnub.configuration.maximumReconnectionRetries,
                fixedDelay = Duration.ofSeconds(3)
            )
            PNReconnectionPolicy.EXPONENTIAL -> ExponentialPolicy(maxRetries = pubnub.configuration.maximumReconnectionRetries)
        }
    }
}
