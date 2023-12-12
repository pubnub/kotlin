package com.pubnub.api.v2.subscriptions

interface SubscribeCapable {
    fun subscribe(cursor: SubscriptionCursor? = null)
    fun unsubscribe()
}
