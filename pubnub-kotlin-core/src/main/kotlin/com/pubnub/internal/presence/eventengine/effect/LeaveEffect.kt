package com.pubnub.internal.presence.eventengine.effect

import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.v2.callbacks.onFailure
import com.pubnub.internal.eventengine.Effect
import org.slf4j.LoggerFactory

internal class LeaveEffect(
    val leaveRemoteAction: RemoteAction<Boolean>,
) : Effect {
    private val log = LoggerFactory.getLogger(LeaveEffect::class.java)

    override fun runEffect() {
        log.trace("Running LeaveEffect")
        leaveRemoteAction.async { result ->
            result.onFailure {
                log.error("LeaveEffect failed", it)
            }
        }
    }
}
