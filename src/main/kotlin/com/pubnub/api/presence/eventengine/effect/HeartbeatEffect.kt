package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.Effect
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import org.slf4j.LoggerFactory

class HeartbeatEffect(
    private val heartbeatRemoteAction: RemoteAction<Boolean>,
    private val presenceEventSink: Sink<PresenceEvent>
) : Effect{
    private val log = LoggerFactory.getLogger(HeartbeatEffect::class.java)

    override fun runEffect() {
        log.trace("Running HeartbeatEffect")
        heartbeatRemoteAction.async { _, status ->
            if(status.error) {
                presenceEventSink.add(
                    PresenceEvent.HeartbeatFailure( status.exception ?: PubNubException("Unknown error"))
                )
            } else{
                presenceEventSink.add(
                    PresenceEvent.HeartbeatSuccess
                )
            }
        }
    }
}
