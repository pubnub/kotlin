package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.EventDeliver
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.subscribe.eventengine.event.Event

class ReceiveMessagesEffect(
    private val receiveMessagesProvider: ReceiveMessagesProvider,
    private val eventDeliver: EventDeliver,
    private val receiveMessagesInvocation: SubscribeEffectInvocation.ReceiveMessages
) : ManagedEffect {
    override fun runEffect(completionBlock: () -> Unit) {
        val receiveMessagesRemoteAction = getReceiveMessagesRemoteAction()
        receiveMessagesRemoteAction.async { result, status ->
            try {
                if (status.error) {
                    eventDeliver.passEventForHandling(
                        Event.ReceiveFailure(
                            status.exception
                                ?: PubNubException("Unknown error") // todo check it that can happen
                        )
                    )
                } else {
                    eventDeliver.passEventForHandling(
                        Event.ReceiveSuccess(
                            result!!.messages,
                            result.subscriptionCursor
                        )
                    )
                }
            } finally {
                completionBlock()
            }
        }
    }

    override fun cancel() {
        val receiveMessagesRemoteAction = getReceiveMessagesRemoteAction()
        receiveMessagesRemoteAction.silentCancel()
    }

    private fun getReceiveMessagesRemoteAction(): RemoteAction<ReceiveMessagesResult> =
        receiveMessagesProvider.receiveMessages(
            receiveMessagesInvocation.channels,
            receiveMessagesInvocation.channelGroups,
            receiveMessagesInvocation.subscriptionCursor
        )
}
