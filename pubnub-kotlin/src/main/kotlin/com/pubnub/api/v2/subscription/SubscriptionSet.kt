package com.pubnub.api.v2.subscription

import com.pubnub.api.v2.callbacks.EventEmitter
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.BaseSubscriptionSet

interface SubscriptionSet : BaseSubscriptionSet<EventListener, Subscription>, EventEmitter {
    operator fun plus(subscription: Subscription): SubscriptionSet
}
