package com.pubnub.internal.subscribe.eventengine.effect

import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult

internal interface MessagesConsumer {
    fun announce(message: PNMessageResult)

    fun announce(presence: PNPresenceEventResult)

    fun announce(signal: PNSignalResult)

    fun announce(messageAction: PNMessageActionResult)

    fun announce(pnObjectEventResult: PNObjectEventResult)

    fun announce(pnFileEventResult: PNFileEventResult)
}
