package com.pubnub.internal.subscribe.eventengine.effect

import com.pubnub.api.logging.LogConfig
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.internal.eventengine.Effect
import com.pubnub.internal.logging.LoggerManager
import org.slf4j.event.Level

internal class EmitStatusEffect(
    private val statusConsumer: StatusConsumer,
    private val status: PNStatus,
    private val logConfig: LogConfig
) : Effect {
    private val log = LoggerManager.instance.getLogger(logConfig, this::class.java)

    override fun runEffect() {
        log.trace(
            LogMessage(
                pubNubId = logConfig.pnInstanceId,
                logLevel = Level.TRACE,
                location = this::class.java.simpleName,
                type = LogMessageType.TEXT,
                message = LogMessageContent.Text("Running EmitStatusEffect: $status"),
            )
        )
        statusConsumer.announce(status)
    }
}
