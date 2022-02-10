package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.AbstractState
import com.pubnub.api.state.Effect
import com.pubnub.api.state.Input
import kotlin.reflect.KClass


interface StateMachineBuilder<I : Input, S : AbstractState<I>>

fun <I : Input, S : AbstractState<I>> startFSMWith(stateCreator: () -> S): StateMachineBuilder<I, S> {
    TODO()
}

infix fun <I : Input, S : AbstractState<I>> StateMachineBuilder<I, S>.andThen(build: StateMachineBuilder<I, S>.() -> Unit) {

}

class StateBuilder<I : Input, S : AbstractState<I>, SS : S> {

}

fun <I : Input, S : AbstractState<I>, II : I, SS : S> StateBuilder<I, S, SS>.onEntry(build: SS.() -> Collection<Effect>) {

}

fun <I : Input, S : AbstractState<I>, II : I, SS : S> StateBuilder<I, S, SS>.onExit(build: SS.() -> Collection<Effect>) {

}


fun <I : Input, S : AbstractState<I>, SS : S> StateMachineBuilder<I, S>.forState(
    clazz: KClass<SS>,
    onEntry: SS.() -> Collection<Effect> = { listOf() },
    onExit: SS.() -> Collection<Effect> = { listOf() },
    build: StateBuilder<I, S, SS>.() -> Unit
) {

}

fun <I : Input, S : AbstractState<I>, II : I, SS : S> StateBuilder<I, S, SS>.forInput(clazz: KClass<II>): SingleStateTransitionBuilder<I, S, II> {
    TODO()
}

interface SingleStateTransitionBuilder<I : Input, S : AbstractState<I>, II : I>

infix fun <I : Input, S : AbstractState<I>, II : I, SS : S> SingleStateTransitionBuilder<I, S, II>.doReturn(build: SingleStateTransitionBuilder<I, S, II>.(II, S) -> SS?) {

}

fun aa() {
    startFSMWith<SInput, SubscribeStates> { SubscribeStates.Unsubscribed() } andThen {
        forState(SubscribeStates.Unsubscribed::class) {
            forInput(SubscribeInput::class) doReturn { i, s ->
                SubscribeStates.Handshaking(s.subscribeStateBag + i)
            }
        }
        forState(
            SubscribeStates.Handshaking::class,
            onEntry = { listOf(call) },
            onExit = { listOf(EndHttpCallEffect(call.id)) }
        ) {
            forInput(SubscribeInput::class) doReturn { i, s ->
                s.subscribeStateBag.ifDifferent(s.subscribeStateBag + i) { newBag ->
                    SubscribeStates.Handshaking(newBag)
                }

            }
            forInput(HandshakeResult.HandshakeSuccess::class) doReturn { i, s ->
                SubscribeStates.Receiving(s.subscribeStateBag.copy(cursor = i.cursor))
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
