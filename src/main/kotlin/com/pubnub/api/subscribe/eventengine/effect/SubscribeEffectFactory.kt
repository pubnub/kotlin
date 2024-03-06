package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.Effect
import com.pubnub.api.eventengine.EffectFactory
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.server.MissedMessages
import com.pubnub.api.presence.eventengine.data.PresenceData
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.subscribe.eventengine.effect.effectprovider.HandshakeProvider
import com.pubnub.api.subscribe.eventengine.effect.effectprovider.ReceiveMessagesProvider
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import java.util.concurrent.ScheduledExecutorService

internal data class ReceiveMessagesResult(
    val messages: List<PNEvent>,
    val subscriptionCursor: SubscriptionCursor,
    // todo I don't know how the server behaves when there is no MissedMessages "rg" is empty or not exist.
    // if empty then it is ok ot have "List<MissedMessages>" if not exist then "List<MissedMessages>?"
    val missedMessages: List<MissedMessages>? // todo zamienić na MissedEvents bo to raczej mogą być messages, signals, objects, presenceEvents ?
)

internal class SubscribeEffectFactory(
    private val handshakeProvider: HandshakeProvider,
    private val receiveMessagesProvider: ReceiveMessagesProvider,
    private val subscribeEventSink: Sink<SubscribeEvent>,
    private val retryConfiguration: RetryConfiguration,
    private val executorService: ScheduledExecutorService,
    private val messagesConsumer: MessagesConsumer,
    private val statusConsumer: StatusConsumer,
    private val presenceData: PresenceData,
    private val sendStateWithSubscribe: Boolean,
) : EffectFactory<SubscribeEffectInvocation> {
    override fun create(effectInvocation: SubscribeEffectInvocation): Effect? {
        return when (effectInvocation) {
            is SubscribeEffectInvocation.EmitMessages -> {
                EmitMessagesEffect(messagesConsumer, effectInvocation.messages)
            }
            is SubscribeEffectInvocation.EmitStatus -> {
                EmitStatusEffect(statusConsumer, effectInvocation.status)
            }
            is SubscribeEffectInvocation.Handshake -> {
                val handshakeRemoteAction =
                    handshakeProvider.getHandshakeRemoteAction(
                        effectInvocation.channels,
                        effectInvocation.channelGroups,
                        if (sendStateWithSubscribe) {
                            presenceData.channelStates.filter { it.key in effectInvocation.channels }
                        } else {
                            null
                        }
                    )
                HandshakeEffect(handshakeRemoteAction, subscribeEventSink)
            }
            is SubscribeEffectInvocation.HandshakeReconnect -> {
                val handshakeRemoteAction =
                    handshakeProvider.getHandshakeRemoteAction(
                        effectInvocation.channels,
                        effectInvocation.channelGroups,
                        if (sendStateWithSubscribe) {
                            presenceData.channelStates.filter { it.key in effectInvocation.channels }
                        } else {
                            null
                        }
                    )
                HandshakeReconnectEffect(
                    handshakeRemoteAction,
                    subscribeEventSink,
                    retryConfiguration,
                    executorService,
                    effectInvocation.attempts,
                    effectInvocation.reason
                )
            }
            is SubscribeEffectInvocation.ReceiveMessages -> {
                val receiveMessagesRemoteAction: RemoteAction<ReceiveMessagesResult> =
                    receiveMessagesProvider.getReceiveMessagesRemoteAction(
                        effectInvocation.channels,
                        effectInvocation.channelGroups,
                        effectInvocation.subscriptionCursor
                    )
                ReceiveMessagesEffect(receiveMessagesRemoteAction, subscribeEventSink)
            }
            is SubscribeEffectInvocation.ReceiveReconnect -> {
                val receiveMessagesRemoteAction = receiveMessagesProvider.getReceiveMessagesRemoteAction(
                    effectInvocation.channels,
                    effectInvocation.channelGroups,
                    effectInvocation.subscriptionCursor
                )

                ReceiveReconnectEffect(
                    receiveMessagesRemoteAction,
                    subscribeEventSink,
                    retryConfiguration,
                    executorService,
                    effectInvocation.attempts,
                    effectInvocation.reason
                )
            }

            SubscribeEffectInvocation.CancelHandshake,
            SubscribeEffectInvocation.CancelHandshakeReconnect,
            SubscribeEffectInvocation.CancelReceiveMessages,
            SubscribeEffectInvocation.CancelReceiveReconnect,
            -> null
        }
    }
}
