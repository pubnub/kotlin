package com.pubnub.api.enums;

import org.jetbrains.annotations.Nullable;

/**
 * Created by Max on 4/7/16.
 */
public enum PNOperationType implements com.pubnub.core.OperationType {
    PNSubscribeOperation(""),
    PNUnsubscribeOperation("pres"),

    PNPublishOperation("pub"),
    PNSignalOperation("sig"),

    PNHistoryOperation("hist"),
    PNFetchMessagesOperation("hist"),
    PNDeleteMessagesOperation("hist"),
    PNMessageCountOperation("mc"),

    PNWhereNowOperation("pres"),

    PNHeartbeatOperation("pres"),
    PNSetStateOperation("pres"),
    PNAddChannelsToGroupOperation("cg"),
    PNRemoveChannelsFromGroupOperation("cg"),
    PNChannelGroupsOperation("cg"),
    PNRemoveGroupOperation("cg"),
    PNChannelsForGroupOperation("cg"),
    PNPushNotificationEnabledChannelsOperation("push"),
    PNAddPushNotificationsOnChannelsOperation("push"),
    PNRemovePushNotificationsFromChannelsOperation("push"),
    PNRemoveAllPushNotificationsOperation("push"),
    PNTimeOperation("time"),

    // CREATED
    PNHereNowOperation("pres"),
    PNGetState("pres"),
    PNAccessManagerAudit("pam"),
    PNAccessManagerGrant("pam"),

    // UUID Metadata
    PNSetUuidMetadataOperation("obj"),
    PNGetUuidMetadataOperation("obj"),
    PNGetAllUuidMetadataOperation("obj"),
    PNRemoveUuidMetadataOperation("obj"),
    // Channel Metadata
    PNSetChannelMetadataOperation("obj"),
    PNGetChannelMetadataOperation("obj"),
    PNGetAllChannelsMetadataOperation("obj"),
    PNRemoveChannelMetadataOperation("obj"),

    // Memberships
    PNSetMembershipsOperation("obj"),
    PNGetMembershipsOperation("obj"),
    PNRemoveMembershipsOperation("obj"),
    PNManageMembershipsOperation("obj"),

    // Members
    PNSetChannelMembersOperation("obj"),
    PNGetChannelMembersOperation("obj"),
    PNRemoveChannelMembersOperation("obj"),
    PNManageChannelMembersOperation("obj"),

    // PAMv3
    PNAccessManagerGrantToken("pamv3"),
    PNAccessManagerRevokeToken("pamv3"),

    // Message Actions
    PNAddMessageAction("msga"),
    PNGetMessageActions("msga"),
    PNDeleteMessageAction("msga"),

    PNFileAction("file");

    private final String queryParam;

    PNOperationType(String queryParam) {
        this.queryParam = queryParam;
    }

    @Nullable
    @Override
    public String getQueryParam() {
        return queryParam;
    }
}
