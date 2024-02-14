package com.pubnub.internal.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.api.v2.callbacks.onFailure
import com.pubnub.api.v2.callbacks.onSuccess
import com.pubnub.internal.eventengine.ManagedEffect
import com.pubnub.internal.eventengine.Sink
import com.pubnub.internal.extension.scheduleWithDelay
import com.pubnub.internal.models.server.SubscribeEnvelope
import com.pubnub.internal.retry.RetryableBase
import com.pubnub.internal.subscribe.eventengine.event.SubscribeEvent
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
            receiveMessagesRemoteAction.async { result ->
                result.onFailure {
                    subscribeEventSink.add(
                        SubscribeEvent.ReceiveReconnectFailure(
                            PubNubException.from(it)
                        )
                    )
                }.onSuccess {
                    subscribeEventSink.add(
                        SubscribeEvent.ReceiveReconnectSuccess(
                            it.messages,
                            it.subscriptionCursor
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
