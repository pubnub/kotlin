package com.pubnub.core

data class UserId(val value: String) {
    init {
        require(value.isNotBlank()) { "UserId can't have empty value" }
    }
}
