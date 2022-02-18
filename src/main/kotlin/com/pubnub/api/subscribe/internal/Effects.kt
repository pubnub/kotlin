package com.pubnub.api.subscribe.internal

import com.pubnub.api.models.server.SubscribeMessage
import com.pubnub.api.state.Effect

sealed class SubscribeEffect(override val child: SubscribeEffect? = null) : Effect()

data class NewMessages(val messages: List<SubscribeMessage>) : SubscribeEffect()

data class NewState(val name: String, val status: SubscriptionStatus) : SubscribeEffect()

sealed class SubscribeHttpEffect : SubscribeEffect() {
    data class ReceiveMessagesHttpCallEffect(
        val subscriptionStatus: SubscriptionStatus
    ) : SubscribeHttpEffect()

    data class HandshakeHttpCallEffect(
        val subscriptionStatus: SubscriptionStatus
    ) : SubscribeHttpEffect()
}

data class CancelEffect(val idToCancel: String) : SubscribeEffect()

data class ScheduleRetry(val retryableEffect: SubscribeHttpEffect, val retryCount: Int) : SubscribeEffect(retryableEffect)
