package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.eventengine.EventHandler
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.eventengine.ManagedEffectFactory
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

data class ReceiveMessagesResult(
    val messages: List<PNEvent>,
    val subscriptionCursor: SubscriptionCursor
)

class SubscribeManagedEffectFactory(
    private val handshakeProvider: HandshakeProvider,
    private val receiveMessagesProvider: ReceiveMessagesProvider,
    private val eventHandler: EventHandler,
    private val policy: RetryPolicy,
    private val executorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor(),
) : ManagedEffectFactory<SubscribeEffectInvocation> {
    override fun create(effectInvocation: SubscribeEffectInvocation): ManagedEffect? {
        return when (effectInvocation) {
            is SubscribeEffectInvocation.EmitMessages -> null // todo
            is SubscribeEffectInvocation.EmitStatus -> null // todo
            is SubscribeEffectInvocation.Handshake -> {
                handleHandshake(effectInvocation.channels, effectInvocation.channelGroups)
            }

            is SubscribeEffectInvocation.HandshakeReconnect -> {
                scheduleManagedEffect(
                    attempt = effectInvocation.attempts,
                    reconnectFailureEvent = Event.HandshakeReconnectGiveUp(effectInvocation.reason!!), // todo figure this out here
                    managedEffect =
                    handleHandshake(effectInvocation.channels, effectInvocation.channelGroups)

                )
            }

            is SubscribeEffectInvocation.ReceiveMessages -> {
                handleReceiveMessages(
                    effectInvocation.channels,
                    effectInvocation.channelGroups,
                    effectInvocation.subscriptionCursor
                )
            }

            is SubscribeEffectInvocation.ReceiveReconnect -> scheduleManagedEffect(
                attempt = effectInvocation.attempts,
                reconnectFailureEvent = Event.ReceiveReconnectGiveUp(effectInvocation.reason!!), // todo figure this out here
                managedEffect = handleReceiveMessages(
                    effectInvocation.channels,
                    effectInvocation.channelGroups,
                    effectInvocation.subscriptionCursor
                )

            )

            SubscribeEffectInvocation.CancelHandshake,
            SubscribeEffectInvocation.CancelHandshakeReconnect,
            SubscribeEffectInvocation.CancelReceiveMessages,
            SubscribeEffectInvocation.CancelReceiveReconnect,
            -> null
        }
    }

    private fun scheduleManagedEffect(
        attempt: Int,
        reconnectFailureEvent: Event,
        managedEffect: ManagedEffect
    ): ManagedEffect? {
        val delay = policy.nextDelay(attempt)

        if (delay == null) {
            eventHandler.handleEvent(event = reconnectFailureEvent)
            return null
        }

        return DelayedManagedEffect(managedEffect, executorService, delay)
    }

    private fun handleReceiveMessages(
        channels: List<String>,
        channelGroups: List<String>,
        subscriptionCursor: SubscriptionCursor
    ) =
        receiveMessagesProvider.receiveMessages(
            channels, channelGroups, subscriptionCursor
        ).toManagedEffect { result, status ->
            if (status.error) {
                eventHandler.handleEvent(
                    Event.ReceiveFailure(
                        status.exception
                            ?: PubNubException("Unknown error") // todo check it that can happen
                    )
                )
            } else {
                eventHandler.handleEvent(Event.ReceiveSuccess(result!!.messages, result.subscriptionCursor))
            }
        }

    private fun handleHandshake(channels: List<String>, channelGroups: List<String>) =
        handshakeProvider.handshake(channels, channelGroups)
            .toManagedEffect { result, status ->
                if (status.error) {
                    eventHandler.handleEvent(
                        Event.HandshakeFailure(
                            status.exception
                                ?: PubNubException("Unknown error") // todo check it that can happen
                        )
                    )
                } else {
                    eventHandler.handleEvent(Event.HandshakeSuccess(result!!))
                }
            }
}
