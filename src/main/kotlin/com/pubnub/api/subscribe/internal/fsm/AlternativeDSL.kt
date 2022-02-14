package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.AbstractState
import com.pubnub.api.state.Effect
import com.pubnub.api.state.Event
import com.pubnub.api.subscribe.*
import com.pubnub.api.subscribe.HandshakeResult.HandshakeSucceeded
import com.pubnub.api.subscribe.internal.fsm.SubscribeStates
import com.pubnub.api.subscribe.internal.fsm.SubscribeStates.Handshaking
import com.pubnub.api.subscribe.internal.fsm.SubscribeStates.Unsubscribed
import com.pubnub.api.subscribe.internal.fsm.plus
import kotlin.reflect.KClass


interface StateMachineBuilder<I : Event, E : Effect, S : AbstractState<I, E>>

fun <I : Event, E : Effect, S : AbstractState<I, E>> startFSMWith(stateCreator: () -> S): StateMachineBuilder<I, E, S> {
    TODO()
}

fun <I : Event, E : Effect, S : AbstractState<I, E>> startFSMWith2(
    initState: S,
    hugeSwitch: (S, I) -> Pair<S?, Collection<E>>?
) {
    TODO()
}


infix fun <I : Event, E : Effect, S : AbstractState<I, E>> StateMachineBuilder<I, E, S>.andThen(build: StateMachineBuilder<I, E, S>.() -> Unit) {

}

infix fun <I : Event, E : Effect, S : AbstractState<I, E>> StateMachineBuilder<I, E, S>.andWhen(hugeSwitch: (S, I) -> (Pair<S, Collection<E>>)) {
    TODO()
}

class StateBuilder<I : Event, E : Effect, S : AbstractState<I, E>, SS : S> {

}

fun <I : Event, E : Effect, S : AbstractState<I, E>, II : I, SS : S> StateBuilder<I, E, S, SS>.onEntry(build: SS.() -> Collection<Effect>) {

}

fun <I : Event, E : Effect, S : AbstractState<I, E>, II : I, SS : S> StateBuilder<I, E, S, SS>.onExit(build: SS.() -> Collection<Effect>) {

}


fun <I : Event, E : Effect, S : AbstractState<I, E>, SS : S> StateMachineBuilder<I, E, S>.forState(
    clazz: KClass<SS>,
    onEntry: SS.() -> Collection<Effect> = { listOf() },
    onExit: SS.() -> Collection<Effect> = { listOf() },
    build: StateBuilder<I, E, S, SS>.() -> Unit
) {

}

fun <I : Event, E : Effect, S : AbstractState<I, E>, II : I, SS : S> StateBuilder<I, E, S, SS>.forInput(clazz: KClass<II>): SingleStateTransitionBuilder<I, E, S, II> {
    TODO()
}

interface SingleStateTransitionBuilder<I : Event, E : Effect, S : AbstractState<I, E>, II : I>

infix fun <I : Event, E : Effect, S : AbstractState<I, E>, II : I, SS : S> SingleStateTransitionBuilder<I, E, S, II>.transitionTo(
    build: SingleStateTransitionBuilder<I, E, S, II>.(II, S) -> SS?
) {

}

infix fun <I : Event, E : Effect, S : AbstractState<I, E>, II : I, SS : S> SingleStateTransitionBuilder<I, E, S, II>.transitionWithEffects(
    build: SingleStateTransitionBuilder<I, E, S, II>.(II, S) -> Pair<SS?, Collection<Effect>>
) {

}

fun aa() {
    startFSMWith<SubscribeEvent, Effect, SubscribeStates> { Unsubscribed() } andThen {
        forState(Unsubscribed::class) {
            forInput(Commands.SubscribeCommandIssued::class) transitionTo { i, s ->
                Handshaking(s.subscriptionStatus + i)
            }
        }
        forState(
            Handshaking::class,
            onEntry = { listOf(call) },
            onExit = { listOf(EndHttpCallEffect(call.id)) }
        ) {
            forInput(Commands.SubscribeCommandIssued::class) transitionTo { i, s ->
                s.subscriptionStatus.ifDifferent(s.subscriptionStatus + i) { newBag ->
                    Handshaking(newBag)
                }

            }
            forInput(HandshakeSucceeded::class) transitionTo { i, s ->
                SubscribeStates.Receiving(s.subscriptionStatus.copy(cursor = i.cursor))
            }
            forInput(HandshakeSucceeded::class) transitionWithEffects { i, s ->
                SubscribeStates.Receiving(s.subscriptionStatus.copy(cursor = i.cursor)) to listOf()
            }
        }
    }

//    val machineDescription = machineDescription<SubscribeEvent, EnumState> { st ->
//        when (st) {
//            EnumState.Unsubscribed -> transitionDescription {
//                when (ev) {
//                    is SubscribeCommands.SubscribeCommandIssued -> transitionTo(EnumState.Handshaking(st.status + ev))
//                    else -> noTransition()
//                }
//            }
//            is EnumState.Receiving -> transitionDescription {
//                when (ev) {
//                    ReceivingResult.ReceivingFail -> TODO()
//                    is ReceivingResult.ReceivingSuccess -> transitionTo(EnumState.Receiving(st.status + ev))
//                    is SubscribeCommands -> transitionTo(
//                        state = when (ev) {
//                            is SubscribeCommands.SubscribeCommandIssued -> EnumState.Receiving(st.status + ev)
//                            SubscribeCommands.UnsubscribeAllCommandIssued -> EnumState.Unsubscribed
//                            is SubscribeCommands.UnsubscribeCommandIssued -> EnumState.Receiving(st.status + ev)
//                        },
//                        effects = listOf(EndHttpCallEffect(st.call.id))
//                    )
//                    else -> noTransition()
//                }
//            }
//            is EnumState.Handshaking -> transitionDescription {
//                when (ev) {
//                    HandshakeResult.HandshakeFail -> TODO()
//                    is HandshakeSuccess -> transitionTo(EnumState.Receiving(st.status + ev))
//                    is SubscribeCommands -> {
//                        transitionTo(
//                            state = when (ev) {
//                                is SubscribeCommands.SubscribeCommandIssued -> EnumState.Handshaking(st.status + ev)
//                                SubscribeCommands.UnsubscribeAllCommandIssued -> EnumState.Unsubscribed
//                                is SubscribeCommands.UnsubscribeCommandIssued -> EnumState.Handshaking(st.status + ev)
//                            },
//                            effects = listOf(EndHttpCallEffect(st.call.id))
//                        )
//                    }
//                    else -> noTransition()
//                }
//            }
//            else -> TODO("WHY?!")
//        }
//    }
}







data class EnumBasedTransitionBuilder<I : Event, E : Effect, S>(
    val input: E,
    val state: S
) {


}

fun <I : Event, E : Effect, S> EnumBasedTransitionBuilder<I, E, S>.noTransition(): Pair<S, Collection<E>> {
    return state to listOf()
}

fun <I : Event, E : Effect, S> EnumBasedTransitionBuilder<I, E, S>.transitionTo(
    state: S,
    effects: Collection<E> = listOf()
): Pair<S, Collection<E>> {
    return state to effects
}




internal fun SubscriptionStatus.ifDifferent(
    newStateBag: SubscriptionStatus,
    newStateFactory: (SubscriptionStatus) -> SubscribeStates
): SubscribeStates? {
    return if (newStateBag != this) {
        newStateFactory(newStateBag)
    } else {
        null
    }
}
