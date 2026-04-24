package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.extension.scheduleWithDelay
import java.time.Duration
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture

internal class WaitEffect(
    private val heartbeatInterval: Duration,
    private val presenceEventSink: Sink<PresenceEvent>,
    private val executorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
) : ManagedEffect {

    @Transient
    private var cancelled: Boolean = false

    @Transient
    private var scheduled: ScheduledFuture<*>? = null

    @Synchronized
    override fun runEffect() {
        if (cancelled) {
            return
        }

        scheduled = executorService.scheduleWithDelay(heartbeatInterval) {
            presenceEventSink.add(PresenceEvent.TimesUp)
        }
    }

    @Synchronized
    override fun cancel() {
        if (cancelled) {
            return
        }
        scheduled?.cancel(true)
        cancelled = true
    }
}
