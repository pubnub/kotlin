package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.eventengine.Effect
import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.models.consumer.PNStatus
import org.slf4j.LoggerFactory

class EmitStatusEffect(
    private val statusConsumer: StatusConsumer,
    private val status: PNStatus
) : Effect {
    private val log = LoggerFactory.getLogger(EffectDispatcher::class.java)

    override fun runEffect() {
        log.trace("Running EmitStatusEffect thread: ${Thread.currentThread().id}")
        statusConsumer.announce(status)
    }
}
