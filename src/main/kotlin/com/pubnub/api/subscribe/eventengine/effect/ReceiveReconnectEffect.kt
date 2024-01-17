package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.retry.RetryableBase
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.extension.scheduleWithDelay
import org.slf4j.LoggerFactory
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture

internal class ReceiveReconnectEffect(
    private val receiveMessagesRemoteAction: RemoteAction<ReceiveMessagesResult>,
    private val subscribeEventSink: Sink<SubscribeEvent>,
    retryConfiguration: RetryConfiguration,
    private val executorService: ScheduledExecutorService,
    private val attempts: Int,
    private val reason: PubNubException?
) : ManagedEffect, RetryableBase<SubscribeEnvelope>(retryConfiguration, RetryableEndpointGroup.SUBSCRIBE) {
    private val log = LoggerFactory.getLogger(ReceiveReconnectEffect::class.java)

    @Transient
    private var scheduled: ScheduledFuture<*>? = null

    @Transient
    private var cancelled = false

    @Synchronized
    override fun runEffect() {
        log.trace("Running ReceiveReconnectEffect")

        if (cancelled) {
            return
        }

        if (!shouldRetry(attempts)) {
            subscribeEventSink.add(SubscribeEvent.ReceiveReconnectGiveup(reason ?: PubNubException("Unknown error")))
            return
        }

        val effectiveDelay = getEffectiveDelay(statusCode = reason?.statusCode ?: 0, retryAfterHeaderValue = reason?.retryAfterHeaderValue ?: 0)
        scheduled = executorService.scheduleWithDelay(effectiveDelay) {
            receiveMessagesRemoteAction.async { result, status ->
                if (status.error) {
                    subscribeEventSink.add(
                        SubscribeEvent.ReceiveReconnectFailure(
                            status.exception ?: PubNubException("Unknown error")
                        )
                    )
                } else {
                    subscribeEventSink.add(
                        SubscribeEvent.ReceiveReconnectSuccess(
                            result!!.messages,
                            result.subscriptionCursor
                        )
                    )
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

        receiveMessagesRemoteAction.silentCancel()
        scheduled?.cancel(true)
    }
}
