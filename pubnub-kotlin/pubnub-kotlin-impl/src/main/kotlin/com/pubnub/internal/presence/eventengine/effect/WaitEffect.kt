package com.pubnub.internal.presence.eventengine.effect

import com.pubnub.api.logging.LogConfig
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.internal.eventengine.ManagedEffect
import com.pubnub.internal.eventengine.Sink
import com.pubnub.internal.extension.scheduleWithDelay
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.presence.eventengine.event.PresenceEvent
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import kotlin.time.Duration

internal class WaitEffect(
    private val heartbeatInterval: Duration,
    private val presenceEventSink: Sink<PresenceEvent>,
    private val executorService: ScheduledExecutorService,
    private val logConfig: LogConfig,
) : ManagedEffect {
    private val log = LoggerManager.instance.getLogger(logConfig, this::class.java)

    @Transient
    private var cancelled: Boolean = false

    @Transient
    private var scheduled: ScheduledFuture<*>? = null

    @Synchronized
    override fun runEffect() {
        log.trace(
            LogMessage(
                location = this::class.java.simpleName,
                message = LogMessageContent.Text("Running WaitEffect.")
            )
        )
        if (cancelled) {
            return
        }

        try {
            scheduled =
                executorService.scheduleWithDelay(heartbeatInterval) {
                    presenceEventSink.add(PresenceEvent.TimesUp)
                }
        } catch (_: RejectedExecutionException) {
            log.trace(
                LogMessage(
                    location = this::class.java.simpleName,
                    message = LogMessageContent.Text("Unable to schedule retry, PubNub was likely already destroyed.")
                )
            )
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
