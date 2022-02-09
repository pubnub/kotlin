package com.pubnub.api.subscribe.internal

import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.state.AbstractState
import com.pubnub.api.state.Effect
import com.pubnub.api.state.Input
import java.util.*

interface SInput : Input

interface SEffect : Effect

data class SubscribeInput(val channels: List<String>) : SInput

sealed class HandshakeResult : SInput {
    data class HandshakeSuccess(val cursor: Cursor) : HandshakeResult()
    object HandshakeFail : HandshakeResult()
}

sealed class ReceivingResult : SInput {
    data class ReceivingSuccess(val subscribeEnvelope: SubscribeEnvelope) : ReceivingResult()
    object ReceivingFail : ReceivingResult()
}

class SubscribeModule {
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

    abstract val subscribeStateBag: SubscribeStateBag

    data class Unsubscribed(override val subscribeStateBag: SubscribeStateBag = SubscribeStateBag()) : SubscribeStates({
        withNoEffects(
            when (it) {
                is SubscribeInput -> {
                    Handshaking(subscribeStateBag + it)
                }
                else -> null
            }
        )
    })

    data class Handshaking(override val subscribeStateBag: SubscribeStateBag) : SubscribeStates({
        withNoEffects(
            when (it) {
                is SubscribeInput -> {
                    subscribeStateBag.changeToIfDifferentState(it) { newBag ->
                        Handshaking(newBag)
                    }
                }

                is HandshakeResult.HandshakeSuccess -> {
                    Receiving(subscribeStateBag.copy(cursor = it.cursor))
                }
                else -> null
            }
        )
    }) {
        private val call = HandshakeHttpCallEffect(subscribeStateBag = subscribeStateBag)

        override fun onEntry(): Collection<SEffect> = listOf(call)

        override fun onExit(): Collection<SEffect> = listOf(EndHttpCallEffect(call.id))
    }

    data class Receiving(override val subscribeStateBag: SubscribeStateBag) : SubscribeStates({
        when (it) {
            is ReceivingResult.ReceivingSuccess -> {
                withEffects(Receiving(subscribeStateBag), NewMessagesEffect(it.subscribeEnvelope))
            }
            else -> withNoEffects(when (it) {
                is SubscribeInput -> {
                    subscribeStateBag.changeToIfDifferentState(it) { newBag ->
                        Receiving(newBag)
                    }
                }
                else -> null
            })
        }
    }) {

        private val call = ReceiveMessagesHttpCallEffect(subscribeStateBag = subscribeStateBag)

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

interface HttpCallEffect : SEffect {
    val id: String
}

data class ReceiveMessagesHttpCallEffect(
    val subscribeStateBag: SubscribeStateBag,
    override val id: String = UUID.randomUUID().toString()
) : HttpCallEffect


data class HandshakeHttpCallEffect(
    val subscribeStateBag: SubscribeStateBag,
    override val id: String = UUID.randomUUID().toString()
) : HttpCallEffect

data class EndHttpCallEffect(
    val id: String
) : SEffect

data class NewMessagesEffect(val subscribeEnvelope: SubscribeEnvelope) : SEffect

data class SubscribeStateBag(
    val channels: List<String> = listOf(),
    val groups: List<String> = listOf(),
    val cursor: Cursor? = null
)

data class Cursor(
    val timetoken: Long,
    val region: String
)

internal fun SubscribeStateBag.changeToIfDifferentState(
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

