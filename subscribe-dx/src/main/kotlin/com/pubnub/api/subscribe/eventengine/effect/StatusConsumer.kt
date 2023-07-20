package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.core.Status

interface StatusConsumer {
    fun announce(status: Status)
}
