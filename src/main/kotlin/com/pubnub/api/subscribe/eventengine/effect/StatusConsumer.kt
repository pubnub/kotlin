package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.models.consumer.PNStatus

internal interface StatusConsumer {
    fun announce(status: PNStatus)
}
