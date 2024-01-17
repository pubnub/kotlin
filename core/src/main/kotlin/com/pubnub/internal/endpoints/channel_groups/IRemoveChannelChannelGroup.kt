package com.pubnub.internal.endpoints.channel_groups

interface IRemoveChannelChannelGroup {
    val channelGroup: String
    val channels: List<String>
}
