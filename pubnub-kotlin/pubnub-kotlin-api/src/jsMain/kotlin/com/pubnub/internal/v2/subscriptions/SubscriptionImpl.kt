package com.pubnub.internal.v2.subscriptions

import com.pubnub.api.callbacks.Listener
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionCursor
import com.pubnub.api.v2.subscriptions.SubscriptionSet

class SubscriptionImpl(private val jsSubscription: dynamic) : Subscription {

    override fun close() {
        unsubscribe()
    }

    override fun plus(subscription: Subscription): SubscriptionSet {
        TODO("Not yet implemented")
    }

    override fun addListener(listener: EventListener) {
        jsSubscription.addListener(listener)
    }


    override fun removeListener(listener: Listener) {
        jsSubscription.removeListener(listener)
    }

    override fun removeAllListeners() {
        TODO("Not yet implemented") //todo js doesn't support this?
    }

    override fun subscribe(cursor: SubscriptionCursor) { // todo use cursor
        jsSubscription.subscribe()
    }

    override fun unsubscribe() {
        jsSubscription.unsubscribe()
    }

    override var onMessage: ((PNMessageResult) -> Unit)?
        get() = TODO("Not yet implemented")
        set(value) {}

    override var onPresence: ((PNPresenceEventResult) -> Unit)?
        get() = TODO("Not yet implemented")
        set(value) {}

    override var onSignal: ((PNSignalResult) -> Unit)?
        get() = TODO("Not yet implemented")
        set(value) {}

    override var onMessageAction: ((PNMessageActionResult) -> Unit)?
        get() = TODO("Not yet implemented")
        set(value) {}

    override var onObjects: ((PNObjectEventResult) -> Unit)?
        get() = TODO("Not yet implemented")
        set(value) {}

    override var onFile: ((PNFileEventResult) -> Unit)?
        get() = TODO("Not yet implemented")
        set(value) {}

}
