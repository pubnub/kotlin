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

actual fun EventListener.emit(
    pubnub: PubNub,
    event: PNEvent
) {
    when (event) {
        is PNMessageResult -> this.onMessage(pubnub, event)
        is PNMessageActionResult -> this.onMessageAction(pubnub, event)
        is PNFileEventResult -> this.onFile(pubnub, event)
        is PNObjectEventResult -> this.onObjects(pubnub, event)
        is PNSignalResult -> this.onSignal(pubnub, event)
        is PNPresenceEventResult -> this.onPresence(pubnub, event)
    }
}

actual fun Any.toJsonElement(): JsonElement {
    TODO("Not yet implemented")
}

actual fun StatusListener.emit(
    pubnub: PubNub,
    status: PNStatus
) {
    TODO("Not yet implemented")
}