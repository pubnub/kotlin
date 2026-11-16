package com.pubnub.internal.subscribe.eventengine.effect

import com.pubnub.api.logging.LogConfig
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.internal.eventengine.Effect
import com.pubnub.internal.logging.LoggerManager

internal class ResetSubscribeConnectionEffect(
    private val resetSubscribeConnection: () -> Unit,
    private val logConfig: LogConfig
) : Effect {
    private val log = LoggerManager.instance.getLogger(logConfig, this::class.java)

    override fun runEffect() {
        log.trace(LogMessage(message = LogMessageContent.Text("Running ResetSubscribeConnectionEffect")))
        resetSubscribeConnection()
    }
}
