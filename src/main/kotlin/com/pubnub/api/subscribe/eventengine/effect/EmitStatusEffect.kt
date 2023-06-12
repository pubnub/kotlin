package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.eventengine.Effect
import com.pubnub.api.models.consumer.PNStatus

class EmitStatusEffect(
    private val statusConsumer: StatusConsumer,
    private val status: PNStatus
) : Effect {
    override fun runEffect() {
        statusConsumer.announce(status)
    }
}
