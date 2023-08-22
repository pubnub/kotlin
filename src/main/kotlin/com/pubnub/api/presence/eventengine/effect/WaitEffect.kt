package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.extension.scheduleWithDelay
import java.time.Duration
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture

class WaitEffect( // todo handle pubnub.configuration.heartbeatInterval <= 0
    private val heartbeatInterval: Duration, // todo if the interval is 0 or less, do not start the timer
    private val presenceEventSink: Sink<PresenceEvent>, // todo this should  be check at PresenceEventEngineCreation
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
