package com.pubnub.internal.presence.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.logging.LogConfig
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.internal.eventengine.Effect
import com.pubnub.internal.eventengine.Sink
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.presence.eventengine.event.PresenceEvent
import com.pubnub.internal.subscribe.eventengine.effect.StatusConsumer

internal class HeartbeatEffect(
    val heartbeatRemoteAction: RemoteAction<Boolean>,
    val presenceEventSink: Sink<PresenceEvent>,
    val heartbeatNotificationOptions: PNHeartbeatNotificationOptions,
    val statusConsumer: StatusConsumer,
    private val logConfig: LogConfig,
) : Effect {
    private val log = LoggerManager.instance.getLogger(logConfig, this::class.java)

    override fun runEffect() {
        log.trace(
            LogMessage(
                location = this::class.java.simpleName,
                message = LogMessageContent.Text("Running HeartbeatEffect"),
            )
        )
        heartbeatRemoteAction.async { result ->
            result.onFailure { exception ->
                if (heartbeatNotificationOptions == PNHeartbeatNotificationOptions.ALL ||
                    heartbeatNotificationOptions == PNHeartbeatNotificationOptions.FAILURES
                ) {
                    statusConsumer.announce(PNStatus(PNStatusCategory.PNHeartbeatFailed, PubNubException.from(exception)))
                }
                presenceEventSink.add(
                    PresenceEvent.HeartbeatFailure(PubNubException.from(exception)),
                )
            }.onSuccess {
                if (heartbeatNotificationOptions == PNHeartbeatNotificationOptions.ALL) {
                    statusConsumer.announce(PNStatus(PNStatusCategory.PNHeartbeatSuccess))
                }
                presenceEventSink.add(
                    PresenceEvent.HeartbeatSuccess,
                )
            }
        }
    }
}
