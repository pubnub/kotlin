package com.pubnub.internal.subscribe.eventengine.effect

import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.internal.eventengine.Effect
import com.pubnub.internal.logging.LogConfig
import com.pubnub.internal.logging.LoggerManager
import org.slf4j.event.Level

internal class EmitMessagesEffect(
    private val messagesConsumer: MessagesConsumer,
    private val messages: List<PNEvent>,
    private val logConfig: LogConfig,
) : Effect {
    private val log = LoggerManager.instance.getLogger(logConfig, this::class.java)

    override fun runEffect() {
        log.trace(
            LogMessage(
                pubNubId = logConfig.pnInstanceId,
                logLevel = Level.TRACE,
                location = this::class.java.simpleName,
                type = LogMessageType.TEXT,
                message = LogMessageContent.Text("Running EmitMessagesEffect: Emitting ${messages.size} messages to consumers"),
            )
        )
        for (message in messages) {
            try {
                when (message) {
                    is PNMessageResult -> messagesConsumer.announce(message)
                    is PNPresenceEventResult -> messagesConsumer.announce(message)
                    is PNSignalResult -> messagesConsumer.announce(message)
                    is PNMessageActionResult -> messagesConsumer.announce(message)
                    is PNObjectEventResult -> messagesConsumer.announce(message)
                    is PNFileEventResult -> messagesConsumer.announce(message)
                }
            } catch (_: Throwable) {
                // ignore
            }
        }
    }
}
