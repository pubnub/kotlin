package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.Effect
import org.slf4j.LoggerFactory

class LeaveEffect(
    private val leaveRemoteAction: RemoteAction<Boolean>
) : Effect {
    private val log = LoggerFactory.getLogger(LeaveEffect::class.java)

    override fun runEffect() {
        log.trace("Running LeaveEffect")
        leaveRemoteAction.async { _, status ->
            if (status.error) {
                throw PubNubException("LeaveEffect failed") // todo how to handle this?
            }
        }
    }
}
