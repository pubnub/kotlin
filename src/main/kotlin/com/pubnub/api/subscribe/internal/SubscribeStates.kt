package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.State
import com.pubnub.api.subscribe.AbstractSubscribeEffect

sealed class SubscribeState : State<AbstractSubscribeEffect> {
    abstract val status: SubscriptionStatus
    override fun onEntry(): Collection<AbstractSubscribeEffect> = listOf()
    override fun onExit(): Collection<AbstractSubscribeEffect> = listOf()
}

object Unsubscribed : SubscribeState() {
    override val status: SubscriptionStatus = SubscriptionStatus()
}

data class Receiving(
    override val status: SubscriptionStatus,
    val call: SubscribeHttpEffect.ReceiveMessagesHttpCallEffect = SubscribeHttpEffect.ReceiveMessagesHttpCallEffect(
        status
    ),
) : SubscribeState() {
    override fun onEntry(): Collection<AbstractSubscribeEffect> = listOf(call)
}

data class Handshaking(
    override val status: SubscriptionStatus,
    val retryCount: Int = 0
) : SubscribeState() {
    val call: ScheduleRetry = ScheduleRetry(
        retryableEffect = SubscribeHttpEffect.HandshakeHttpCallEffect(
            status
        ), retryCount = retryCount
    )

    override fun onEntry(): Collection<AbstractSubscribeEffect> = listOf(call)
}

data class Reconnecting(
    override val status: SubscriptionStatus,
    val retryCount: Int = 1
) : SubscribeState() {
    val call: ScheduleRetry = ScheduleRetry(
        retryableEffect = SubscribeHttpEffect.ReceiveMessagesHttpCallEffect(
            status
        ), retryCount = retryCount
    )

    override fun onEntry(): Collection<AbstractSubscribeEffect> = listOf(call)
}

data class ReconnectingFailed(
    override val status: SubscriptionStatus,
) : SubscribeState()


data class HandshakingFailed(
    override val status: SubscriptionStatus,
) : SubscribeState()
