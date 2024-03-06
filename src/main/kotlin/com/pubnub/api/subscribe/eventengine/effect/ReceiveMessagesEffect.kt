package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.models.TimeRange
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import org.slf4j.LoggerFactory

internal class ReceiveMessagesEffect(
    private val receiveMessagesRemoteAction: RemoteAction<ReceiveMessagesResult>,
    private val subscribeEventSink: Sink<SubscribeEvent>,
) : ManagedEffect {
    private val log = LoggerFactory.getLogger(ReceiveMessagesEffect::class.java)

    override fun runEffect() {
        log.trace("Running ReceiveMessagesEffect")

        receiveMessagesRemoteAction.async { result: ReceiveMessagesResult?, status ->
            if (status.error) {
                subscribeEventSink.add(
                    SubscribeEvent.ReceiveFailure(
                        status.exception
                            ?: PubNubException("Unknown error")
                    )
                )
            } else {
                if (result!!.missedMessages == null || result.missedMessages?.isEmpty()!!) {
                    subscribeEventSink.add(SubscribeEvent.ReceiveSuccess(result.messages, result.subscriptionCursor))
                } else {
                    // todo MissedMessages contains start/end Cursor that contains Region that is not needed for customers. That's why we create map.
                    val missedMessagesMap: Map<String, TimeRange> = createMissedMessagesMap(result)
                    subscribeEventSink.add(
                        SubscribeEvent.ReceiveSuccessWithMissedMessages(
                            result.messages,
                            result.subscriptionCursor,
                            missedMessagesMap
                        )
                    )
                }
            }
        }
    }

    // todo instead of passing ReceiveMessagesResult pass only result.missedMessages
    private fun createMissedMessagesMap(result: ReceiveMessagesResult): Map<String, TimeRange> {
        return result.missedMessages!!.associateBy(
            { missedMessages -> missedMessages.channel },
            { missedMessages ->
                TimeRange(
                    missedMessages.startingCursor.timeToken,
                    missedMessages.endingCursor.timeToken
                )
            }
        )
    }

    override fun cancel() {
        receiveMessagesRemoteAction.silentCancel()
    }
}
