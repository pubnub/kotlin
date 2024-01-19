package com.pubnub.internal.endpoints.channel_groups

interface IAddChannelChannelGroup {
    val channelGroup: String
    val channels: List<String>
}
