package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.AbstractState
import com.pubnub.api.state.Effect
import com.pubnub.api.state.Input
import com.pubnub.api.subscribe.SubscribeInput
import com.pubnub.api.subscribe.HandshakeResult.HandshakeSuccess
import com.pubnub.api.subscribe.SubscribeCommands
import com.pubnub.api.subscribe.internal.SubscribeStates.Handshaking
import com.pubnub.api.subscribe.internal.SubscribeStates.Unsubscribed
import kotlin.reflect.KClass


interface StateMachineBuilder<I : Input, E : Effect, S : AbstractState<I, E>>

fun <I : Input, E : Effect, S : AbstractState<I, E>> startFSMWith(stateCreator: () -> S): StateMachineBuilder<I, E, S> {
    TODO()
}

infix fun <I : Input, E : Effect, S : AbstractState<I, E>> StateMachineBuilder<I, E, S>.andThen(build: StateMachineBuilder<I, E, S>.() -> Unit) {

}

class StateBuilder<I : Input, E : Effect, S : AbstractState<I, E>, SS : S> {

}

fun <I : Input, E : Effect, S : AbstractState<I, E>, II : I, SS : S> StateBuilder<I, E, S, SS>.onEntry(build: SS.() -> Collection<Effect>) {

}

fun <I : Input, E : Effect, S : AbstractState<I, E>, II : I, SS : S> StateBuilder<I, E, S, SS>.onExit(build: SS.() -> Collection<Effect>) {

}


fun <I : Input, E : Effect, S : AbstractState<I, E>, SS : S> StateMachineBuilder<I, E, S>.forState(
    clazz: KClass<SS>,
    onEntry: SS.() -> Collection<Effect> = { listOf() },
    onExit: SS.() -> Collection<Effect> = { listOf() },
    build: StateBuilder<I, E, S, SS>.() -> Unit
) {

}

fun <I : Input, E : Effect, S : AbstractState<I, E>, II : I, SS : S> StateBuilder<I, E, S, SS>.forInput(clazz: KClass<II>): SingleStateTransitionBuilder<I, E, S, II> {
    TODO()
}

interface SingleStateTransitionBuilder<I : Input, E : Effect, S : AbstractState<I, E>, II : I>

infix fun <I : Input, E : Effect, S : AbstractState<I, E>, II : I, SS : S> SingleStateTransitionBuilder<I, E, S, II>.transitionTo(build: SingleStateTransitionBuilder<I, E, S, II>.(II, S) -> SS?) {

}

infix fun <I : Input, E : Effect, S : AbstractState<I, E>, II : I, SS : S> SingleStateTransitionBuilder<I, E, S, II>.transitionWithEffects(
    build: SingleStateTransitionBuilder<I, E, S, II>.(II, S) -> Pair<SS?, Collection<Effect>>
) {

}

fun aa() {
    startFSMWith<SubscribeInput, Effect, SubscribeStates> { Unsubscribed() } andThen {
        forState(Unsubscribed::class) {
            forInput(SubscribeCommands.Subscribe::class) transitionTo { i, s ->
                Handshaking(s.stateBag + i)
            }
        }
        forState(
            Handshaking::class,
            onEntry = { listOf(call) },
            onExit = { listOf(EndHttpCallEffect(call.id)) }
        ) {
            forInput(SubscribeCommands.Subscribe::class) transitionTo { i, s ->
                s.stateBag.ifDifferent(s.stateBag + i) { newBag ->
                    Handshaking(newBag)
                }

            }
            forInput(HandshakeSuccess::class) transitionTo { i, s ->
                SubscribeStates.Receiving(s.stateBag.copy(cursor = i.cursor))
            }
            forInput(HandshakeSuccess::class) transitionWithEffects { i, s ->
                SubscribeStates.Receiving(s.stateBag.copy(cursor = i.cursor)) to listOf()
            }
        }
    }
}

internal fun SubscribeStateBag.ifDifferent(
    newStateBag: SubscribeStateBag,
    newStateFactory: (SubscribeStateBag) -> SubscribeStates
): SubscribeStates? {
    return if (newStateBag != this) {
        newStateFactory(newStateBag)
    } else {
        null
    }
}
