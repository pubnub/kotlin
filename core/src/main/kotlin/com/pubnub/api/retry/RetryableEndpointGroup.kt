package com.pubnub.api.retry

/**
 * Enum representing various retryable endpoint groups.
 * Each enum constant denotes a specific category of operations
 * that can be retried under certain conditions.
 */
enum class RetryableEndpointGroup {
    /**
     * Represents operation related to subscribing like [PubNub.subscribe]
     */
    SUBSCRIBE,
    /**
     * Represents the group of operations related to publishing like [PubNub.publish], [PubNub.publishFileMessage], [PubNub.signal], [PubNub.fire]
     */
    PUBLISH,
    /**
     * Represents the group of operations related to presence like [PubNub.getPresenceState], [PubNub.setPresenceState],
     *   [PubNub.hereNow], [PubNub.whereNow], [PubNub.presence] (Heartbeat), and Leave.
     */
    PRESENCE,
    /**
     * Represents the group of operations related to file persistence like [PubNub.getFileUrl], [PubNub.deleteFile],
     *   [PubNub.listFiles], [PubNub.downloadFile], and [PubNub.sendFile].
     */
    FILE_PERSISTENCE,
    /**
     * Represents the group of operations related to message persistence like [PubNub.fetchMessages],
     *   [PubNub.deleteMessages], [PubNub.messageCounts], and [PubNub.time].
     */
    MESSAGE_PERSISTENCE,
    /**
     * Represents the group of operations on channel group like  [PubNub.listAllChannelGroups], [PubNub.deleteChannelGroup],
     *   [PubNub.removeChannelsFromChannelGroup], [PubNub.listChannelsForChannelGroup], and [PubNub.addChannelsToChannelGroup]
     */
    CHANNEL_GROUP,
    /**
     * Represents the group of operations related to push notification like [PubNub.removeAllPushNotificationsFromDeviceWithPushToken],
     *   [PubNub.addPushNotificationsOnChannels], [PubNub.auditPushChannelProvisions] and [PubNub.removePushNotificationsFromChannels]
     */
    PUSH_NOTIFICATION,
    /**
     * Represents the group of operations related to application context like [PubNub.getAllUUIDMetadata], [PubNub.getUUIDMetadata],
     *   [PubNub.setUUIDMetadata], [PubNub.removeUUIDMetadata], [PubNub.getAllChannelMetadata], [PubNub.getChannelMetadata],
     *   [PubNub.removeChannelMetadata], [PubNub.setChannelMetadata], [PubNub.getChannelMembers], [PubNub.manageChannelMembers],
     *   [PubNub.getMemberships], and [PubNub.manageMemberships]
     */
    APP_CONTEXT,
    /**
     * Represents the group of operations related to message reaction like [PubNub.addMessageAction],
     *   [PubNub.getMessageActions] and [PubNub.removeMessageAction]
     */
    MESSAGE_REACTION,
    /**
     * Represents the group of operations related to access management like [PubNub.grant], [PubNub.grantToken], [PubNub.revokeToken]
     */
    ACCESS_MANAGER,
}
