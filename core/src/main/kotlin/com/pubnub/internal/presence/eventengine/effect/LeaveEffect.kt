package com.pubnub.internal.presence.eventengine.effect

import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.internal.eventengine.Effect
import org.slf4j.LoggerFactory

internal class LeaveEffect(
    val leaveRemoteAction: RemoteAction<Boolean>,
) : Effect {
    private val log = LoggerFactory.getLogger(LeaveEffect::class.java)

    override fun runEffect() {
        log.trace("Running LeaveEffect")
        leaveRemoteAction.async { _, status ->
            if (status.error) {
                log.error("LeaveEffect failed", status.exception)
            }
        }
    }
}
