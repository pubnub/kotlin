package com.pubnub.internal.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.internal.retry.RetryableBase
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.api.v2.callbacks.onFailure
import com.pubnub.api.v2.callbacks.onSuccess
import com.pubnub.internal.eventengine.ManagedEffect
import com.pubnub.internal.eventengine.Sink
import com.pubnub.internal.extension.scheduleWithDelay
import com.pubnub.internal.models.server.SubscribeEnvelope
import com.pubnub.internal.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.internal.subscribe.eventengine.event.SubscriptionCursor
import org.slf4j.LoggerFactory
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture

internal class HandshakeReconnectEffect(
    private val handshakeRemoteAction: RemoteAction<SubscriptionCursor>,
    private val subscribeEventSink: Sink<SubscribeEvent>,
    retryConfiguration: RetryConfiguration,
    private val executorService: ScheduledExecutorService,
    private val attempts: Int,
    private val reason: PubNubException?
) : ManagedEffect, RetryableBase<SubscribeEnvelope>(retryConfiguration, RetryableEndpointGroup.SUBSCRIBE) {
    private val log = LoggerFactory.getLogger(HandshakeReconnectEffect::class.java)

    @Transient
    private var scheduled: ScheduledFuture<*>? = null

    @Transient
    private var cancelled = false

    @Synchronized
    override fun runEffect() {
        log.trace("Running HandshakeReconnectEffect")

        if (cancelled) {
            return
        }

        if (!shouldRetry(attempts)) {
            subscribeEventSink.add(
                SubscribeEvent.HandshakeReconnectGiveup(
                    reason ?: PubNubException("Unknown error")
                )
            )
            return
        }

        val effectiveDelay = getEffectiveDelay(statusCode = reason?.statusCode ?: 0, retryAfterHeaderValue = reason?.retryAfterHeaderValue ?: 0)
        scheduled = executorService.scheduleWithDelay(effectiveDelay) {
            handshakeRemoteAction.async { result ->
                result.onFailure {
                    subscribeEventSink.add(
                        SubscribeEvent.HandshakeReconnectFailure(
                            PubNubException.from(it)
                        )
                    )
                }.onSuccess { cursor ->
                    subscribeEventSink.add(SubscribeEvent.HandshakeReconnectSuccess(cursor))
                }
            }
        }
    }

    @Synchronized
    override fun cancel() {
        if (cancelled) {
            return
        }
        cancelled = true
        handshakeRemoteAction.silentCancel()
        scheduled?.cancel(true)
    }
}
