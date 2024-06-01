package com.pubnub.test

import com.pubnub.api.JsonElement
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusListener
import com.pubnub.internal.managers.MapperManager

actual fun EventListener.emit(
    pubnub: PubNub,
    event: PNEvent
) {
    when (event) {
        is PNMessageResult -> this.message(pubnub, event)
        is PNMessageActionResult -> this.messageAction(pubnub, event)
        is PNFileEventResult -> this.file(pubnub, event)
        is PNObjectEventResult -> this.objects(pubnub, event)
        is PNSignalResult -> this.signal(pubnub, event)
        is PNPresenceEventResult -> this.presence(pubnub, event)
    }
}

private val mapper = MapperManager()

actual fun Any.toJsonElement(): JsonElement {
    return mapper.toJsonTree(this)
}

actual fun StatusListener.emit(
    pubnub: PubNub,
    status: PNStatus
) {
    this.status(pubnub, status)
}