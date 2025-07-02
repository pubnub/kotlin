package com.pubnub.internal.subscribe.eventengine.effect

import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.internal.eventengine.Effect
import com.pubnub.internal.logging.LogConfig
import com.pubnub.api.logging.LogMessageType
import com.pubnub.internal.logging.LoggerManager

internal class EmitStatusEffect(
    private val statusConsumer: StatusConsumer,
    private val status: PNStatus,
    private val logConfig: LogConfig
) : Effect {
//    private val log = LoggerFactory.getLogger(EmitStatusEffect::class.java)
    private val log = LoggerManager.getLogger(logConfig, this::class.java)

    override fun runEffect() {
        //todo fix
        log.trace(LogMessageType.TEXT,"Running EmitStatusEffect: $status")
        statusConsumer.announce(status)
    }
}
