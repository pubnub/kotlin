package com.pubnub.api.managers

import com.pubnub.api.PubNub

class BasePathManager(val pubnub: PubNub) {

    fun basePath(): String {
        return StringBuilder("http")
            .append(if (pubnub.config.secure) "s" else "")
            .append("://")
            .append(pubnub.config.origin)
            .toString()
    }

}