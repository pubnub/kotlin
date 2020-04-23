package com.pubnub.api.enums

import java.util.*

enum class PNUserFields(s: String) {
    CUSTOM("custom");

    private val value: String = s

    override fun toString(): String {
        return value.toLowerCase(Locale.US)

    }

}