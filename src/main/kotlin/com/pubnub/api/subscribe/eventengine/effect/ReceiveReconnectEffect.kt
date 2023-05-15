package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.EventQueue
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.subscribe.eventengine.event.Event
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class ReceiveReconnectEffect(
    private val remoteAction: RemoteAction<ReceiveMessagesResult>,
    private val eventQueue: EventQueue,
    private val policy: RetryPolicy,
    private val executorService: ScheduledExecutorService,
    private val attempts: Int,
    private val reason: PubNubException?
) : ManagedEffect {

    @Transient
    private var scheduled: ScheduledFuture<*>? = null

    @Transient
    private var cancelled = false
    override fun runEffect() {
        if (cancelled) {
            return
        }

        val delay = policy.nextDelay(attempts)
        if (delay == null) {
            eventQueue.add(event = Event.ReceiveReconnectGiveUp(reason!!))
            return
        }

        scheduled = executorService.schedule({
            remoteAction.async { result, status ->
                if (status.error) {
                    eventQueue.add(
                        Event.ReceiveReconnectFailure(
                            status.exception ?: PubNubException("dfa")
                        )
                    )
                } else {
                    eventQueue.add(
                        Event.ReceiveReconnectSuccess(
                            result!!.messages,
                            result.subscriptionCursor
                        )
                    )
                }
            }
        }, delay.toMillis(), TimeUnit.MILLISECONDS)
    }

    override fun cancel() {
        if (cancelled) {
            return
        }
        cancelled = true

        remoteAction.silentCancel()
        scheduled?.cancel(true)
    }
}
