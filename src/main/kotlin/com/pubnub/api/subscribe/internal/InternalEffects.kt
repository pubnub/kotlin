package com.pubnub.api.subscribe.internal

import com.pubnub.api.subscribe.AbstractSubscribeEffect

sealed class SubscribeHttpEffect : AbstractSubscribeEffect() {
    data class ReceiveMessagesHttpCallEffect(
        val subscriptionStatus: SubscriptionStatus
    ) : SubscribeHttpEffect()

    data class HandshakeHttpCallEffect(
        val subscriptionStatus: SubscriptionStatus
    ) : SubscribeHttpEffect()
}

class EndHttpCallEffect(val idToCancel: String) : AbstractSubscribeEffect()
