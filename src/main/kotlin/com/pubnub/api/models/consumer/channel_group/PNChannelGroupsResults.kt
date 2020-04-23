package com.pubnub.api.models.consumer.channel_group

class PNChannelGroupsAddChannelResult

class PNChannelGroupsDeleteGroupResult

class PNChannelGroupsRemoveChannelResult

class PNChannelGroupsAllChannelsResult {
    var channels = listOf<String>()
        internal set
}

class PNChannelGroupsListAllResult {
    var groups = listOf<String>()
        internal set
}