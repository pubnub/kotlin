package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.eventengine.Effect
import com.pubnub.core.Status
import org.slf4j.LoggerFactory

class EmitStatusEffect<in S : Status>(
    private val statusConsumer: StatusConsumer<S>,
    private val status: S
) : Effect {
    private val log = LoggerFactory.getLogger(EmitStatusEffect::class.java)

    override fun runEffect() {
        log.trace("Running EmitStatusEffect")
        statusConsumer.announce(status)
    }
}
