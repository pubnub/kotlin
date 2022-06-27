package com.pubnub.api

import com.google.gson.annotations.JsonAdapter
import com.pubnub.api.PNConfiguration.Companion.isValid
import com.pubnub.api.utils.DataClassAsValue

@JsonAdapter(DataClassAsValue::class)
data class UserId( val value: String) {
    init {
        PubNubUtil.require(value.isValid(), PubNubError.USERID_NULL_OR_EMPTY)
    }
}
