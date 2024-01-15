package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import org.slf4j.LoggerFactory
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

internal class HandshakeReconnectEffect(
    private val handshakeRemoteAction: RemoteAction<SubscriptionCursor>,
    private val subscribeEventSink: Sink<SubscribeEvent>,
    private val policy: RetryPolicy,
    private val executorService: ScheduledExecutorService,
    private val handshakeReconnectInvocation: SubscribeEffectInvocation.HandshakeReconnect,
) : ManagedEffect {
    private val log = LoggerFactory.getLogger(HandshakeReconnectEffect::class.java)

    @Transient
    private var scheduled: ScheduledFuture<*>? = null

    @Transient
    private var cancelled = false

    @Synchronized
    override fun runEffect() {
        log.trace("Running HandshakeReconnectEffect")

        if (cancelled) {
            return
        }

        val delay = policy.nextDelay(handshakeReconnectInvocation.attempts)
        if (delay == null) {
            subscribeEventSink.add(SubscribeEvent.HandshakeReconnectGiveup(handshakeReconnectInvocation.reason ?: PubNubException("Unknown error")))
            return
        }

        scheduled = executorService.schedule({
            println("-=HandshakeReconnectEffect") // todo remove
            handshakeRemoteAction.async { result, status ->
                if (status.error) {
                    subscribeEventSink.add(SubscribeEvent.HandshakeReconnectFailure(status.exception ?: PubNubException("Unknown error")))
                } else {
                    subscribeEventSink.add(SubscribeEvent.HandshakeReconnectSuccess(result!!))
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
        handshakeRemoteAction.silentCancel()
        scheduled?.cancel(true)
    }

//    private fun nextDelay(attempt: Int): Duration? {
//        if (attempt >= maxRetries) {
//            return null
//        }
//        return computeDelay(attempt)
//    }
}
