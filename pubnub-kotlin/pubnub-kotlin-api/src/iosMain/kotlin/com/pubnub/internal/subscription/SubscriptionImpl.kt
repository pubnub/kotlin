package com.pubnub.internal.subscription

import cocoapods.PubNubSwift.PubNubSubscriptionObjC
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
class SubscriptionImpl(
    val objCSubscription: PubNubSubscriptionObjC
) : Subscription {
    override fun close() {
        objCSubscription.dispose()
    }

    override fun addListener(listener: EventListener) {
        objCSubscription.addListener(listener = listener.underlying)
    }

    override fun removeListener(listener: Listener) {
        (listener as? EventListener)?.let { objCSubscription.removeListener(it.underlying) }
    }

    override fun removeAllListeners() {
        objCSubscription.removeAllListeners()
    }

    override fun subscribe(cursor: SubscriptionCursor) {
        objCSubscription.subscribeWith(timetoken = cursor.timetoken.toULong())
    }

    override fun unsubscribe() {
        objCSubscription.unsubscribe()
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

    override fun plus(subscription: Subscription): SubscriptionSet {
        TODO("Not yet implemented")
    }
}
