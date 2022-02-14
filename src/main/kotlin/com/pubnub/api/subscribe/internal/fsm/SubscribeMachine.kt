package com.pubnub.api.subscribe.internal.fsm

import com.pubnub.api.state.*
import com.pubnub.api.state.Event
import com.pubnub.api.subscribe.*
import com.pubnub.api.subscribe.internal.EndHttpCallEffect
import com.pubnub.api.subscribe.internal.SubscribeHttpEffect
import com.pubnub.api.subscribe.internal.SubscriptionStatus

class SubscribeMachine : AbstractMachine<SubscribeEvent, AbstractSubscribeEffect>(SubscribeStates.Unsubscribed())

internal sealed class SubscribeStates(
    handleBlock: (event: Event) -> Pair<Collection<AbstractSubscribeEffect>, SubscribeStates?>
) : AbstractState<SubscribeEvent, AbstractSubscribeEffect>(handleBlock) {

    abstract val subscriptionStatus: SubscriptionStatus
    open fun additionalOnEntryEffects(): Collection<AbstractSubscribeEffect> = listOf()
    override fun onEntry(): Collection<AbstractSubscribeEffect> {
        return listOf(
            NewStateEffect(
                name = this::class.simpleName!!,
                status = subscriptionStatus
            )
        ) + additionalOnEntryEffects()
    }


    data class Unsubscribed(override val subscriptionStatus: SubscriptionStatus = SubscriptionStatus()) :
        SubscribeStates({
            withNoEffects(
                when (it) {
                    is Commands.SubscribeCommandIssued -> {
                        Handshaking(subscriptionStatus + it)
                    }
                    else -> null
                }
            )
        })

    data class Handshaking(override val subscriptionStatus: SubscriptionStatus) : SubscribeStates({
        withNoEffects(
            when (it) {
                is Commands.SubscribeCommandIssued -> {
                    subscriptionStatus.ifStatesAreDifferent(it) { newBag ->
                        Handshaking(newBag)
                    }
                }

                is HandshakeResult.HandshakeSucceeded -> {
                    Receiving(subscriptionStatus.copy(cursor = it.cursor))
                }
                else -> null
            }
        )
    }) {
        val call = SubscribeHttpEffect.HandshakeHttpCallEffect(subscriptionStatus = subscriptionStatus)

        override fun additionalOnEntryEffects(): Collection<AbstractSubscribeEffect> {
            return listOf(call)
        }

        override fun onExit(): Collection<AbstractSubscribeEffect> = listOf(EndHttpCallEffect(call.id))
    }

    data class Receiving(override val subscriptionStatus: SubscriptionStatus) : SubscribeStates({
        when (it) {
            is ReceivingResult.ReceivingSucceeded -> {
                withEffects(Receiving(subscriptionStatus), NewMessagesEffect(it.subscribeEnvelope.messages))
            }
            else -> withNoEffects(when (it) {
                is Commands.SubscribeCommandIssued -> {
                    subscriptionStatus.ifStatesAreDifferent(it) { newBag ->
                        Receiving(newBag)
                    }
                }
                else -> null
            })
        }
    }) {

        private val call = SubscribeHttpEffect.ReceiveMessagesHttpCallEffect(subscriptionStatus = subscriptionStatus)

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

internal fun SubscriptionStatus.ifStatesAreDifferent(
    input: Commands.SubscribeCommandIssued,
    newStateFactory: (SubscriptionStatus) -> SubscribeStates
): SubscribeStates? {
    val newStateBag = this + input

    return if (newStateBag != this) {
        newStateFactory(newStateBag)
    } else {
        null
    }
}

operator fun SubscriptionStatus.plus(input: Commands.SubscribeCommandIssued): SubscriptionStatus {
    return copy(channels = this.channels + input.channels)
}
