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
    PNHereNowOperation,

    PNHeartbeatOperation,

    PNSetStateOperation,
    PNGetState,


    PNAddChannelsToGroupOperation,
    PNRemoveChannelsFromGroupOperation,
    PNChannelGroupsOperation,
    PNRemoveGroupOperation,
    PNChannelsForGroupOperation,

    PNPushNotificationModifiedChannelsOperations,
    PNPushNotificationListChannelsOperation,

    PNPushNotificationEnabledChannelsOperation,
    PNAddPushNotificationsOnChannelsOperation,
    PNRemovePushNotificationsFromChannelsOperation,
    PNRemoveAllPushNotificationsOperation,

    PNAccessManagerAudit,
    PNAccessManagerGrant,

    PNTimeOperation,
}
