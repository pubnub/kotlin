package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import org.slf4j.LoggerFactory
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class ReceiveReconnectEffect(
    private val receiveMessagesRemoteAction: RemoteAction<ReceiveMessagesResult>,
    private val subscribeEventSink: Sink<SubscribeEvent>,
    private val policy: RetryPolicy,
    private val executorService: ScheduledExecutorService,
    private val attempts: Int,
    private val reason: PubNubException?
) : ManagedEffect {
    private val log = LoggerFactory.getLogger(ReceiveReconnectEffect::class.java)

    @Transient
    private var scheduled: ScheduledFuture<*>? = null

    @Transient
    private var cancelled = false

    @Synchronized
    override fun runEffect() {
        log.trace("Running ReceiveReconnectEffect")

        if (cancelled) {
            return
        }

        val delay = policy.nextDelay(attempts)
        if (delay == null) {
            subscribeEventSink.add(SubscribeEvent.ReceiveReconnectGiveup(reason ?: PubNubException("Unknown error")))
            return
        }

        scheduled = executorService.schedule({
            receiveMessagesRemoteAction.async { result, status ->
                if (status.error) {
                    subscribeEventSink.add(
                        SubscribeEvent.ReceiveReconnectFailure(
                            status.exception ?: PubNubException("Unknown error")
                        )
                    )
                } else {
                    subscribeEventSink.add(
                        SubscribeEvent.ReceiveReconnectSuccess(
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

        receiveMessagesRemoteAction.silentCancel()
        scheduled?.cancel(true)
    }
}
