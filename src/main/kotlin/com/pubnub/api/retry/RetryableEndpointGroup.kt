package com.pubnub.api.retry

import com.pubnub.api.PubNub
import com.pubnub.api.retry.RetryableEndpointGroup.ACCESS_MANAGER
import com.pubnub.api.retry.RetryableEndpointGroup.APP_CONTEXT
import com.pubnub.api.retry.RetryableEndpointGroup.CHANNEL_GROUP
import com.pubnub.api.retry.RetryableEndpointGroup.FILE_PERSISTENCE
import com.pubnub.api.retry.RetryableEndpointGroup.MESSAGE_PERSISTENCE
import com.pubnub.api.retry.RetryableEndpointGroup.MESSAGE_REACTION
import com.pubnub.api.retry.RetryableEndpointGroup.PRESENCE
import com.pubnub.api.retry.RetryableEndpointGroup.PUBLISH
import com.pubnub.api.retry.RetryableEndpointGroup.PUSH_NOTIFICATION
import com.pubnub.api.retry.RetryableEndpointGroup.SUBSCRIBE

/**
 * Enum representing various retryable endpoint groups.
 *
 * @property SUBSCRIBE Group for [PubNub.subscribe] operations.
 * @property PUBLISH Group for publish operations including [PubNub.publish], [PubNub.publishFileMessage], [PubNub.signal], [PubNub.fire].
 * @property PRESENCE Group for presence operations like [PubNub.getPresenceState], [PubNub.setPresenceState],
 *   [PubNub.hereNow], [PubNub.whereNow], [PubNub.presence] (Heartbeat), and Leave.
 * @property FILE_PERSISTENCE Group for file persistence operations including [PubNub.getFileUrl], [PubNub.deleteFile],
 *   [PubNub.listFiles], [PubNub.downloadFile], and [PubNub.sendFile].
 * @property MESSAGE_PERSISTENCE Group for message persistence operations such as [PubNub.fetchMessages],
 *   [PubNub.deleteMessages], [PubNub.messageCounts], and [PubNub.time].
 * @property CHANNEL_GROUP Group for channel group operations including [PubNub.listAllChannelGroups], [PubNub.deleteChannelGroup],
 *   [PubNub.removeChannelsFromChannelGroup], [PubNub.listChannelsForChannelGroup], and [PubNub.addChannelsToChannelGroup].
 * @property PUSH_NOTIFICATION Group for push notification operations like [PubNub.removeAllPushNotificationsFromDeviceWithPushToken],
 *   [PubNub.addPushNotificationsOnChannels], [PubNub.auditPushChannelProvisions], [PubNub.removePushNotificationsFromChannels],
 * @property APP_CONTEXT Group for application context operations including [PubNub.getAllUUIDMetadata], [PubNub.getUUIDMetadata],
 *   [PubNub.setUUIDMetadata], [PubNub.removeUUIDMetadata], [PubNub.getAllChannelMetadata], [PubNub.getChannelMetadata],
 *   [PubNub.removeChannelMetadata], [PubNub.setChannelMetadata], [PubNub.getChannelMembers], [PubNub.manageChannelMembers],
 *   [PubNub.getMemberships], and [PubNub.manageMemberships].
 * @property MESSAGE_REACTION Group for message reaction operations such as [PubNub.addMessageAction], [PubNub.getMessageActions], [PubNub.removeMessageAction].
 * @property ACCESS_MANAGER Group for access management operations including [PubNub.grant], [PubNub.grantToken], [PubNub.revokeToken].
 */

enum class RetryableEndpointGroup {
    SUBSCRIBE,
    PUBLISH,
    PRESENCE,
    FILE_PERSISTENCE,
    MESSAGE_PERSISTENCE,
    CHANNEL_GROUP,
    PUSH_NOTIFICATION,
    APP_CONTEXT,
    MESSAGE_REACTION,
    ACCESS_MANAGER,
}
