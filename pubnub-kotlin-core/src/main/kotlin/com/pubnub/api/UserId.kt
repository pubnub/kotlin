package com.pubnub.api

import com.pubnub.internal.PNConfiguration.Companion.isValid
import com.pubnub.internal.PubNubUtil

data class UserId(val value: String) {
    init {
        PubNubUtil.require(value.isValid(), PubNubError.USERID_NULL_OR_EMPTY)
    }
}
