package com.pubnub.internal.subscribe.eventengine.effect

import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.internal.eventengine.Effect
import org.slf4j.LoggerFactory

internal class EmitStatusEffect(
    private val statusConsumer: StatusConsumer,
    private val status: PNStatus
) : Effect {
    private val log = LoggerFactory.getLogger(EmitStatusEffect::class.java)

    override fun runEffect() {
        log.trace("Running EmitStatusEffect: $status")
        statusConsumer.announce(status)
    }
}
