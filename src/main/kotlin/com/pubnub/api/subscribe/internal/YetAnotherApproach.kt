package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.Effect

enum class States {
    Unsubsrcibed,
    Handshaking,
    Receiving
}

enum class Input {
    Subscribe,
    Unsubscribe
}

fun <I : Input> StateDescriptionBldr.then(
    on: I,
    transitionTo: (I) -> Pair<States, Collection<Effect>>
): StateDescriptionBldr {
    TODO()
}

data class TransitionsDescription<I : Input>(
    val on: I,
    val transitionTo: (I) -> Pair<States, Collection<Effect>>
)

interface StateDescriptionBldr

fun state(
    onEntry: () -> Collection<Effect> = { listOf() },
    onExit: () -> Collection<Effect> = { listOf() },
    transitions: Collection<TransitionsDescription<Input>>
): StateDescriptionBldr {
    TODO()
}

data class StateDescription(
    val state: States,
    val onEntry: () -> Collection<Effect>,
    val onExit: () -> Collection<Effect>,
    val transitions: Collection<TransitionsDescription<Input>>
)
