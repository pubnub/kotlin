package com.pubnub.internal.presence.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.internal.eventengine.ManagedEffect
import com.pubnub.internal.eventengine.Sink
import com.pubnub.internal.extension.scheduleWithDelay
import com.pubnub.internal.presence.eventengine.event.PresenceEvent
import com.pubnub.internal.subscribe.eventengine.effect.RetryPolicy
import org.slf4j.LoggerFactory
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture

internal class DelayedHeartbeatEffect(
    val heartbeatRemoteAction: RemoteAction<Boolean>,
    val presenceEventSink: Sink<PresenceEvent>,
    val policy: RetryPolicy,
    val executorService: ScheduledExecutorService,
    val delayedHeartbeatInvocation: PresenceEffectInvocation.DelayedHeartbeat
) : ManagedEffect {
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

        val delay = policy.nextDelay(delayedHeartbeatInvocation.attempts)
        if (delay == null) {
            presenceEventSink.add(PresenceEvent.HeartbeatGiveup(delayedHeartbeatInvocation.reason ?: PubNubException("Unknown error")))
            return
        }

        scheduled = executorService.scheduleWithDelay(delay) {
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
