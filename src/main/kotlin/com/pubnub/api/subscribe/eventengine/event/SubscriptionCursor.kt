package com.pubnub.api.subscribe.eventengine.event

data class SubscriptionCursor(val timetoken: String, val region: Long) {
    constructor(timetoken: Long, region: String) : this(timetoken.toString(), region.toLong())
}
