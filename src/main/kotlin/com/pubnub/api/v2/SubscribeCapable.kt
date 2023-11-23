package com.pubnub.api.v2

interface SubscribeCapable {
    fun subscribe(cursor: SubscriptionCursor? = null)
    fun unsubscribe()
}
