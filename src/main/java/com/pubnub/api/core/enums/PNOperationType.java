package com.pubnub.api.core.enums;

/**
 * Created by Max on 4/7/16.
 */
public enum PNOperationType {
    PNSubscribeOperation,
    PNUnsubscribeOperation,
    PNPublishOperation,
    PNHistoryOperation,
    PNWhereNowOperation,
    PNHereNowGlobalOperation,
    PNHereNowForChannelOperation,
    PNHereNowForChannelGroupOperation,
    PNHeartbeatOperation,
    PNSetStateOperation,
    PNStateForChannelOperation,
    PNStateForChannelGroupOperation,
    PNAddChannelsToGroupOperation,
    PNRemoveChannelsFromGroupOperation,
    PNChannelGroupsOperation,
    PNRemoveGroupOperation,
    PNChannelsForGroupOperation,
    PNPushNotificationEnabledChannelsOperation,
    PNAddPushNotificationsOnChannelsOperation,
    PNRemovePushNotificationsFromChannelsOperation,
    PNRemoveAllPushNotificationsOperation,
    PNTimeOperation,
}
