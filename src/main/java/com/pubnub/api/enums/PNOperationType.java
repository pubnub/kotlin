package com.pubnub.api.enums;

/**
 * Created by Max on 4/7/16.
 */
public enum PNOperationType {
    PNSubscribeOperation,
    PNUnsubscribeOperation,

    PNPublishOperation,
    PNSignalOperation,

    PNHistoryOperation,
    PNFetchMessagesOperation,
    PNDeleteMessagesOperation,
    PNMessageCountOperation,

    PNWhereNowOperation,

    PNHeartbeatOperation,
    PNSetStateOperation,
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

    // CREATED
    PNHereNowOperation,
    PNGetState,
    PNAccessManagerAudit,
    PNAccessManagerGrant,

    // Users
    PNCreateUserOperation,
    PNGetUserOperation,
    PNGetUsersOperation,
    PNUpdateUserOperation,
    PNDeleteUserOperation,

    // Spaces
    PNCreateSpaceOperation,
    PNGetSpaceOperation,
    PNGetSpacesOperation,
    PNUpdateSpaceOperation,
    PNDeleteSpaceOperation,

    // Members
    PNGetMembers,
    PNManageMembers,

    // Memberships
    PNGetMemberships,
    PNManageMemberships,

    // PAMv3
    PNAccessManagerGrantToken,

    // Message Actions
    PNAddMessageAction,
    PNGetMessageActions,
    PNDeleteMessageAction
}
