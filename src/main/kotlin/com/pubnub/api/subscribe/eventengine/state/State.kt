package com.pubnub.api.subscribe.eventengine.state

import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

sealed class State {
    open fun onEntry(): List<EffectInvocation> = listOf()
    open fun onExit(): List<EffectInvocation> = listOf()
    open fun transition(event: Event): Pair<State, List<EffectInvocation>> =
        this to listOf() // zmienić na abstract ?

    object Unsubscribed : State() {
        override fun onEntry(): List<EffectInvocation> {
            return listOf()
        }

        override fun onExit(): List<EffectInvocation> {
            return listOf()
        }

        override fun transition(event: Event): Pair<State, List<EffectInvocation>> {
            var stateAfterTransition: State = Unsubscribed
            val transitionEffects = mutableListOf<EffectInvocation>()

            when (event) {
                is Event.SubscriptionChanged -> {
                    // na podstawie current stanu wyciągnij onExit effects
                    println("---State:UNSUBSCRIBED, Event: SUBSCRIPTION_CHANGE, Transition to: HANDSHAKING, Transition effects: ..")
                    stateAfterTransition = Handshaking(event.channels, event.channelGroups)

                    // transitionEffects.add() <--not transition effects here
                }
                is Event.SubscriptionRestored -> {
                    stateAfterTransition = Receiving(event.channels, event.channelGroups, event.subscriptionCursor)
                }
                else -> {
                    // ignore it
                    println("In UNSUBSCRIBED state ignoring event: $event")
                }
            }
            return Pair(stateAfterTransition, transitionEffects)
        }
    }

    data class Handshaking(private val channels: List<String>, private val channelGroups: List<String>) : State() {
        override fun onEntry(): List<EffectInvocation> {
            return listOf(EffectInvocation.Handshake(channels, channelGroups))
        }

        override fun onExit(): List<EffectInvocation> {
            return listOf(EffectInvocation.CancelHandshake)
        }

        override fun transition(event: Event): Pair<State, List<EffectInvocation>> {
            var stateAfterTransition: State = this
            val transitionEffects = mutableListOf<EffectInvocation>()

            when (event) {
                is Event.HandshakeSuccess -> {
                    println("---State:, Event: , Transition to: , Transition effects: ..")

                    stateAfterTransition = Receiving(channels, channelGroups, event.subscriptionCursor)
                    transitionEffects.add(EffectInvocation.EmitStatus("Connected")) // toDo change Connected
                }
                is Event.SubscriptionRestored -> {
                    stateAfterTransition = Receiving(event.channels, event.channelGroups, event.subscriptionCursor)
                }
                is Event.HandshakeFailure -> {
                    stateAfterTransition = HandshakeReconnecting(channels, channelGroups)
                }
                is Event.SubscriptionChanged -> {
                    stateAfterTransition = Handshaking(event.channels, event.channelGroups)
                }
                is Event.Disconnect -> {
                    stateAfterTransition = HandshakeStopped
                }
                else -> {
                    println("In UNSUBSCRIBED state ignoring event: $event")
                }
            }

            return Pair(stateAfterTransition, transitionEffects)
        }
    }

    data class HandshakeReconnecting(private val channels: List<String>, private val channelGroups: List<String>) :
        State() {
        override fun onEntry(): List<EffectInvocation> {
            return listOf(EffectInvocation.HandshakeReconnect(channels, channelGroups))
        }

        override fun onExit(): List<EffectInvocation> {
            return listOf(EffectInvocation.CancelHandshakeReconnect)
        }

        override fun transition(event: Event): Pair<State, List<EffectInvocation>> {
            var stateAfterTransition: State = Unsubscribed
            val transitionEffects = mutableListOf<EffectInvocation>()

            when (event) {
                is Event.HandshakeReconnectFailure -> {
                    stateAfterTransition = HandshakeReconnecting(event.channels, event.channelGroups)
                }
                is Event.SubscriptionChanged -> {
                    stateAfterTransition = HandshakeReconnecting(event.channels, event.channelGroups)
                }
                is Event.Disconnect -> {
                    stateAfterTransition = HandshakeStopped
                }
                is Event.HandshakeReconnectGiveUp -> {
                    stateAfterTransition = HandshakeFailed
                }
                is Event.HandshakeReconnectSuccess -> {
                    stateAfterTransition = Receiving(event.channels, event.channelGroups, event.subscriptionCursor)
                    transitionEffects.add(EffectInvocation.EmitStatus("Connected")) // toDo change Connected
                }
                is Event.SubscriptionRestored -> {
                    stateAfterTransition = Receiving(event.channels, event.channelGroups, event.subscriptionCursor)
                }
                else -> {
                    println("In HandshakeReconnecting state ignoring event: $event")
                }
            }

            return Pair(stateAfterTransition, transitionEffects)
        }
    }

    object HandshakeStopped : State() {
        override fun onEntry(): List<EffectInvocation> {
            return emptyList()
        }

        override fun onExit(): List<EffectInvocation> {
            return emptyList()
        }

        override fun transition(event: Event): Pair<State, List<EffectInvocation>> {
            var stateAfterTransition: State = Unsubscribed
            val transitionEffects = mutableListOf<EffectInvocation>()

            when (event) {
                is Event.Reconnect -> {
                    stateAfterTransition = HandshakeReconnecting(event.channels, event.channelGroups)
                }
                else -> {
                    println("In HandshakeStopped state ignoring event: $event")
                }
            }
            return Pair(stateAfterTransition, transitionEffects)
        }
    }

    object HandshakeFailed : State() {
        override fun onEntry(): List<EffectInvocation> {
            return emptyList()
        }

        override fun onExit(): List<EffectInvocation> {
            return emptyList()
        }

        override fun transition(event: Event): Pair<State, List<EffectInvocation>> {
            var stateAfterTransition: State = Unsubscribed
            val transitionEffects = mutableListOf<EffectInvocation>()

            when (event) {
                is Event.HandshakeReconnectRetry -> {
                    stateAfterTransition = HandshakeReconnecting(event.channels, event.channelGroups)
                }
                is Event.SubscriptionChanged -> {
                    stateAfterTransition = HandshakeReconnecting(event.channels, event.channelGroups)
                }
                is Event.SubscriptionRestored -> {
                    stateAfterTransition = ReceiveReconnecting(event.channels, event.channelGroups)
                }
                is Event.Reconnect -> {
                    stateAfterTransition = HandshakeReconnecting(event.channels, event.channelGroups)
                }
                else -> {
                    println("In HandshakeFailed state ignoring event: $event")
                }
            }

            return Pair(stateAfterTransition, transitionEffects)
        }
    }

    data class ReceiveReconnecting(private val channels: List<String>, val channelGroups: List<String>, val attempts: Int = 0) :
        State() {
        override fun onEntry(): List<EffectInvocation> {
            return listOf(EffectInvocation.ReceiveReconnect(channels, channelGroups))
        }

        override fun onExit(): List<EffectInvocation> {
            return listOf(EffectInvocation.CancelReceiveReconnect)
        }

        override fun transition(event: Event): Pair<State, List<EffectInvocation>> {
            var stateAfterTransition: State = this
            val transitionEffects = mutableListOf<EffectInvocation>()

            when (event) {
                is Event.ReceiveReconnectFailure -> {
                    stateAfterTransition = ReceiveReconnecting(event.channels, event.channelGroups, attempts) // toDo attempts+1
                }
                is Event.SubscriptionChanged -> {
                    stateAfterTransition = ReceiveReconnecting(event.channels, event.channelGroups) // todo sprawdzić czy to ma być z eventa czy ze stanu, we wszytskich przypadkach
                }
                is Event.Disconnect -> {
                    stateAfterTransition = ReceiveStopped(channels, channelGroups)
                }
                is Event.ReceiveReconnectGiveUp -> {
                    stateAfterTransition = ReceiveFailed(channels, channelGroups)
                    transitionEffects.add(EffectInvocation.EmitStatus("Disconnected"))
                }
                is Event.ReceiveReconnectSuccess -> {
                    stateAfterTransition = Receiving(channels, channelGroups, event.subscriptionCursor)
                    transitionEffects.add(EffectInvocation.EmitMessages(event.messages))
                    transitionEffects.add(EffectInvocation.EmitStatus("Connected"))
                }
                is Event.SubscriptionRestored -> {
                    stateAfterTransition = ReceiveReconnecting(event.channels, event.channelGroups)
                }

                else -> {
                    println("In ReceiveReconnecting state ignoring event: $event")
                }
            }

            return Pair(stateAfterTransition, transitionEffects)
        }
    }

    data class Receiving(
        private val channels: List<String>,
        private val channelGroups: List<String>,
        private val subscriptionCursor: SubscriptionCursor
    ) : State() {
        override fun onEntry(): List<EffectInvocation> {
            return listOf(
                EffectInvocation.ReceiveMessages(
                    channels,
                    channelGroups,
                    subscriptionCursor
                )
            )
        }

        override fun onExit(): List<EffectInvocation> {
            return listOf(EffectInvocation.CancelReceiveMessages)
        }

        override fun transition(event: Event): Pair<State, List<EffectInvocation>> {
            var stateAfterTransition: State = this
            val transitionEffects = mutableListOf<EffectInvocation>()

            when (event) {
                is Event.ReceiveFailure -> {
                    stateAfterTransition = ReceiveReconnecting(event.channels, event.channelGroups)
                }

                is Event.Disconnect -> {
                    stateAfterTransition = ReceiveStopped(channels, channelGroups)
                    transitionEffects.add(EffectInvocation.EmitStatus("Disconnected"))
                }
                is Event.SubscriptionChanged -> {
                    stateAfterTransition = Receiving(event.channels, event.channelGroups, event.subscriptionCursor)
                }
                is Event.SubscriptionRestored -> {
                    stateAfterTransition = Receiving(event.channels, event.channelGroups, event.subscriptionCursor)
                }
                is Event.ReceiveSuccess -> {
                    println("---State:, Event: , Transition to: , Transition effects: ..")
                    stateAfterTransition = Receiving(channels, channelGroups, event.subscriptionCursor)
                    transitionEffects.add(EffectInvocation.EmitMessages(event.messages))
                    transitionEffects.add(EffectInvocation.EmitStatus("Connected"))
                }
                else -> {
                    println("In UNSUBSCRIBED state ignoring event: $event")
                }
            }

            return Pair(stateAfterTransition, transitionEffects)
        }
    }

    data class ReceiveStopped(private val channels: List<String>, val channelGroups: List<String>) : State() {
        override fun onEntry(): List<EffectInvocation> {
            return emptyList()
        }

        override fun onExit(): List<EffectInvocation> {
            return emptyList()
        }

        override fun transition(event: Event): Pair<State, List<EffectInvocation>> {
            var stateAfterTransition: State = this
            val transitionEffects = mutableListOf<EffectInvocation>()

            when (event) {
                is Event.Reconnect -> {
                    stateAfterTransition = ReceiveReconnecting(channels, channelGroups)
                }

                else -> {
                    println("In ReceiveFailed state ignoring event: $event")
                }
            }

            return Pair(stateAfterTransition, transitionEffects)
        }
    }

    data class ReceiveFailed(private val channels: List<String>, val channelGroups: List<String>) : State() {
        override fun onEntry(): List<EffectInvocation> {
            return emptyList()
        }

        override fun onExit(): List<EffectInvocation> {
            return emptyList()
        }

        override fun transition(event: Event): Pair<State, List<EffectInvocation>> {
            var stateAfterTransition: State = this
            val transitionEffects = mutableListOf<EffectInvocation>()

            when (event) {
                is Event.ReceiveReconnectRetry -> {
                    stateAfterTransition = ReceiveReconnecting(channels, channelGroups)
                }

                else -> {
                    println("In ReceiveFailed state ignoring event: $event")
                }
            }

            return Pair(stateAfterTransition, transitionEffects)
        }
    }
}
