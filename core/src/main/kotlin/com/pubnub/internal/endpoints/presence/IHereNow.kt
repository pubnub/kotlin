package com.pubnub.internal.endpoints.presence

interface IHereNow {
    val channels: List<String>
    val channelGroups: List<String>
    val includeState: Boolean
    val includeUUIDs: Boolean
}
