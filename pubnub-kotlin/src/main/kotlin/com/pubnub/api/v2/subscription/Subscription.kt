package com.pubnub.api.v2.subscription

import com.pubnub.api.v2.callbacks.EventEmitter
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.BaseSubscription

interface Subscription : BaseSubscription<EventListener>, EventEmitter {
    operator fun plus(subscription: Subscription): SubscriptionSet
}