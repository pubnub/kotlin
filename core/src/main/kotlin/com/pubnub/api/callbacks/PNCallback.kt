package com.pubnub.api.callbacks

import com.pubnub.api.models.consumer.PNStatus

fun interface PNCallback<X> {
    fun onResponse(result: X?, status: PNStatus)
}
