package com.pubnub.api

import com.pubnub.api.PNConfiguration.Companion.isValid

data class UserId(val value: String) {
    init {
        PubNubUtil.require(value.isValid(), PubNubError.USERID_NULL_OR_EMPTY)
    }
}
