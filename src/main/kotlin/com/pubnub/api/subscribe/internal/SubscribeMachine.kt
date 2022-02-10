package com.pubnub.api.subscribe.internal

import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.state.AbstractState
import com.pubnub.api.state.Effect
import com.pubnub.api.state.Input
import com.pubnub.api.subscribe.SInput
import java.util.*

abstract class SEffect : Effect()

sealed class HandshakeResult : SInput {
    data class HandshakeSuccess(val cursor: Cursor) : HandshakeResult()
    object HandshakeFail : HandshakeResult()
}

sealed class ReceivingResult : SInput {
    data class ReceivingSuccess(val subscribeEnvelope: SubscribeEnvelope) : ReceivingResult()
    object ReceivingFail : ReceivingResult()
}

fun foldIt(inputs: Collection<SInput>) {
    val currentState: AbstractState<SInput> = SubscribeStates.Unsubscribed()

    inputs.fold<SInput, Pair<AbstractState<SInput>, Collection<Effect>>>(currentState to listOf()) { (s, efs), i ->
        val (nEfs, ns) = s.transition(i)
        ns to (efs + nEfs)
    }
}

class SubscribeMachine {
    private var currentState: AbstractState<SInput> = SubscribeStates.Unsubscribed()

    fun handle(input: SInput): Collection<Effect> {
        val (events, newState) = currentState.transition(input)
        currentState = newState
        return events
    }
}

internal sealed class SubscribeStates(
    handleBlock: (input: Input) -> Pair<Collection<Effect>, SubscribeStates?>
) : AbstractState<SInput>(handleBlock) {

    abstract val stateBag: SubscribeStateBag

    data class Unsubscribed(override val stateBag: SubscribeStateBag = SubscribeStateBag()) : SubscribeStates({
        withNoEffects(
            when (it) {
                is SubscribeInput -> {
                    Handshaking(stateBag + it)
                }
                else -> null
            }
        )
    })

    data class Handshaking(override val stateBag: SubscribeStateBag) : SubscribeStates({
        withNoEffects(
            when (it) {
                is SubscribeInput -> {
                    stateBag.ifStatesAreDifferent(it) { newBag ->
                        Handshaking(newBag)
                    }
                }

                is HandshakeResult.HandshakeSuccess -> {
                    Receiving(stateBag.copy(cursor = it.cursor))
                }
                else -> null
            }
        )
    }) {
        val call = HandshakeHttpCallEffect(subscribeStateBag = stateBag)

        override fun onEntry(): Collection<SEffect> = listOf(call)

        override fun onExit(): Collection<SEffect> = listOf(EndHttpCallEffect(call.id))
    }

    data class Receiving(override val stateBag: SubscribeStateBag) : SubscribeStates({
        when (it) {
            is ReceivingResult.ReceivingSuccess -> {
                withEffects(Receiving(stateBag), NewMessagesEffect(it.subscribeEnvelope))
            }
            else -> withNoEffects(when (it) {
                is SubscribeInput -> {
                    stateBag.ifStatesAreDifferent(it) { newBag ->
                        Receiving(newBag)
                    }
                }
                else -> null
            })
        }
    }) {

        private val call = ReceiveMessagesHttpCallEffect(subscribeStateBag = stateBag)

        override fun onEntry(): Collection<SEffect> = listOf(call)

        override fun onExit(): Collection<SEffect> = listOf(EndHttpCallEffect(call.id))
    }

}

internal fun withNoEffects(newState: SubscribeStates?): Pair<Collection<Effect>, SubscribeStates?> {
    return Pair(listOf(), newState)
}

internal fun withEffects(
    newState: SubscribeStates?,
    vararg effects: Effect
): Pair<Collection<Effect>, SubscribeStates?> {
    return Pair(effects.toList(), newState)
}

abstract class HttpCallEffect : SEffect()

data class ReceiveMessagesHttpCallEffect(
    val subscribeStateBag: SubscribeStateBag
) : HttpCallEffect()


data class HandshakeHttpCallEffect(
    val subscribeStateBag: SubscribeStateBag
) : HttpCallEffect()

class EndHttpCallEffect(val idToCancel: String) : SEffect()

data class NewMessagesEffect(val subscribeEnvelope: SubscribeEnvelope) : SEffect()

data class SubscribeStateBag(
    val channels: List<String> = listOf(),
    val groups: List<String> = listOf(),
    val cursor: Cursor? = null
)

data class Cursor(
    val timetoken: Long,
    val region: String
)

internal fun SubscribeStateBag.ifStatesAreDifferent(
    input: SubscribeInput,
    newStateFactory: (SubscribeStateBag) -> SubscribeStates
): SubscribeStates? {
    val newStateBag = this + input

    return if (newStateBag != this) {
        newStateFactory(newStateBag)
    } else {
        null
    }
}

operator fun SubscribeStateBag.plus(input: SubscribeInput): SubscribeStateBag {
    return copy(channels = this.channels + input.channels)
}

