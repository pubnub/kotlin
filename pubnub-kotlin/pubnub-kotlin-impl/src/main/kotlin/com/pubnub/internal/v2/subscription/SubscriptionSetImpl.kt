package com.pubnub.internal.v2.subscription

import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.internal.PubNubImpl

class SubscriptionSetImpl(
    pubnub: PubNubImpl,
    initialSubscriptions: Set<SubscriptionInternal>,
) : BaseSubscriptionSetImpl<Subscription>(pubnub, initialSubscriptions), SubscriptionSet {
    override val subscriptions: Set<Subscription> get() = subscriptionsInternal.toSet() as Set<Subscription>
}
