package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.eventengine.EventDeliver
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
    private val eventDeliver: EventDeliver, // todo move it to ManagedEffect implementation?
    private val policy: RetryPolicy,
    private val executorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor(),
) : ManagedEffectFactory<SubscribeEffectInvocation> {
    override fun create(effectInvocation: SubscribeEffectInvocation): ManagedEffect? {
        return when (effectInvocation) {
            is SubscribeEffectInvocation.EmitMessages -> null // todo
            is SubscribeEffectInvocation.EmitStatus -> null // todo
            is SubscribeEffectInvocation.Handshake -> {
                getHandshakeEffect(effectInvocation.channels, effectInvocation.channelGroups)
            }

            is SubscribeEffectInvocation.HandshakeReconnect -> {
                scheduleManagedEffect(
                    attempt = effectInvocation.attempts,
                    reconnectFailureEvent = Event.HandshakeReconnectGiveUp(effectInvocation.reason!!), // todo figure this out here
                    managedEffect = getHandshakeReconnectEffect(
                        effectInvocation.channels,
                        effectInvocation.channelGroups
                    )
                )
            }

            is SubscribeEffectInvocation.ReceiveMessages -> {
                getReceiveMessagesEffect(
                    effectInvocation.channels,
                    effectInvocation.channelGroups,
                    effectInvocation.subscriptionCursor
                )
            }

            is SubscribeEffectInvocation.ReceiveReconnect -> scheduleManagedEffect(
                attempt = effectInvocation.attempts,
                reconnectFailureEvent = Event.ReceiveReconnectGiveUp(effectInvocation.reason!!), // todo figure this out here
                managedEffect = getReceiveReconnectEffect(
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
            eventDeliver.passEventForHandling(event = reconnectFailureEvent)
            return null
        }

        return DelayedManagedEffect(managedEffect, executorService, delay)
    }

    private fun getReceiveMessagesEffect(
        channels: List<String>,
        channelGroups: List<String>,
        subscriptionCursor: SubscriptionCursor
    ): ManagedEffect = receiveMessagesProvider.receiveMessages(channels, channelGroups, subscriptionCursor)
        .toManagedEffect { result, status ->
            if (status.error) {
                eventDeliver.passEventForHandling(
                    Event.ReceiveFailure(
                        status.exception
                            ?: PubNubException("Unknown error") // todo check it that can happen
                    )
                )
            } else {
                eventDeliver.passEventForHandling(Event.ReceiveSuccess(result!!.messages, result.subscriptionCursor))
            }
        }

    private fun getReceiveReconnectEffect(
        channels: List<String>,
        channelGroups: List<String>,
        subscriptionCursor: SubscriptionCursor
    ): ManagedEffect = receiveMessagesProvider.receiveMessages(channels, channelGroups, subscriptionCursor)
        .toManagedEffect { result, status ->
            if (status.error) {
                eventDeliver.passEventForHandling(
                    Event.ReceiveReconnectFailure(
                        status.exception ?: PubNubException("dfa")
                    )
                )
            } else {
                eventDeliver.passEventForHandling(
                    Event.ReceiveReconnectSuccess(
                        result!!.messages,
                        result.subscriptionCursor
                    )
                )
            }
        }

    private fun getHandshakeEffect(channels: List<String>, channelGroups: List<String>): ManagedEffect = handshakeProvider.handshake(channels, channelGroups)
        .toManagedEffect { result, status ->
            if (status.error) {
                eventDeliver.passEventForHandling(
                    Event.HandshakeFailure(
                        status.exception
                            ?: PubNubException("Unknown error") // todo check it that can happen
                    )
                )
            } else {
                eventDeliver.passEventForHandling(Event.HandshakeSuccess(result!!))
            }
        }

    private fun getHandshakeReconnectEffect(channels: List<String>, channelGroups: List<String>): ManagedEffect = handshakeProvider.handshake(channels, channelGroups)
        .toManagedEffect { result, status ->
            if (status.error) {
                eventDeliver.passEventForHandling(
                    Event.HandshakeReconnectFailure(
                        status.exception ?: PubNubException("Unknown error")
                    )
                )
            } else {
                eventDeliver.passEventForHandling(Event.HandshakeReconnectSuccess(channels, channelGroups, result!!))
            }
        }
}
