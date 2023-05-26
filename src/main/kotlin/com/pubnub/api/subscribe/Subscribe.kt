@file:Suppress("UNUSED_PARAMETER")

package com.pubnub.api.subscribe

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.managers.DuplicationManager
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.subscribe.eventengine.Bus
import com.pubnub.api.subscribe.eventengine.EffectBus
import com.pubnub.api.subscribe.eventengine.EventBus
import com.pubnub.api.subscribe.eventengine.EventEngine
import com.pubnub.api.subscribe.eventengine.QueueEffectBus
import com.pubnub.api.subscribe.eventengine.QueueEventBus
import com.pubnub.api.subscribe.eventengine.effect.ExponentialPolicy
import com.pubnub.api.subscribe.eventengine.effect.HandshakeProvider
import com.pubnub.api.subscribe.eventengine.effect.LinearPolicy
import com.pubnub.api.subscribe.eventengine.effect.MessagesConsumer
import com.pubnub.api.subscribe.eventengine.effect.NoRetriesPolicy
import com.pubnub.api.subscribe.eventengine.effect.ReceiveMessagesProvider
import com.pubnub.api.subscribe.eventengine.effect.ReceiveMessagesResult
import com.pubnub.api.subscribe.eventengine.effect.RetryPolicy
import com.pubnub.api.subscribe.eventengine.effect.StatusConsumer
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectFactory
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import com.pubnub.api.workers.SubscribeMessageProcessor
import java.time.Duration
import java.util.concurrent.BlockingQueue
import com.pubnub.api.endpoints.pubsub.Subscribe as SubscribeCall

fun PubNub.createSubscribeModule(
    listenerManager: ListenerManager = ListenerManager(this), eventBus: Bus<Event> = QueueEventBus(),
    effectBus: EffectBus<SubscribeEffectInvocation> = object : QueueEffectBus {
        override val queue: BlockingQueue<SubscribeEffectInvocation> =
    },
): Subscribe {
    val messageProcessor = SubscribeMessageProcessor(this, DuplicationManager(this.configuration))

    return Subscribe(eventBus = eventBus,
        moduleRequirements = object : ModuleRequirements, MessagesConsumer by listenerManager,
            StatusConsumer by listenerManager {
            override fun getHandshakeRemoteAction(
                channels: List<String>, channelGroups: List<String>
            ): RemoteAction<SubscriptionCursor> = SubscribeCall(this@createSubscribeModule).also {
                it.channels = channels
                it.channelGroups = channelGroups
            }.map {
                SubscriptionCursor(
                    timetoken = it.metadata.timetoken, region = it.metadata.region
                )
            }

            override fun getReceiveMessagesRemoteAction(
                channels: List<String>, channelGroups: List<String>, subscriptionCursor: SubscriptionCursor
            ): RemoteAction<ReceiveMessagesResult> = SubscribeCall(this@createSubscribeModule).also {
                it.channels = channels
                it.channelGroups = channelGroups
                it.timetoken = subscriptionCursor.timetoken
                it.region = subscriptionCursor.region
            }.map {
                ReceiveMessagesResult(
                    messages = it.messages.mapNotNull { messageProcessor.processIncomingPayload(it) },
                    subscriptionCursor = SubscriptionCursor(
                        timetoken = it.metadata.timetoken, region = it.metadata.region
                    )
                )
            }

            override val retryPolicy: RetryPolicy
                get() = when (this@createSubscribeModule.configuration.reconnectionPolicy) {
                    PNReconnectionPolicy.NONE -> NoRetriesPolicy
                    PNReconnectionPolicy.LINEAR -> LinearPolicy(
                        maxRetries = this@createSubscribeModule.configuration.maximumReconnectionRetries,
                        fixedDelay = Duration.ofSeconds(3)
                    )

                    PNReconnectionPolicy.EXPONENTIAL -> ExponentialPolicy(maxRetries = this@createSubscribeModule.configuration.maximumReconnectionRetries)
                }
        })
}

fun <T, U> RemoteAction<T>.map(mappingBlock: (T) -> U): RemoteAction<U> = object : RemoteAction<U> {
    override fun sync(): U? = this@map.sync()?.let(mappingBlock)

    override fun silentCancel() {
        this@map.silentCancel()
    }

    override fun async(callback: (result: U?, status: PNStatus) -> Unit) {
        this@map.async { result, status -> callback(result?.let(mappingBlock), status) }
    }
}

interface ModuleRequirements : HandshakeProvider, ReceiveMessagesProvider, ReconnectionPolicyProvider, MessagesConsumer,
    StatusConsumer

interface ReconnectionPolicyProvider {
    val retryPolicy: RetryPolicy
}

class Subscribe(private val moduleRequirements: ModuleRequirements, private val eventBus: EventBus,
    private val effectBus: EffectBus<SubscribeEffectInvocation>
) {

    private val eventEngine = createEventEngine()

    private fun createEventEngine(): EventEngine {

        val managedEffectFactory = SubscribeEffectFactory(
            handshakeProvider = moduleRequirements,
            receiveMessagesProvider = moduleRequirements,
            eventSink = eventBus,
            policy = moduleRequirements.retryPolicy,
            messagesConsumer = moduleRequirements,
            statusConsumer = moduleRequirements,
        )

        val effectDispatcher = EffectDispatcher(managedEffectFactory).also { it.start() }
        return EventEngine(
            SubscribeState.Unsubscribed, eventSource = eventBus, effectDispatcher = effectDispatcher
        ).also { it.start() }
    }

    fun subscribe(
        channels: List<String>, channelGroups: List<String>, withPresence: Boolean, withTimetoken: Long
    ) {
        eventBus.add(Event.SubscriptionChanged(channels, channelGroups))
    }

    fun unsubscribe(
        channels: List<String> = emptyList(), channelGroups: List<String> = emptyList()
    ) {
        eventBus.add(Event.SubscriptionChanged(channels, channelGroups))
    }

    fun unsubscribeAll() {
        eventBus.add(Event.SubscriptionChanged(emptyList(), emptyList()))
    }

    fun getSubscribedChannels(): List<String> {
        TODO("Not yet implemented")
    }

    fun getSubscribedChannelGroups(): List<String> {
        TODO("Not yet implemented")
    }
}
