package com.pubnub.internal.endpoints

interface IMessageCounts {
    val channels: List<String>
    val channelsTimetoken: List<Long>
}
