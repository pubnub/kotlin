package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import org.slf4j.LoggerFactory

internal class HandshakeEffect(
    private val handshakeRemoteAction: RemoteAction<SubscriptionCursor>,
    private val subscribeEventSink: Sink<SubscribeEvent>,
) : ManagedEffect {
    private val log = LoggerFactory.getLogger(HandshakeEffect::class.java)

    override fun runEffect() {
        log.trace("Running HandshakeEffect")

        handshakeRemoteAction.async { result, status ->
            if (status.error) {
                subscribeEventSink.add(
                    SubscribeEvent.HandshakeFailure(
                        status.exception
                            ?: PubNubException("Unknown error")
                    )
                )
            } else {
                subscribeEventSink.add(SubscribeEvent.HandshakeSuccess(result!!))
            }
        }
    }

    override fun cancel() {
        handshakeRemoteAction.silentCancel()
    }
}
