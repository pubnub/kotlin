package com.pubnub.internal.v2.subscription

import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.BaseSubscriptionSet
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.internal.PubNubCore
import com.pubnub.internal.v2.callbacks.DelegatingEventListener

class SubscriptionSetImpl(
    pubnub: PubNubCore,
    initialSubscriptions: Set<SubscriptionImpl>,
) : SubscriptionSet, BaseSubscriptionSetImpl<EventListener, Subscription>(pubnub, initialSubscriptions) {
    /**
     * Adds a [BaseSubscription] to this set. Equivalent to calling [add].
     *
     * Please note that this [BaseSubscriptionSet] will *not* attempt to ensure all subscriptions match their
     * active/inactive state. That is, if you previously called [subscribe] or [unsubscribe] on this set, it will not be
     * called on the newly added [subscription] automatically.
     *
     * @param subscription the [BaseSubscription] to add.
     * @see [add]
     */

    override operator fun plus(subscription: Subscription): SubscriptionSet {
        add(subscription)
        return this
    }

    /**
     * Add a listener.
     *
     * @param listener The listener to be added.
     */
    override fun addListener(listener: EventListener) {
        addListener(DelegatingEventListener(listener))
    }

    private val emitterHelper = EmitterHelper(eventEmitter)
    override var onMessage: ((PNMessageResult) -> Unit)? by emitterHelper::onMessage
    override var onPresence: ((PNPresenceEventResult) -> Unit)? by emitterHelper::onPresence
    override var onSignal: ((PNSignalResult) -> Unit)? by emitterHelper::onSignal
    override var onMessageAction: ((PNMessageActionResult) -> Unit)? by emitterHelper::onMessageAction
    override var onObjects: ((PNObjectEventResult) -> Unit)? by emitterHelper::onObjects
    override var onFile: ((PNFileEventResult) -> Unit)? by emitterHelper::onFile
}
