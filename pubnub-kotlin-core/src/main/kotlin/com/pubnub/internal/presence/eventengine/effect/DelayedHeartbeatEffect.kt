package com.pubnub.internal.presence.eventengine.effect

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
import com.pubnub.internal.presence.eventengine.event.PresenceEvent
import com.pubnub.internal.retry.RetryableBase
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

        val effectiveDelay = getEffectiveDelay(
            statusCode = reason?.statusCode ?: 0,
            retryAfterHeaderValue = reason?.retryAfterHeaderValue ?: 0
        )
        scheduled = executorService.scheduleWithDelay(effectiveDelay) {
            heartbeatRemoteAction.async { result ->
                result.onFailure {
                    presenceEventSink.add(PresenceEvent.HeartbeatFailure(PubNubException.from(it)))
                }.onSuccess {
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
