package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.eventengine.EventSink
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.subscribe.eventengine.event.Event
import org.slf4j.LoggerFactory
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class ReceiveReconnectEffect(
    private val remoteAction: RemoteAction<ReceiveMessagesResult>,
    private val eventSink: EventSink,
    private val policy: RetryPolicy,
    private val executorService: ScheduledExecutorService,
    private val attempts: Int,
    private val reason: PubNubException?
) : ManagedEffect {
    private val log = LoggerFactory.getLogger(EffectDispatcher::class.java)

    @Transient
    private var scheduled: ScheduledFuture<*>? = null

    @Transient
    private var cancelled = false

    @Synchronized
    override fun runEffect() {
        log.trace("Running ReceiveReconnectEffect thread: ${Thread.currentThread().id}")

        if (cancelled) {
            return
        }

        val delay = policy.nextDelay(attempts)
        if (delay == null) {
            eventSink.add(Event.ReceiveReconnectGiveUp(reason ?: PubNubException("Unknown error")))
            return
        }

        scheduled = executorService.schedule({
            remoteAction.async { result, status ->
                if (status.error) {
                    eventSink.add(
                        Event.ReceiveReconnectFailure(
                            status.exception ?: PubNubException("Unknown error")
                        )
                    )
                } else {
                    eventSink.add(
                        Event.ReceiveReconnectSuccess(
                            result!!.messages,
                            result.subscriptionCursor
                        )
                    )
                }
            }
        }, delay.toMillis(), TimeUnit.MILLISECONDS)
    }

    @Synchronized
    override fun cancel() {
        if (cancelled) {
            return
        }
        cancelled = true

        remoteAction.silentCancel()
        scheduled?.cancel(true)
    }
}
