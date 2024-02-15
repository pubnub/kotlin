package com.pubnub.api.models.consumer.channel_group

/**
 * Result of the AddChannelsToChannelGroup operation.
 */
class PNChannelGroupsAddChannelResult

/**
 * Result of the DeleteChannelGroup operation.
 */
class PNChannelGroupsDeleteGroupResult

/**
 * Result of the RemoveChannelsFromChannelGroup operation.
 */
class PNChannelGroupsRemoveChannelResult

/**
 * Result of the ListChannelsForChannelGroup operation.
 *
 * @property channels List of channels belonging to a channel group.
 */
class PNChannelGroupsAllChannelsResult internal constructor(
    val channels: List<String>
)

/**
 * Result of the ListAllChannelGroups operation.
 *
 * @property groups List of all channel groups
 */
class PNChannelGroupsListAllResult internal constructor(
    val groups: List<String>
)
