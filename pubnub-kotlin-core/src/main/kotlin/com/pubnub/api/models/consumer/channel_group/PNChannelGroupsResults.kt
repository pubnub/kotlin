package com.pubnub.api.models.consumer.channel_group

import com.pubnub.internal.PubNubImpl

/**
 * Result of the [PubNubImpl.addChannelsToChannelGroup] operation.
 */
class PNChannelGroupsAddChannelResult

/**
 * Result of the [PubNubImpl.deleteChannelGroup] operation.
 */
class PNChannelGroupsDeleteGroupResult

/**
 * Result of the [PubNubImpl.removeChannelsFromChannelGroup] operation.
 */
class PNChannelGroupsRemoveChannelResult

/**
 * Result of the [PubNubImpl.listChannelsForChannelGroup] operation.
 *
 * @property channels List of channels belonging to a channel group.
 */
class PNChannelGroupsAllChannelsResult internal constructor(
    val channels: List<String>
)

/**
 * Result of the [PubNubImpl.listAllChannelGroups] operation.
 *
 * @property groups List of all channel groups
 */
class PNChannelGroupsListAllResult internal constructor(
    val groups: List<String>
)
