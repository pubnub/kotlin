package com.pubnub.api.subscribe.eventengine.state

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.eventengine.State
import com.pubnub.api.eventengine.noTransition
import com.pubnub.api.eventengine.transitionTo
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

sealed class SubscribeState : State<SubscribeEffectInvocation, SubscribeEvent, SubscribeState> {
    object Unsubscribed : SubscribeState() {
        override fun transition(event: SubscribeEvent): Pair<SubscribeState, Set<SubscribeEffectInvocation>> {
            return when (event) {
                is SubscribeEvent.SubscriptionChanged -> {
                    transitionTo(Handshaking(event.channels, event.channelGroups))
                }
                is SubscribeEvent.SubscriptionRestored -> {
                    transitionTo(Receiving(event.channels, event.channelGroups, event.subscriptionCursor))
                }
                else -> {
                    noTransition()
                }
            }
        }
    }

    data class Handshaking(
        val channels: Set<String>,
        val channelGroups: Set<String>
    ) : SubscribeState() {
        override fun onEntry() = setOf(SubscribeEffectInvocation.Handshake(channels, channelGroups))
        override fun onExit() = setOf(SubscribeEffectInvocation.CancelHandshake)

        override fun transition(event: SubscribeEvent): Pair<SubscribeState, Set<SubscribeEffectInvocation>> {
            return when (event) {
                is SubscribeEvent.HandshakeSuccess -> {
                    transitionTo(
                        state = Receiving(channels, channelGroups, event.subscriptionCursor),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                category = PNStatusCategory.PNConnectedCategory,
                                operation = PNOperationType.PNSubscribeOperation, // todo is PNSubscribeOperation correct operation
                                error = false,
                                affectedChannels = channels.toList(),
                                affectedChannelGroups = channelGroups.toList()
                            )
                        )
                    )
                }

                is SubscribeEvent.SubscriptionRestored -> {
                    transitionTo(Receiving(event.channels, event.channelGroups, event.subscriptionCursor))  // todo check with doc
                }
                is SubscribeEvent.HandshakeFailure -> {
                    transitionTo(HandshakeReconnecting(channels, channelGroups, 0, event.reason))
                }
                is SubscribeEvent.SubscriptionChanged -> {
                    transitionTo(Handshaking(event.channels, event.channelGroups))
                }
                is SubscribeEvent.Disconnect -> {
                    transitionTo(HandshakeStopped(channels, channelGroups, reason = null))
                }
                is SubscribeEvent.UnsubscribeAll -> {
                    transitionTo(Unsubscribed)
                }
                else -> {
                    noTransition()
                }
            }
        }
    }

    data class HandshakeReconnecting(
        val channels: Set<String>,
        val channelGroups: Set<String>,
        val attempts: Int,
        val reason: PubNubException?
    ) : SubscribeState() {
        override fun onEntry() =
            setOf(SubscribeEffectInvocation.HandshakeReconnect(channels, channelGroups, attempts, reason))

        override fun onExit() = setOf(SubscribeEffectInvocation.CancelHandshakeReconnect)

        override fun transition(event: SubscribeEvent): Pair<SubscribeState, Set<SubscribeEffectInvocation>> {
            return when (event) {
                is SubscribeEvent.HandshakeReconnectFailure -> {
                    transitionTo(
                        HandshakeReconnecting(
                            channels, channelGroups, attempts + 1, event.reason
                        )
                    )
                }
                is SubscribeEvent.SubscriptionChanged -> {
                    transitionTo(Handshaking(event.channels, event.channelGroups))
                }
                is SubscribeEvent.Disconnect -> {
                    transitionTo(HandshakeStopped(channels, channelGroups, reason))
                }
                is SubscribeEvent.HandshakeReconnectGiveup -> {
                    transitionTo(
                        HandshakeFailed(channels, channelGroups, event.reason),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                category = PNStatusCategory.PNConnectionError,
                                operation = PNOperationType.PNSubscribeOperation,
                                error = true,
                                affectedChannels = channels.toList(),
                                affectedChannelGroups = channelGroups.toList(),
                                exception = reason
                            )
                        )
                    )
                }
                is SubscribeEvent.HandshakeReconnectSuccess -> {
                    transitionTo(
                        state = Receiving(channels, channelGroups, event.subscriptionCursor),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                category = PNStatusCategory.PNConnectedCategory,
                                operation = PNOperationType.PNSubscribeOperation,
                                error = false,
                                affectedChannels = channels.toList(),
                                affectedChannelGroups = channelGroups.toList()
                            )
                        )
                    )
                }

                is SubscribeEvent.SubscriptionRestored -> {
                    transitionTo(Receiving(event.channels, event.channelGroups, event.subscriptionCursor))
                }
                is SubscribeEvent.UnsubscribeAll -> {
                    transitionTo(Unsubscribed)
                }
                else -> {
                    noTransition()
                }
            }
        }
    }

    data class HandshakeStopped(
        val channels: Set<String>,
        val channelGroups: Set<String>,
        val reason: PubNubException?
    ) : SubscribeState() {

        override fun transition(event: SubscribeEvent): Pair<SubscribeState, Set<SubscribeEffectInvocation>> {
            return when (event) {
                is SubscribeEvent.Reconnect -> {
                    transitionTo(Handshaking(channels, channelGroups))
                }
                is SubscribeEvent.SubscriptionChanged -> {
                    transitionTo(Handshaking(event.channels, event.channelGroups)) // todo transition to HandshakeStopped + update channels/channelGroups list
                }
                is SubscribeEvent.SubscriptionRestored -> {
                    transitionTo(Receiving(event.channels, event.channelGroups, event.subscriptionCursor)) // todo check with doc. transitionTo Handshaking or self?
                }
                is SubscribeEvent.UnsubscribeAll -> {
                    transitionTo(Unsubscribed)
                }
                else -> {
                    noTransition()
                }
            }
        }
    }

    data class HandshakeFailed(
        val channels: Set<String>,
        val channelGroups: Set<String>,
        val reason: PubNubException
    ) : SubscribeState() {

        override fun transition(event: SubscribeEvent): Pair<SubscribeState, Set<SubscribeEffectInvocation>> {
            return when (event) {
                is SubscribeEvent.HandshakeReconnectRetry -> {
                    transitionTo(HandshakeReconnecting(channels, channelGroups, 0, reason))
                }
                is SubscribeEvent.SubscriptionChanged -> {
                    transitionTo(Handshaking(event.channels, event.channelGroups))
                }
                is SubscribeEvent.SubscriptionRestored -> {
                    transitionTo(
                        Receiving(
                            event.channels, event.channelGroups, event.subscriptionCursor
                        )
                    )
                }
                is SubscribeEvent.Reconnect -> {
                    transitionTo(Handshaking(channels, channelGroups))
                }
                is SubscribeEvent.UnsubscribeAll -> {
                    transitionTo(Unsubscribed)
                }
                else -> {
                    noTransition()
                }
            }
        }
    }

    data class Receiving(
        private val channels: Set<String>,
        private val channelGroups: Set<String>,
        private val subscriptionCursor: SubscriptionCursor
    ) : SubscribeState() {
        override fun onEntry() = setOf(
            SubscribeEffectInvocation.ReceiveMessages(
                channels, channelGroups, subscriptionCursor
            )
        )

        override fun onExit() = setOf(SubscribeEffectInvocation.CancelReceiveMessages)

        override fun transition(event: SubscribeEvent): Pair<SubscribeState, Set<SubscribeEffectInvocation>> {

            return when (event) {
                is SubscribeEvent.ReceiveFailure -> {
                    transitionTo(
                        ReceiveReconnecting(
                            channels, channelGroups, subscriptionCursor, 0, event.reason
                        )
                    )
                }
                is SubscribeEvent.Disconnect -> {
                    transitionTo(
                        state = ReceiveStopped(channels, channelGroups, subscriptionCursor),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                category = PNStatusCategory.PNDisconnectedCategory,
                                operation = PNOperationType.PNSubscribeOperation,
                                error = false, // todo is PNDisconnectedCategory error
                                affectedChannels = channels.toList(),
                                affectedChannelGroups = channelGroups.toList()
                            )
                        )
                    )
                }
                is SubscribeEvent.SubscriptionChanged -> {
                    transitionTo(Receiving(event.channels, event.channelGroups, subscriptionCursor))
                }
                is SubscribeEvent.SubscriptionRestored -> {
                    transitionTo(Receiving(event.channels, event.channelGroups, event.subscriptionCursor))
                }
                is SubscribeEvent.ReceiveSuccess -> {
                    transitionTo(
                        state = Receiving(channels, channelGroups, event.subscriptionCursor),
                        SubscribeEffectInvocation.EmitMessages(event.messages),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                category = PNStatusCategory.PNConnectedCategory,
                                operation = PNOperationType.PNSubscribeOperation, // todo is PNSubscribeOperation correct operation
                                error = false,
                                affectedChannels = channels.toList(),
                                affectedChannelGroups = channelGroups.toList()
                            )
                        )
                    )
                }
                is SubscribeEvent.UnsubscribeAll -> {
                    transitionTo(Unsubscribed)
                }

                else -> {
                    noTransition()
                }
            }
        }
    }

    data class ReceiveReconnecting(
        val channels: Set<String>,
        val channelGroups: Set<String>,
        val subscriptionCursor: SubscriptionCursor,
        val attempts: Int,
        val reason: PubNubException?
    ) : SubscribeState() {
        override fun onEntry() =
            setOf(
                SubscribeEffectInvocation.ReceiveReconnect(
                    channels,
                    channelGroups,
                    subscriptionCursor,
                    attempts,
                    reason
                )
            )

        override fun onExit() = setOf(SubscribeEffectInvocation.CancelReceiveReconnect)

        override fun transition(event: SubscribeEvent): Pair<SubscribeState, Set<SubscribeEffectInvocation>> {
            return when (event) {
                is SubscribeEvent.ReceiveReconnectFailure -> {
                    transitionTo(
                        ReceiveReconnecting(
                            channels, channelGroups, subscriptionCursor, attempts + 1, event.reason
                        )
                    )
                }
                is SubscribeEvent.SubscriptionChanged -> {
                    transitionTo(
                        Receiving(event.channels, event.channelGroups, subscriptionCursor)
                    )
                }
                is SubscribeEvent.Disconnect -> {
                    transitionTo(
                        ReceiveStopped(channels, channelGroups, subscriptionCursor),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                category = PNStatusCategory.PNDisconnectedCategory,
                                operation = PNOperationType.PNSubscribeOperation,
                                error = false, // todo is PNDisconnectedCategory error
                                affectedChannels = channels.toList(),
                                affectedChannelGroups = channelGroups.toList()
                            )
                        )
                    )
                }
                is SubscribeEvent.ReceiveReconnectGiveup -> {
                    transitionTo(
                        state = ReceiveFailed(channels, channelGroups, subscriptionCursor, event.reason),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                category = PNStatusCategory.PNUnexpectedDisconnectCategory,
                                operation = PNOperationType.PNSubscribeOperation,
                                error = false, // todo is PNDisconnectedCategory error
                                affectedChannels = channels.toList(),
                                affectedChannelGroups = channelGroups.toList()
                            )
                        )
                    )
                }
                is SubscribeEvent.ReceiveReconnectSuccess -> {
                    transitionTo(
                        state = Receiving(channels, channelGroups, event.subscriptionCursor),
                        SubscribeEffectInvocation.EmitMessages(event.messages),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                category = PNStatusCategory.PNConnectedCategory,
                                operation = PNOperationType.PNSubscribeOperation, // todo is PNSubscribeOperation correct operation
                                error = false,
                                affectedChannels = channels.toList(),
                                affectedChannelGroups = channelGroups.toList()
                            )
                        )
                    )
                }
                is SubscribeEvent.SubscriptionRestored -> {
                    transitionTo(Receiving(event.channels, event.channelGroups, subscriptionCursor))
                }
                is SubscribeEvent.UnsubscribeAll -> {
                    transitionTo(Unsubscribed)
                }
                else -> {
                    noTransition()
                }
            }
        }
    }

    data class ReceiveStopped(
        private val channels: Set<String>,
        val channelGroups: Set<String>,
        val subscriptionCursor: SubscriptionCursor
    ) : SubscribeState() {
        override fun transition(event: SubscribeEvent): Pair<SubscribeState, Set<SubscribeEffectInvocation>> {
            return when (event) {
                is SubscribeEvent.Reconnect -> {
                    transitionTo(Receiving(channels, channelGroups, subscriptionCursor))
                }
                is SubscribeEvent.SubscriptionChanged -> {
                    transitionTo(Receiving(event.channels, event.channelGroups, subscriptionCursor)) // todo transition to ReceiveStopped + update channels/channelGroups list
                }
                is SubscribeEvent.SubscriptionRestored -> {
                    transitionTo(Receiving(event.channels, event.channelGroups, event.subscriptionCursor)) // todo check if this shouldn't go to ReceiveStopped
                }
                is SubscribeEvent.UnsubscribeAll -> {
                    transitionTo(Unsubscribed)
                }
                else -> {
                    noTransition()
                }
            }
        }
    }

    data class ReceiveFailed(
        private val channels: Set<String>,
        val channelGroups: Set<String>,
        val subscriptionCursor: SubscriptionCursor,
        val reason: PubNubException
    ) : SubscribeState() {
        override fun transition(event: SubscribeEvent): Pair<SubscribeState, Set<SubscribeEffectInvocation>> {
            return when (event) {
                is SubscribeEvent.ReceiveReconnectRetry -> {
                    transitionTo(ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason))
                }
                is SubscribeEvent.Reconnect -> {
                    transitionTo(Receiving(channels, channelGroups, subscriptionCursor))
                }
                is SubscribeEvent.SubscriptionChanged -> {
                    transitionTo(Receiving(event.channels, event.channelGroups, subscriptionCursor))
                }
                is SubscribeEvent.SubscriptionRestored -> {
                    transitionTo(Receiving(event.channels, event.channelGroups, event.subscriptionCursor))
                }
                is SubscribeEvent.UnsubscribeAll -> {
                    transitionTo(Unsubscribed)
                }
                else -> {
                    noTransition()
                }
            }
        }
    }
}
