package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.*
import com.pubnub.api.state.Input
import com.pubnub.api.subscribe.*

class SubscribeMachine : AbstractMachine<SubscribeInput, AbstractSubscribeEffect>(SubscribeStates.Unsubscribed())

internal sealed class SubscribeStates(
    handleBlock: (input: Input) -> Pair<Collection<AbstractSubscribeEffect>, SubscribeStates?>
) : AbstractState<SubscribeInput, AbstractSubscribeEffect>(handleBlock) {

    abstract val stateBag: SubscribeStateBag
    open fun additionalOnEntryEffects(): Collection<AbstractSubscribeEffect> = listOf()
    override fun onEntry(): Collection<AbstractSubscribeEffect> {
        return listOf(NewStateEffect(this::class.simpleName!!)) + additionalOnEntryEffects()
    }

    data class Unsubscribed(override val stateBag: SubscribeStateBag = SubscribeStateBag()) : SubscribeStates({
        withNoEffects(
            when (it) {
                is SubscribeCommands.Subscribe -> {
                    Handshaking(stateBag + it)
                }
                else -> null
            }
        )
    })

    data class Handshaking(override val stateBag: SubscribeStateBag) : SubscribeStates({
        withNoEffects(
            when (it) {
                is SubscribeCommands.Subscribe -> {
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
        val call = SubscribeHttpEffect.HandshakeHttpCallEffect(subscribeStateBag = stateBag)

        override fun additionalOnEntryEffects(): Collection<AbstractSubscribeEffect> {
            return listOf(call)
        }

        override fun onExit(): Collection<AbstractSubscribeEffect> = listOf(EndHttpCallEffect(call.id))
    }

    data class Receiving(override val stateBag: SubscribeStateBag) : SubscribeStates({
        when (it) {
            is ReceivingResult.ReceivingSuccess -> {
                withEffects(Receiving(stateBag), NewMessagesEffect(it.subscribeEnvelope.messages))
            }
            else -> withNoEffects(when (it) {
                is SubscribeCommands.Subscribe -> {
                    stateBag.ifStatesAreDifferent(it) { newBag ->
                        Receiving(newBag)
                    }
                }
                else -> null
            })
        }
    }) {

        private val call = SubscribeHttpEffect.ReceiveMessagesHttpCallEffect(subscribeStateBag = stateBag)

        override fun additionalOnEntryEffects(): Collection<AbstractSubscribeEffect> = listOf(call)

        override fun onExit(): Collection<AbstractSubscribeEffect> = listOf(EndHttpCallEffect(call.id))
    }

}

internal fun withNoEffects(newState: SubscribeStates?): Pair<Collection<AbstractSubscribeEffect>, SubscribeStates?> {
    return Pair(listOf(), newState)
}

internal fun withEffects(
    newState: SubscribeStates?,
    vararg effects: AbstractSubscribeEffect
): Pair<Collection<AbstractSubscribeEffect>, SubscribeStates?> {
    return Pair(effects.toList(), newState)
}

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
    input: SubscribeCommands.Subscribe,
    newStateFactory: (SubscribeStateBag) -> SubscribeStates
): SubscribeStates? {
    val newStateBag = this + input

    return if (newStateBag != this) {
        newStateFactory(newStateBag)
    } else {
        null
    }
}

operator fun SubscribeStateBag.plus(input: SubscribeCommands.Subscribe): SubscribeStateBag {
    return copy(channels = this.channels + input.channels)
}

