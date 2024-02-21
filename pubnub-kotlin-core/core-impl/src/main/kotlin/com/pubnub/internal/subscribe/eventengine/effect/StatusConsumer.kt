package com.pubnub.internal.subscribe.eventengine.effect

import com.pubnub.api.models.consumer.PNStatus

internal interface StatusConsumer {
    fun announce(status: PNStatus)
}
