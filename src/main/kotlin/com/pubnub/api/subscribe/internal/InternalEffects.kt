package com.pubnub.api.subscribe.internal

import com.pubnub.api.subscribe.AbstractSubscribeEffect

sealed class SubscribeHttpEffect : AbstractSubscribeEffect() {
    data class ReceiveMessagesHttpCallEffect(
        val subscribeStateBag: SubscribeStateBag
    ) : SubscribeHttpEffect()

    data class HandshakeHttpCallEffect(
        val subscribeStateBag: SubscribeStateBag
    ) : SubscribeHttpEffect()

}

class EndHttpCallEffect(val idToCancel: String) : AbstractSubscribeEffect()

