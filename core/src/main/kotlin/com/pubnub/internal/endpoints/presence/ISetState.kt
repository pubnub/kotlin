package com.pubnub.internal.endpoints.presence

interface ISetState {
    val channels: List<String>
    val channelGroups: List<String>
    val state: Any
    val uuid: String
}
