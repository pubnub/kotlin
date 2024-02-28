package com.pubnub.api

data class UserId(val value: String) {
    init {
        if (value.isBlank()) {
            throw PubNubException(PubNubError.USERID_NULL_OR_EMPTY)
        }
    }
}
