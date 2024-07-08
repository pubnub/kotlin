package com.pubnub.internal.subscription

import cocoapods.PubNubSwift.PubNubSubscriptionSetObjC
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
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
class SubscriptionSetImpl(
    private val objCSubscriptionSet: PubNubSubscriptionSetObjC
) : SubscriptionSet {
    override fun close() {
        objCSubscriptionSet.dispose()
    }

    override fun addListener(listener: EventListener) {
        objCSubscriptionSet.addListener(listener = listener.underlying)
    }

    override fun removeListener(listener: Listener) {
        (listener as? EventListener)?.let { objCSubscriptionSet.removeListener(it.underlying) }
    }

    override fun removeAllListeners() {
        objCSubscriptionSet.removeAllListeners()
    }

    override fun add(subscription: Subscription) {
        TODO("Not yet implemented")
    }

    override fun remove(subscription: Subscription) {
        TODO("Not yet implemented")
    }

    override val subscriptions: Set<Subscription>
        get() = TODO("Not yet implemented")

    override fun subscribe(cursor: SubscriptionCursor) {
        TODO("Not yet implemented")
    }

    override fun unsubscribe() {
        TODO("Not yet implemented")
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

    override fun plusAssign(subscription: Subscription) {
        TODO("Not yet implemented")
    }

    override fun minusAssign(subscription: Subscription) {
        TODO("Not yet implemented")
    }
}
