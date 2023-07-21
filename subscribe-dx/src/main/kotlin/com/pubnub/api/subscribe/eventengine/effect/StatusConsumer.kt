package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.core.Status

interface StatusConsumer<in S : Status> {
    fun announce(status: S)
}
