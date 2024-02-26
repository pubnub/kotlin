package com.pubnub.internal.v2.callbacks

import com.pubnub.api.BasePubNub
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.models.toApi

open class DelegatingEventListener(private val listener: EventListener) : InternalEventListener {
    override fun message(
        pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
        event: PNMessageResult,
    ) {
        listener.message(pubnub as PubNubImpl, event)
    }

    override fun presence(
        pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
        event: PNPresenceEventResult,
    ) {
        listener.presence(pubnub as PubNubImpl, event)
    }

    override fun signal(
        pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
        event: PNSignalResult,
    ) {
        listener.signal(pubnub as PubNubImpl, event)
    }

    override fun messageAction(
        pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
        event: PNMessageActionResult,
    ) {
        listener.messageAction(pubnub as PubNubImpl, event)
    }

    override fun objects(
        pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
        event: com.pubnub.internal.models.consumer.pubsub.objects.PNObjectEventResult,
    ) {
        listener.objects(pubnub as PubNubImpl, event.toApi())
    }

    override fun file(
        pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
        event: PNFileEventResult,
    ) {
        listener.file(pubnub as PubNubImpl, event)
    }

    override fun equals(other: Any?): Boolean {
        // WARNING: this was modified from the generated one to add "this.listener === other"
        // it is crucial that this remains untouched for ListenerManager to work properly
        if (this === other || this.listener === other) return true
        if (other !is DelegatingEventListener) return false

        if (listener != other.listener) return false

        return true
    }

    override fun hashCode(): Int {
        return listener.hashCode()
    }
}
