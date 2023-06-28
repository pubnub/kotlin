package com.pubnub.api.subscribe.eventengine.state

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.eventengine.State
import com.pubnub.api.eventengine.noTransition
import com.pubnub.api.eventengine.transitionTo
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

sealed class SubscribeState : State<SubscribeEffectInvocation, Event, SubscribeState> {
    object Unsubscribed : SubscribeState() {
        override fun transition(event: Event): Pair<SubscribeState, List<SubscribeEffectInvocation>> {
            return when (event) {
                is Event.SubscriptionChanged -> {
                    transitionTo(Handshaking(event.channels, event.channelGroups))
                }

                is Event.SubscriptionRestored -> {
                    transitionTo(Receiving(event.channels, event.channelGroups, event.subscriptionCursor))
                }

                else -> {
                    noTransition()
                }
            }
        }
    }

    data class Handshaking(
        val channels: List<String>,
        val channelGroups: List<String>
    ) : SubscribeState() {
        override fun onEntry() = listOf(SubscribeEffectInvocation.Handshake(channels, channelGroups))
        override fun onExit() = listOf(SubscribeEffectInvocation.CancelHandshake)

        override fun transition(event: Event): Pair<SubscribeState, List<SubscribeEffectInvocation>> {
            return when (event) {
                is Event.HandshakeSuccess -> {
                    transitionTo(
                        state = Receiving(channels, channelGroups, event.subscriptionCursor),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                category = PNStatusCategory.PNConnectedCategory,
                                operation = PNOperationType.PNSubscribeOperation, // todo is PNSubscribeOperation correct operation
                                error = false,
                                affectedChannels = channels,
                                affectedChannelGroups = channelGroups
                            )
                        )
                    )
                }

                is Event.SubscriptionRestored -> {
                    transitionTo(Receiving(event.channels, event.channelGroups, event.subscriptionCursor))
                }

                is Event.HandshakeFailure -> {
                    transitionTo(HandshakeReconnecting(channels, channelGroups, 0, event.reason))
                }

                is Event.SubscriptionChanged -> {
                    transitionTo(Handshaking(event.channels, event.channelGroups))
                }

                is Event.Disconnect -> {
                    transitionTo(HandshakeStopped(channels, channelGroups, reason = null))
                }

                else -> {
                    noTransition()
                }
            }
        }
    }

    data class HandshakeReconnecting(
        val channels: List<String>,
        val channelGroups: List<String>,
        val attempts: Int,
        val reason: PubNubException?
    ) : SubscribeState() {
        override fun onEntry() =
            listOf(SubscribeEffectInvocation.HandshakeReconnect(channels, channelGroups, attempts, reason))

        override fun onExit() = listOf(SubscribeEffectInvocation.CancelHandshakeReconnect)

        override fun transition(event: Event): Pair<SubscribeState, List<SubscribeEffectInvocation>> {
            return when (event) {
                is Event.HandshakeReconnectFailure -> {
                    transitionTo(
                        HandshakeReconnecting(
                            this.channels, this.channelGroups, this.attempts + 1, event.reason
                        )
                    )
                }

                is Event.SubscriptionChanged -> {
                    transitionTo(Handshaking(event.channels, event.channelGroups))
                }

                is Event.Disconnect -> {
                    transitionTo(HandshakeStopped(channels, channelGroups, reason))
                }

                is Event.HandshakeReconnectGiveup -> {
                    transitionTo(
                        HandshakeFailed(this.channels, this.channelGroups, event.reason),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                category = PNStatusCategory.PNDisconnectedCategory,
                                operation = PNOperationType.PNSubscribeOperation,
                                error = true,
                                affectedChannels = channels,
                                affectedChannelGroups = channelGroups,
                                exception = event.reason
                            )
                        )
                    )
                }

                is Event.HandshakeReconnectSuccess -> {
                    transitionTo(
                        state = Receiving(channels, channelGroups, event.subscriptionCursor),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                category = PNStatusCategory.PNConnectedCategory,
                                operation = PNOperationType.PNSubscribeOperation, // todo is PNSubscribeOperation correct operation
                                error = false,
                                affectedChannels = channels,
                                affectedChannelGroups = channelGroups
                            )
                        )
                    )
                }

                is Event.SubscriptionRestored -> {
                    transitionTo(Receiving(event.channels, event.channelGroups, event.subscriptionCursor))
                }

                else -> {
                    noTransition()
                }
            }
        }
    }

    data class HandshakeStopped(
        val channels: List<String>,
        val channelGroups: List<String>,
        val reason: PubNubException?
    ) : SubscribeState() {

        override fun transition(event: Event): Pair<SubscribeState, List<SubscribeEffectInvocation>> {
            return when (event) {
                is Event.Reconnect -> {
                    transitionTo(Handshaking(channels, channelGroups))
                }

                else -> {
                    noTransition()
                }
            }
        }
    }

    data class HandshakeFailed(
        val channels: List<String>,
        val channelGroups: List<String>,
        val reason: PubNubException
    ) : SubscribeState() {

        override fun transition(event: Event): Pair<SubscribeState, List<SubscribeEffectInvocation>> {
            return when (event) {
                is Event.HandshakeReconnectRetry -> {
                    transitionTo(HandshakeReconnecting(channels, channelGroups, 0, reason))
                }

                is Event.SubscriptionChanged -> {
                    transitionTo(Handshaking(event.channels, event.channelGroups))
                }

                is Event.SubscriptionRestored -> {
                    transitionTo(
                        Receiving(
                            event.channels, event.channelGroups, event.subscriptionCursor
                        )
                    )
                }

                is Event.Reconnect -> {
                    transitionTo(Handshaking(channels, channelGroups))
                }

                else -> {
                    noTransition()
                }
            }
        }
    }

    data class Receiving(
        private val channels: List<String>,
        private val channelGroups: List<String>,
        private val subscriptionCursor: SubscriptionCursor
    ) : SubscribeState() {
        override fun onEntry() = listOf(
            SubscribeEffectInvocation.ReceiveMessages(
                channels, channelGroups, subscriptionCursor
            )
        )

        override fun onExit() = listOf(SubscribeEffectInvocation.CancelReceiveMessages)

        override fun transition(event: Event): Pair<SubscribeState, List<SubscribeEffectInvocation>> {

            return when (event) {
                is Event.ReceiveFailure -> {
                    transitionTo(
                        ReceiveReconnecting(
                            this.channels, this.channelGroups, subscriptionCursor, 0, event.reason
                        )
                    )
                }

                is Event.Disconnect -> {
                    transitionTo(
                        state = ReceiveStopped(channels, channelGroups, subscriptionCursor),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                category = PNStatusCategory.PNDisconnectedCategory,
                                operation = PNOperationType.PNSubscribeOperation, // todo is PNSubscribeOperation correct operation
                                error = false, // todo is PNDisconnectedCategory error
                                affectedChannels = channels,
                                affectedChannelGroups = channelGroups
                            )
                        )
                    )
                }

                is Event.SubscriptionChanged -> {
                    transitionTo(Receiving(event.channels, event.channelGroups, subscriptionCursor))
                }

                is Event.SubscriptionRestored -> {
                    transitionTo(Receiving(event.channels, event.channelGroups, event.subscriptionCursor))
                }

                is Event.ReceiveSuccess -> {
                    transitionTo(
                        state = Receiving(channels, channelGroups, event.subscriptionCursor),
                        SubscribeEffectInvocation.EmitMessages(event.messages),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                category = PNStatusCategory.PNConnectedCategory,
                                operation = PNOperationType.PNSubscribeOperation, // todo is PNSubscribeOperation correct operation
                                error = false,
                                affectedChannels = channels,
                                affectedChannelGroups = channelGroups
                            )
                        )
                    )
                }

                else -> {
                    noTransition()
                }
            }
        }
    }

    data class ReceiveReconnecting(
        val channels: List<String>,
        val channelGroups: List<String>,
        val subscriptionCursor: SubscriptionCursor,
        val attempts: Int,
        val reason: PubNubException?
    ) : SubscribeState() {
        override fun onEntry() =
            listOf(
                SubscribeEffectInvocation.ReceiveReconnect(
                    channels,
                    channelGroups,
                    subscriptionCursor,
                    attempts,
                    reason
                )
            )

        override fun onExit() = listOf(SubscribeEffectInvocation.CancelReceiveReconnect)

        override fun transition(event: Event): Pair<SubscribeState, List<SubscribeEffectInvocation>> {
            return when (event) {
                is Event.ReceiveReconnectFailure -> {
                    transitionTo(
                        ReceiveReconnecting(
                            channels, channelGroups, subscriptionCursor, attempts + 1, event.reason
                        )
                    )
                }

                is Event.SubscriptionChanged -> {
                    transitionTo(
                        Receiving(event.channels, event.channelGroups, subscriptionCursor)
                    )
                }

                is Event.Disconnect -> {
                    transitionTo(ReceiveStopped(channels, channelGroups, subscriptionCursor))
                }

                is Event.ReceiveReconnectGiveup -> {
                    transitionTo(
                        state = ReceiveFailed(channels, channelGroups, subscriptionCursor, event.reason),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                category = PNStatusCategory.PNDisconnectedCategory,
                                operation = PNOperationType.PNSubscribeOperation, // todo is PNSubscribeOperation correct operation
                                error = false, // todo is PNDisconnectedCategory error
                                affectedChannels = channels,
                                affectedChannelGroups = channelGroups
                            )
                        )
                    )
                }

                is Event.ReceiveReconnectSuccess -> {
                    transitionTo(
                        state = Receiving(channels, channelGroups, event.subscriptionCursor),
                        SubscribeEffectInvocation.EmitMessages(event.messages),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                category = PNStatusCategory.PNConnectedCategory,
                                operation = PNOperationType.PNSubscribeOperation, // todo is PNSubscribeOperation correct operation
                                error = false,
                                affectedChannels = channels,
                                affectedChannelGroups = channelGroups
                            )
                        )
                    )
                }

                is Event.SubscriptionRestored -> {
                    transitionTo(Receiving(event.channels, event.channelGroups, subscriptionCursor))
                }

                else -> {
                    noTransition()
                }
            }
        }
    }

    data class ReceiveStopped(
        private val channels: List<String>,
        val channelGroups: List<String>,
        val subscriptionCursor: SubscriptionCursor
    ) : SubscribeState() {
        override fun transition(event: Event): Pair<SubscribeState, List<SubscribeEffectInvocation>> {
            return when (event) {
                is Event.Reconnect -> {
                    transitionTo(Receiving(channels, channelGroups, subscriptionCursor))
                }

                else -> {
                    noTransition()
                }
            }
        }
    }

    data class ReceiveFailed(
        private val channels: List<String>,
        val channelGroups: List<String>,
        val subscriptionCursor: SubscriptionCursor,
        val reason: PubNubException
    ) : SubscribeState() {
        override fun transition(event: Event): Pair<SubscribeState, List<SubscribeEffectInvocation>> {
            return when (event) {
                is Event.ReceiveReconnectRetry -> {
                    transitionTo(ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason))
                }

                is Event.Reconnect -> {
                    transitionTo(Receiving(channels, channelGroups, subscriptionCursor))
                }

                is Event.SubscriptionChanged -> {
                    transitionTo(Receiving(event.channels, event.channelGroups, subscriptionCursor))
                }

                is Event.SubscriptionRestored -> {
                    transitionTo(Receiving(event.channels, event.channelGroups, event.subscriptionCursor))
                }
                else -> {
                    noTransition()
                }
            }
        }
    }
}
