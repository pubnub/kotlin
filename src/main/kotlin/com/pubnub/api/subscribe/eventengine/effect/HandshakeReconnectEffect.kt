package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.EventDeliver
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class HandshakeReconnectEffect(
    private val handshakeProvider: HandshakeProvider,
    private val eventDeliver: EventDeliver,
    private val policy: RetryPolicy,
    private val handshakeReconnectInvocation: SubscribeEffectInvocation.HandshakeReconnect,
    private val executorService: ScheduledExecutorService,
) : ManagedEffect {

    @Transient
    private var scheduled: ScheduledFuture<*>? = null

    @Transient
    private var cancelled = false

    @Synchronized
    override fun runEffect(completionBlock: () -> Unit) {
        if (cancelled) { // is it ok to be here or should be after "if (delay == null)"
            return
        }

        val delay = policy.nextDelay(handshakeReconnectInvocation.attempts)
        if (delay == null) {
            eventDeliver.passEventForHandling(event = Event.HandshakeReconnectGiveUp(handshakeReconnectInvocation.reason!!))
            return
        }

        scheduled = executorService.schedule({
            try {
                val handshakeRemoteAction = getHandshakeRemoteAction()
                handshakeRemoteAction.async { result, status ->
                    if (status.error) {
                        eventDeliver.passEventForHandling(
                            Event.HandshakeReconnectFailure(
                                status.exception ?: PubNubException("Unknown error")
                            )
                        )
                    } else {
                        eventDeliver.passEventForHandling(
                            Event.HandshakeReconnectSuccess(
                                handshakeReconnectInvocation.channels,
                                handshakeReconnectInvocation.channelGroups,
                                result!!
                            )
                        )
                    }
                }
            } finally {
                completionBlock()
            }
        }, delay.toMillis(), TimeUnit.MILLISECONDS)
    }

    @Synchronized
    override fun cancel() {
        if (cancelled) {
            return
        }
        cancelled = true
        val handshakeRemoteAction = getHandshakeRemoteAction()
        handshakeRemoteAction.silentCancel()
        scheduled?.cancel(true)
    }

    private fun getHandshakeRemoteAction(): RemoteAction<SubscriptionCursor> {
        return handshakeProvider.handshake(
            handshakeReconnectInvocation.channels,
            handshakeReconnectInvocation.channelGroups
        )
    }
}
