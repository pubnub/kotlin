package com.pubnub.api.subscribe.eventengine.state

import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

sealed class State {
    open fun onEntry(): List<EffectInvocation> = listOf()
    open fun onExit(): List<EffectInvocation> = listOf()
    open fun transition(event: Event): Pair<State, List<EffectInvocation>> =
        this to listOf()     //zmienić na abstract ?


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
                    //na podstawie current stanu wyciągnij onExit effects
                    println("---State:UNSUBSCRIBED, Event: SUBSCRIPTION_CHANGE, Transition to: HANDSHAKING, Transition effects: ..")
                    stateAfterTransition = Handshaking(event.channels, event.channelGroups)

                    //transitionEffects.add() <--not transition effects here
                }
                is Event.SubscriptionRestored -> {
                    stateAfterTransition = Receiving(event.channels, event.channelGroups, event.subscriptionCursor)
                }
                else -> {
                    //ignore it
                    println("In UNSUBSCRIBED state ignoring event: $event")
                }
            }
            return Pair(stateAfterTransition, transitionEffects)


        }
    }

    data class Handshaking(private val channels: List<String>, private val channelGroups: List<String>) : State() {
        override fun onEntry(): List<EffectInvocation> {
            return listOf(EffectInvocation.HandshakeRequest(channels, channelGroups))
        }

        override fun onExit(): List<EffectInvocation> {
            return listOf(EffectInvocation.CancelPendingHandshakeRequest)
        }

        override fun transition(event: Event): Pair<State, List<EffectInvocation>> {
            var stateAfterTransition: State = this
            val transitionEffects = mutableListOf<EffectInvocation>()

            when (event) {
                is Event.HandshakeSuccess -> {
                    println("---State:, Event: , Transition to: , Transition effects: ..")

                    stateAfterTransition = Receiving(channels, channelGroups, event.subscriptionCursor)
                    transitionEffects.add(EffectInvocation.EmitStatus("Connected")) //toDo change Connected

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
                    //ignore it
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
                    transitionEffects.add(EffectInvocation.EmitStatus("Connected")) //toDo change Connected
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


    object RECONNECTING : State()
    object RECONNECTING_FAILED : State()

    data class ReceiveReconnecting(private val channels: List<String>, val channelGroups: List<String>) :
        State() {  //toDo czy channels jest potrzebne?
        override fun onEntry(): List<EffectInvocation> {
            return listOf(EffectInvocation.ReceiveReconnect(channels, channelGroups))
        }

        override fun onExit(): List<EffectInvocation> {
            return listOf(EffectInvocation.CancelReceiveReconnect)
        }

        override fun transition(event: Event): Pair<State, List<EffectInvocation>> {
            return super.transition(event)
        }
    }

    data class Receiving(
        private val channels: List<String>,
        private val channelGroups: List<String>,
        private val subscriptionCursor: SubscriptionCursor
    ) : State() {
        override fun onEntry(): List<EffectInvocation> {
            return listOf(
                EffectInvocation.ReceiveMessagesRequest(
                    channels,
                    channelGroups,
                    subscriptionCursor
                )
            )  //rename to RECEIVE_EVENTS or what will be agreed
        }

        override fun onExit(): List<EffectInvocation> {
            return listOf(EffectInvocation.CancelReceiveEvents)
        }

        override fun transition(event: Event): Pair<State, List<EffectInvocation>> {
            var stateAfterTransition: State = this
            val transitionEffects = mutableListOf<EffectInvocation>()

            when (event) {
                is Event.ReceiveFailure -> {
                    stateAfterTransition = ReceiveReconnecting(event.channels, event.channelGroups)
                }

                is Event.Disconnect -> {
                    stateAfterTransition = ReceiveStopped()
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
                    transitionEffects.add(EffectInvocation.EmitStatus("Connected"))
                }
                else -> {
                    //ignore it
                    println("In UNSUBSCRIBED state ignoring event: $event")
                }
            }

            return Pair(stateAfterTransition, transitionEffects)
        }
    }

    class ReceiveStopped() : State() {
        override fun onEntry(): List<EffectInvocation> {
            return emptyList()
        }

        override fun onExit(): List<EffectInvocation> {
            return emptyList()
        }

        override fun transition(event: Event): Pair<State, List<EffectInvocation>> {
            //todo
            return super.transition(event)
        }
    }

    class ReceiveFailed() : State() {
        override fun onEntry(): List<EffectInvocation> {
            return emptyList()
        }

        override fun onExit(): List<EffectInvocation> {
            return emptyList()
        }

        override fun transition(event: Event): Pair<State, List<EffectInvocation>> {
            //todo
            return super.transition(event)
        }
    }

    object STOPPED : State()
    object PREPARING : State()
}
