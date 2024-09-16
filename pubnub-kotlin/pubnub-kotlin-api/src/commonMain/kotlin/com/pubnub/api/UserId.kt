package com.pubnub.api

import kotlin.js.JsExport

@JsExport
data class UserId
    @Throws(PubNubException::class)
    constructor(val value: String) {
        init {
            if (value.isBlank()) {
                throw PubNubException(PubNubError.USERID_NULL_OR_EMPTY)
            }
        }
    }
