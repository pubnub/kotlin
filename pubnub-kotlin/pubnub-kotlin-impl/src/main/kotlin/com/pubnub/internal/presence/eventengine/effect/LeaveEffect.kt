package com.pubnub.internal.presence.eventengine.effect

import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.logging.LogConfig
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.internal.eventengine.Effect
import com.pubnub.internal.logging.LoggerManager

internal class LeaveEffect(
    val leaveRemoteAction: RemoteAction<Boolean>,
    private val logConfig: LogConfig,
) : Effect {
    private val log = LoggerManager.instance.getLogger(logConfig, this::class.java)

    override fun runEffect() {
        log.trace(LogMessage(message = LogMessageContent.Text("Running LeaveEffect.")))
        leaveRemoteAction.async { result ->
            result.onFailure {
            }
        }
    }
}
