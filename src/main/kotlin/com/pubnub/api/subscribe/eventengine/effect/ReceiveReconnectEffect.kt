package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.EventDeliver
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.subscribe.eventengine.event.Event
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class ReceiveReconnectEffect(
    private val receiveMessagesProvider: ReceiveMessagesProvider,
    private val eventDeliver: EventDeliver,
    private val policy: RetryPolicy,
    private val receiveReconnectInvocation: SubscribeEffectInvocation.ReceiveReconnect,
    private val executorService: ScheduledExecutorService,
) : ManagedEffect {

    @Transient
    private var scheduled: ScheduledFuture<*>? = null

    @Transient
    private var cancelled = false
    override fun runEffect(completionBlock: () -> Unit) {
        if (cancelled) {
            return
        }

        val delay = policy.nextDelay(receiveReconnectInvocation.attempts)
        if (delay == null) {
            eventDeliver.passEventForHandling(event = Event.ReceiveReconnectGiveUp(receiveReconnectInvocation.reason!!))
            return
        }

        scheduled = executorService.schedule({
            try {
                val receiveMessagesRemoteAction = getReceiveMessagesRemoteAction()
                receiveMessagesRemoteAction.async { result, status ->
                    if (status.error) {
                        eventDeliver.passEventForHandling(
                            Event.ReceiveReconnectFailure(
                                status.exception ?: PubNubException("dfa")
                            )
                        )
                    } else {
                        eventDeliver.passEventForHandling(
                            Event.ReceiveReconnectSuccess(
                                result!!.messages,
                                result.subscriptionCursor
                            )
                        )
                    }
                }
            } finally {
                completionBlock()
            }
        }, delay.toMillis(), TimeUnit.MILLISECONDS)
    }

    override fun cancel() {
        if (cancelled) {
            return
        }
        cancelled = true

        val receiveMessagesRemoteAction = getReceiveMessagesRemoteAction()
        receiveMessagesRemoteAction.silentCancel()
        scheduled?.cancel(true)
    }

    private fun getReceiveMessagesRemoteAction(): RemoteAction<ReceiveMessagesResult> =
        receiveMessagesProvider.receiveMessages(
            receiveReconnectInvocation.channels,
            receiveReconnectInvocation.channelGroups,
            receiveReconnectInvocation.subscriptionCursor
        )
}
