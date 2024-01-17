package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.retry.RetryableBase
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.extension.scheduleWithDelay
import org.slf4j.LoggerFactory
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture

internal class DelayedHeartbeatEffect(
    val heartbeatRemoteAction: RemoteAction<Boolean>,
    val presenceEventSink: Sink<PresenceEvent>,
    val retryConfiguration: RetryConfiguration,
    val executorService: ScheduledExecutorService,
    val attempts: Int,
    val reason: PubNubException?
) : ManagedEffect, RetryableBase<SubscribeEnvelope>(retryConfiguration, RetryableEndpointGroup.PRESENCE) {
    private val log = LoggerFactory.getLogger(DelayedHeartbeatEffect::class.java)

    @Transient
    private var scheduled: ScheduledFuture<*>? = null

    @Transient
    private var cancelled = false

    @Synchronized
    override fun runEffect() {
        log.trace("Running DelayedHeartbeatEffect")
        if (cancelled) {
            return
        }

        if (!shouldRetry(attempts)) {
            presenceEventSink.add(PresenceEvent.HeartbeatGiveup(reason ?: PubNubException("Unknown error")))
            return
        }

        val effectiveDelay = getEffectiveDelay(statusCode = reason?.statusCode ?: 0, retryAfterHeaderValue = reason?.retryAfterHeaderValue ?: 0)
        scheduled = executorService.scheduleWithDelay(effectiveDelay) {
            heartbeatRemoteAction.async { _, status ->
                if (status.error) {
                    presenceEventSink.add(PresenceEvent.HeartbeatFailure(status.exception ?: PubNubException("Unknown error")))
                } else {
                    presenceEventSink.add(PresenceEvent.HeartbeatSuccess)
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
        heartbeatRemoteAction.silentCancel()
        scheduled?.cancel(true)
    }
}
