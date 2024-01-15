package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.eventengine.Effect
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.subscribe.eventengine.effect.StatusConsumer
import org.slf4j.LoggerFactory

internal class HeartbeatEffect(
    val heartbeatRemoteAction: RemoteAction<Boolean>,
    val presenceEventSink: Sink<PresenceEvent>,
    val heartbeatNotificationOptions: PNHeartbeatNotificationOptions,
    val statusConsumer: StatusConsumer
) : Effect {
    private val log = LoggerFactory.getLogger(HeartbeatEffect::class.java)

    override fun runEffect() {
        log.trace("Running HeartbeatEffect")
        heartbeatRemoteAction.async { _, status ->
            if (status.error) {
                if (heartbeatNotificationOptions == PNHeartbeatNotificationOptions.ALL ||
                    heartbeatNotificationOptions == PNHeartbeatNotificationOptions.FAILURES
                ) {
                    statusConsumer.announce(status)
                }
                presenceEventSink.add(
                    PresenceEvent.HeartbeatFailure(status.exception ?: PubNubException("Unknown error"))
                )
            } else {
                if (heartbeatNotificationOptions == PNHeartbeatNotificationOptions.ALL) {
                    statusConsumer.announce(status)
                }
                presenceEventSink.add(
                    PresenceEvent.HeartbeatSuccess
                )
            }
        }
    }
}
