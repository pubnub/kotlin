package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult

interface MessagesConsumer {
    fun announce(status: PNStatus) // toDo move to EmitStatusProvider

    fun announce(message: PNMessageResult)

    fun announce(presence: PNPresenceEventResult)

    fun announce(signal: PNSignalResult)

    fun announce(messageAction: PNMessageActionResult)

    fun announce(pnObjectEventResult: PNObjectEventResult)

    fun announce(pnFileEventResult: PNFileEventResult)
}
