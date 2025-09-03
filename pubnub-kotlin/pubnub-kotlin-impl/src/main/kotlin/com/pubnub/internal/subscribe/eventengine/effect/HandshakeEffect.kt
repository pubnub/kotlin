package com.pubnub.internal.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.logging.LogConfig
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.internal.eventengine.ManagedEffect
import com.pubnub.internal.eventengine.Sink
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.internal.subscribe.eventengine.event.SubscriptionCursor

internal class HandshakeEffect(
    private val handshakeRemoteAction: RemoteAction<SubscriptionCursor>,
    private val subscribeEventSink: Sink<SubscribeEvent>,
    private val logConfig: LogConfig,
) : ManagedEffect {
    private val log = LoggerManager.instance.getLogger(logConfig, this::class.java)

    override fun runEffect() {
        log.trace(
            LogMessage(
                location = this::class.java.simpleName,
                type = LogMessageType.TEXT,
                message = LogMessageContent.Text("Running HandshakeEffect")
            )
        )

        handshakeRemoteAction.async { result ->
            result.onFailure {
                subscribeEventSink.add(
                    SubscribeEvent.HandshakeFailure(
                        PubNubException.from(it),
                    ),
                )
            }.onSuccess { cursor ->
                subscribeEventSink.add(SubscribeEvent.HandshakeSuccess(cursor))
            }
        }
    }

    override fun cancel() {
        handshakeRemoteAction.silentCancel()
    }
}
