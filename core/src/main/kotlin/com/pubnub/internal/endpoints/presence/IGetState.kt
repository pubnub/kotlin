package com.pubnub.internal.endpoints.presence

interface IGetState {
    val channels: List<String>
    val channelGroups: List<String>
    val uuid: String
}
