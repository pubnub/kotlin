package com.pubnub.extension

fun Int.nonPositiveToNull() = if (this < 1)
    null
else
    this

fun Int.limit(limit: Int): Int {
    return when {
        this > limit -> limit
        else -> this
    }
}
