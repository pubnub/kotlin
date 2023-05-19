package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.eventengine.Effect
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult

class EmitMessagesEffect(
    val messagesConsumer: MessagesConsumer,
    val messages: List<PNEvent>
) : Effect {
    override fun runEffect() {
        for (message in messages) {
            when (message) {
                is PNMessageResult -> messagesConsumer.announce(message)
                is PNPresenceEventResult -> messagesConsumer.announce(message)
                is PNSignalResult -> messagesConsumer.announce(message)
                is PNMessageActionResult -> messagesConsumer.announce(message)
                is PNObjectEventResult -> messagesConsumer.announce(message)
                is PNFileEventResult -> messagesConsumer.announce(message)
            }
        }
    }
}
