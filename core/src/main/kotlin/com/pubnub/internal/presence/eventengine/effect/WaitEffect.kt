package com.pubnub.internal.presence.eventengine.effect

import com.pubnub.internal.eventengine.ManagedEffect
import com.pubnub.internal.eventengine.Sink
import com.pubnub.internal.extension.scheduleWithDelay
import com.pubnub.internal.presence.eventengine.event.PresenceEvent
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import kotlin.time.Duration

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
