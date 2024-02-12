package com.pubnub.api.v2.callbacks

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.Listener
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult

interface EventEmitter {
    var onMessage: ((PubNub, PNMessageResult) -> Unit)?
    var onPresence: ((PubNub, PNPresenceEventResult) -> Unit)?
    var onSignal: ((PubNub, PNSignalResult) -> Unit)?
    var onMessageAction: ((PubNub, PNMessageActionResult) -> Unit)?
    var onObjects: ((PubNub, PNObjectEventResult) -> Unit)?
    var onFile: ((PubNub, PNFileEventResult) -> Unit)?

    /**
     * Add a listener.
     *
     * @param listener The listener to be added.
     */
    fun addListener(listener: EventListener)

    /**
     * Remove a listener.
     *
     * @param listener The listener to be removed, previously added with [addListener].
     */
    fun removeListener(listener: Listener)

    /**
     * Removes all listeners.
     */
    fun removeAllListeners()
}
