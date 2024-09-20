package com.pubnub.kmp

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusListener

expect fun createPubNub(config: PNConfiguration): PubNub

expect fun createEventListener(
    pubnub: PubNub,
    onMessage: (PubNub, PNMessageResult) -> Unit = { _, _ -> },
    onPresence: (PubNub, PNPresenceEventResult) -> Unit = { _, _ -> },
    onSignal: (PubNub, PNSignalResult) -> Unit = { _, _ -> },
    onMessageAction: (PubNub, PNMessageActionResult) -> Unit = { _, _ -> },
    onObjects: (PubNub, PNObjectEventResult) -> Unit = { _, _ -> },
    onFile: (PubNub, PNFileEventResult) -> Unit = { _, _ -> },
): EventListener

expect fun createStatusListener(
    pubnub: PubNub,
    onStatus: (PubNub, PNStatus) -> Unit = { _, _ -> },
): StatusListener

expect fun createCustomObject(map: Map<String, Any?>): CustomObject
