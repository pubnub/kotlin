package com.pubnub.internal.endpoints

interface IDeleteMessages {
    val channels: List<String>
    val start: Long?
    val end: Long?
}
