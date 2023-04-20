package com.pubnub.api.subscribe.eventengine.state

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

sealed class State {
    open fun onEntry(): List<SubscribeEffectInvocation> = listOf()
    open fun onExit(): List<SubscribeEffectInvocation> = listOf()
    abstract fun transition(event: Event): Pair<State, List<SubscribeEffectInvocation>>

    object Unsubscribed : State() {
        override fun transition(event: Event): Pair<State, List<SubscribeEffectInvocation>> {
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
    ) : State() {
        override fun onEntry() = listOf(SubscribeEffectInvocation.Handshake(channels, channelGroups))
        override fun onExit() = listOf(SubscribeEffectInvocation.CancelHandshake)

        override fun transition(event: Event): Pair<State, List<SubscribeEffectInvocation>> {
            return when (event) {
                is Event.HandshakeSuccess -> {
                    transitionTo(
                        state = Receiving(this.channels, channelGroups, event.subscriptionCursor),
                        SubscribeEffectInvocation.EmitStatus(PNStatusCategory.PNConnectedCategory)
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
    ) : State() {
        override fun onEntry() = listOf(SubscribeEffectInvocation.HandshakeReconnect(channels, channelGroups, attempts, reason))

        override fun onExit() = listOf(SubscribeEffectInvocation.CancelHandshakeReconnect)

        override fun transition(event: Event): Pair<State, List<SubscribeEffectInvocation>> {
            return when (event) {
                is Event.HandshakeReconnectFailure -> {
                    transitionTo(
                        HandshakeReconnecting(
                            this.channels, this.channelGroups, this.attempts + 1, event.reason
                        )
                    )
                }

                is Event.SubscriptionChanged -> {
                    transitionTo(HandshakeReconnecting(event.channels, event.channelGroups, 0, this.reason))
                }

                is Event.Disconnect -> {
                    transitionTo(HandshakeStopped(channels, channelGroups, reason))
                }

                is Event.HandshakeReconnectGiveUp -> {
                    transitionTo(HandshakeFailed(this.channels, this.channelGroups, event.reason))
                }

                is Event.HandshakeReconnectSuccess -> {
                    transitionTo(
                        state = Receiving(event.channels, event.channelGroups, event.subscriptionCursor),
                        SubscribeEffectInvocation.EmitStatus(PNStatusCategory.PNConnectedCategory)
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
    ) : State() {

        override fun transition(event: Event): Pair<State, List<SubscribeEffectInvocation>> {
            return when (event) {
                is Event.Reconnect -> {
                    transitionTo(HandshakeReconnecting(channels, channelGroups, 0, this.reason))
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
    ) : State() {

        override fun transition(event: Event): Pair<State, List<SubscribeEffectInvocation>> {
            return when (event) {
                is Event.HandshakeReconnectRetry -> {
                    transitionTo(HandshakeReconnecting(channels, channelGroups, 0, reason))
                }

                is Event.SubscriptionChanged -> {
                    transitionTo(HandshakeReconnecting(event.channels, event.channelGroups, 0, reason))
                }

                is Event.SubscriptionRestored -> {
                    transitionTo(
                        ReceiveReconnecting(
                            event.channels, event.channelGroups, event.subscriptionCursor, 0, reason
                        )
                    )
                }

                is Event.Reconnect -> {
                    transitionTo(HandshakeReconnecting(channels, channelGroups, 0, reason))
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
    ) : State() {
        override fun onEntry() =
            listOf(SubscribeEffectInvocation.ReceiveReconnect(channels, channelGroups, subscriptionCursor, attempts, reason))

        override fun onExit() = listOf(SubscribeEffectInvocation.CancelReceiveReconnect)

        override fun transition(event: Event): Pair<State, List<SubscribeEffectInvocation>> {
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
                        ReceiveReconnecting(
                            event.channels, event.channelGroups, subscriptionCursor, 0, reason
                        )
                    )
                }

                is Event.Disconnect -> {
                    transitionTo(ReceiveStopped(channels, channelGroups, subscriptionCursor))
                }

                is Event.ReceiveReconnectGiveUp -> {
                    transitionTo(
                        state = ReceiveFailed(channels, channelGroups, subscriptionCursor, event.reason),
                        SubscribeEffectInvocation.EmitStatus(PNStatusCategory.PNDisconnectedCategory)
                    )
                }

                is Event.ReceiveReconnectSuccess -> {
                    transitionTo(
                        state = Receiving(channels, channelGroups, event.subscriptionCursor),
                        SubscribeEffectInvocation.EmitMessages(event.messages),
                        SubscribeEffectInvocation.EmitStatus(PNStatusCategory.PNConnectedCategory)
                    )
                }

                is Event.SubscriptionRestored -> {
                    transitionTo(
                        ReceiveReconnecting(event.channels, event.channelGroups, subscriptionCursor, 0, reason)
                    )
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
    ) : State() {
        override fun onEntry() = listOf(
            SubscribeEffectInvocation.ReceiveMessages(
                channels, channelGroups, subscriptionCursor
            )
        )

        override fun onExit() = listOf(SubscribeEffectInvocation.CancelReceiveMessages)

        override fun transition(event: Event): Pair<State, List<SubscribeEffectInvocation>> {

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
                        SubscribeEffectInvocation.EmitStatus(PNStatusCategory.PNDisconnectedCategory)
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
                        SubscribeEffectInvocation.EmitStatus(PNStatusCategory.PNConnectedCategory)
                    )
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
    ) : State() {
        override fun transition(event: Event): Pair<State, List<SubscribeEffectInvocation>> {
            return when (event) {
                is Event.Reconnect -> {
                    transitionTo(ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, null))
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
    ) : State() {
        override fun transition(event: Event): Pair<State, List<SubscribeEffectInvocation>> {
            return when (event) {
                is Event.ReceiveReconnectRetry -> {
                    transitionTo(ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason))
                }

                is Event.Reconnect -> {
                    transitionTo(ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason))
                }

                else -> {
                    noTransition()
                }
            }
        }
    }
}

private fun State.noTransition(): Pair<State, List<SubscribeEffectInvocation>> = Pair(this, emptyList())
private fun State.transitionTo(
    state: State,
    vararg invocations: SubscribeEffectInvocation
): Pair<State, List<SubscribeEffectInvocation>> {
    val effectInvocations = this.onExit() + invocations + state.onEntry()
    return Pair(state, effectInvocations)
}
