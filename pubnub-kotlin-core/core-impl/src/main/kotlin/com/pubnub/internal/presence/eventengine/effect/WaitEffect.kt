package com.pubnub.internal.presence.eventengine.effect

import com.pubnub.internal.eventengine.ManagedEffect
import com.pubnub.internal.eventengine.Sink
import com.pubnub.internal.extension.scheduleWithDelay
import com.pubnub.internal.presence.eventengine.event.PresenceEvent
import org.slf4j.LoggerFactory
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import kotlin.time.Duration

internal class WaitEffect(
    private val heartbeatInterval: Duration,
    private val presenceEventSink: Sink<PresenceEvent>,
    private val executorService: ScheduledExecutorService
) : ManagedEffect {
    private val log = LoggerFactory.getLogger(WaitEffect::class.java)

    @Transient
    private var cancelled: Boolean = false

    @Transient
    private var scheduled: ScheduledFuture<*>? = null

    @Synchronized
    override fun runEffect() {
        log.trace("Running WaitEffect")
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
