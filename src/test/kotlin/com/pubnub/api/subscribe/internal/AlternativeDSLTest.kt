package com.pubnub.api.subscribe.internal

import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.models.server.SubscribeMetaData
import com.pubnub.api.state.Event
import com.pubnub.api.subscribe.*
import com.pubnub.api.subscribe.internal.fsm.SubscribeStates
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test

class AlternativeDSLTest {
    data class TransitionsDescriptionContext(
        val state: EnumState,
        val event: SubscribeEvent

    ) {
        val updatedStatus: SubscriptionStatus = state.status + event

        operator fun SubscriptionStatus.plus(event: SubscribeEvent): SubscriptionStatus {
            return when (event) {
                is HandshakeResult.HandshakeSucceeded -> copy(cursor = event.cursor)
                is ReceivingResult.ReceivingSucceeded -> copy(
                    cursor = Cursor(
                        timetoken = event.subscribeEnvelope.metadata.timetoken,
                        region = event.subscribeEnvelope.metadata.region
                    )
                )
                is Commands.SubscribeCommandIssued -> copy(
                    channels = channels + event.channels.toSet(),
                    groups = groups + event.groups.toSet(),
                    cursor = event.cursor ?: cursor
                )
                Commands.UnsubscribeAllCommandIssued -> SubscriptionStatus()
                is Commands.UnsubscribeCommandIssued -> copy(
                    channels = channels - event.channels.toSet(),
                    groups = groups - event.groups.toSet(),
                )
                else -> this
            }
        }

    }


    fun transitions(hugeSwitch: TransitionsDescriptionContext.(EnumState, SubscribeEvent) -> Pair<EnumState, Collection<AbstractSubscribeEffect>>): (EnumState, SubscribeEvent) -> Pair<EnumState, Collection<AbstractSubscribeEffect>> {
        return { s, i ->
            val (newState, newEffects) = TransitionsDescriptionContext(s, i).hugeSwitch(s, i)
            if (s == newState) {
                newState to newEffects
            } else {
                newState to (s.onExit() + newState.onEntry() + newEffects)
            }
        }
    }


    sealed class EnumState {
        abstract val status: SubscriptionStatus
        protected open fun additionalOnEntry(): Collection<AbstractSubscribeEffect> = listOf()
        open fun onExit(): Collection<AbstractSubscribeEffect> = listOf()
        open fun onEntry(): Collection<AbstractSubscribeEffect> = listOf(
            NewStateEffect(
                name = this::class.simpleName!!,
                status = status
            )
        ) + additionalOnEntry()

        object Unsubscribed : EnumState() {
            override val status: SubscriptionStatus = SubscriptionStatus()
        }

        data class Receiving(
            override val status: SubscriptionStatus,
            val call: SubscribeHttpEffect.HandshakeHttpCallEffect = SubscribeHttpEffect.HandshakeHttpCallEffect(
                status
            ),
        ) : EnumState() {
            override fun additionalOnEntry(): Collection<AbstractSubscribeEffect> = listOf(call)
        }

        data class Handshaking(
            override val status: SubscriptionStatus,
            val call: SubscribeHttpEffect.ReceiveMessagesHttpCallEffect = SubscribeHttpEffect.ReceiveMessagesHttpCallEffect(
                status
            ),
        ) : EnumState() {
            override fun additionalOnEntry(): Collection<AbstractSubscribeEffect> = listOf(call)
        }
    }

    fun TransitionsDescriptionContext.noTransition(): Pair<EnumState, Collection<AbstractSubscribeEffect>> {
        return state to listOf()
    }

    fun TransitionsDescriptionContext.transitionTo(
        state: EnumState,
        vararg effects: AbstractSubscribeEffect
    ): Pair<EnumState, Collection<AbstractSubscribeEffect>> {
        return state to effects.toList()
    }


    fun stateMachine(initState: EnumState, transitions: (EnumState, SubscribeEvent) -> Pair<EnumState, Collection<AbstractSubscribeEffect>>): (SubscribeEvent) -> Collection<AbstractSubscribeEffect> {
        TODO()
    }


    @Test
    fun a() {
        val fn: (EnumState, SubscribeEvent) -> Pair<EnumState, Collection<AbstractSubscribeEffect>> =
            transitions { st, ev ->
                when (st) {
                    is EnumState.Handshaking -> when (ev) {
                        is Commands -> {
                            transitionTo(
                                when (ev) {
                                    is Commands.SubscribeCommandIssued -> EnumState.Handshaking(updatedStatus)
                                    Commands.UnsubscribeAllCommandIssued -> EnumState.Unsubscribed
                                    is Commands.UnsubscribeCommandIssued -> EnumState.Handshaking(updatedStatus)
                                }
                            )
                        }
                        HandshakeResult.HandshakeFailed -> noTransition()
                        is HandshakeResult.HandshakeSucceeded -> transitionTo(EnumState.Receiving(updatedStatus))
                        else -> noTransition()
                    }
                    is EnumState.Receiving -> when (ev) {
                        is Commands -> {
                            transitionTo(
                                when (ev) {
                                    is Commands.SubscribeCommandIssued -> EnumState.Receiving(updatedStatus)
                                    Commands.UnsubscribeAllCommandIssued -> EnumState.Unsubscribed
                                    is Commands.UnsubscribeCommandIssued -> EnumState.Receiving(updatedStatus)
                                }
                            )
                        }
                        ReceivingResult.ReceivingFailed -> noTransition()
                        is ReceivingResult.ReceivingSucceeded -> transitionTo(
                            EnumState.Receiving(updatedStatus),
                            NewMessagesEffect(ev.subscribeEnvelope.messages)
                        )
                        else -> noTransition()
                    }
                    EnumState.Unsubscribed -> when (ev) {
                        is Commands.SubscribeCommandIssued -> transitionTo(EnumState.Handshaking(st.status + ev))
                        else -> noTransition()
                    }
                }
            }

        val (ns, ef) = fn(EnumState.Unsubscribed, Commands.SubscribeCommandIssued(channels = listOf("ch1")))


        println(ns to ef)
        println(fn(ns, Commands.SubscribeCommandIssued(channels = listOf("ch2"))))
        println(fn(ns, HandshakeResult.HandshakeFailed))

        val inputs = listOf(
            Commands.SubscribeCommandIssued(channels = listOf("ch1")),
            HandshakeResult.HandshakeSucceeded(cursor = Cursor(timetoken = 5, region = "12")),
            ReceivingResult.ReceivingSucceeded(
                SubscribeEnvelope(
                    messages = listOf(),
                    metadata = SubscribeMetaData(timetoken = 5, region = "13")
                )
            )
        )

        val (s, effects) = inputs.fold<SubscribeEvent, Pair<EnumState, Collection<AbstractSubscribeEffect>>>(EnumState.Unsubscribed to listOf()) { (s, ef), ev ->
            val (ns, nEf) = fn(s, ev)
            ns to (ef + nEf)
        }

        println(effects)

        MatcherAssert.assertThat(
            effects.mapNotNull { if (it is NewStateEffect) it.name else null },
            Matchers.`is`(
                listOf(
                    SubscribeStates.Handshaking::class.simpleName,
                    SubscribeStates.Receiving::class.simpleName,
                    SubscribeStates.Receiving::class.simpleName
                )
            )
        )

    }
}