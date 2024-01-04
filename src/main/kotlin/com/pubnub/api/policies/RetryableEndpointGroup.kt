package com.pubnub.api.policies

enum class RetryableEndpointGroup {
    SUBSCRIBE, // subscribe
    PUBLISH, // publish, publishFileMessage, signal, fire
    PRESENCE, // getPresenceState, setPresenceState, hereNow, whereNow, presence/Heartbeat, Leave
    FILE_PERSISTENCE, // getFileUrl, deleteFile, listFiles, downloadFile, sendFile,
    MESSAGE_PERSISTENCE, // fetchMessages, deleteMessages, messageCounts, time
    CHANNEL_GROUP, // listAllChannelGroups, deleteChannelGroup, removeChannelsFromChannelGroup, listChannelsForChannelGroup, addChannelsToChannelGroup
    PUSH_NOTIFICATION, // removeAllPushNotificationsFromDeviceWithPushToken, addPushNotificationsOnChannels, auditPushChannelProvisions, removePushNotificationsFromChannels
    APP_CONTEXT, // getAllUUIDMetadata, getUUIDMetadata, setUUIDMetadata, removeUUIDMetadata, getAllChannelMetadata, getChannelMetadata, removeChannelMetadata, setChannelMetadata, getChannelMembers, manageChannelMembers, getMemberships, manageMemberships
    MESSAGE_REACTION, // addMessageAction, getMessageActions, removeMessageAction
    ACCESS_MANAGER, // grant, grantToken, revokeToken
}
