package com.pubnub.internal.endpoints

interface IHistory {
    val channel: String
    val start: Long?
    val end: Long?
    val count: Int
    val reverse: Boolean
    val includeTimetoken: Boolean
    val includeMeta: Boolean
}
