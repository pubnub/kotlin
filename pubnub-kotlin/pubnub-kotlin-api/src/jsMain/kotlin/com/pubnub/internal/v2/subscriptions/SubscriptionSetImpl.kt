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

class SubscriptionSetImpl(private val jsSubscriptionSet: dynamic) : SubscriptionSet {
    override fun close() {
        unsubscribe()
    }

    override fun plusAssign(subscription: Subscription) {
        TODO("Not yet implemented")
    }

    override fun minusAssign(subscription: Subscription) {
        TODO("Not yet implemented")
    }

    override fun add(subscription: Subscription) {
        TODO("Not yet implemented")
    }

    override fun remove(subscription: Subscription) {
        TODO("Not yet implemented")
    }

    override val subscriptions: Set<Subscription>
        get() = TODO("Not yet implemented")

    override fun addListener(listener: EventListener) {
        jsSubscriptionSet.addListener(listener)
    }

    override fun removeListener(listener: Listener) {
        jsSubscriptionSet.removeListener(listener)
    }

    override fun removeAllListeners() {
        TODO("Not yet implemented")
    }

    override fun subscribe(cursor: SubscriptionCursor) {
        jsSubscriptionSet.subscribe()
    }

    override fun unsubscribe() {
        jsSubscriptionSet.unsubscribe()
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
