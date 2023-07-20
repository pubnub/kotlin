package com.pubnub.api.enums

import com.pubnub.core.OperationType

internal const val PUB = "pub"
internal const val HIST = "hist"
internal const val PRES = "pres"
internal const val CG = "cg"
internal const val PUSH = "push"
internal const val PAM = "pam"
internal const val MC = "mc"
internal const val SIG = "sig"
internal const val OBJ = "obj"
internal const val PAMV3 = "pamv3"
internal const val MSGA = "msga"
internal const val FILE = "file"
internal const val TIME = "time"

enum class PNOperationType(override val queryParam: String? = null) : OperationType {
    PNPublishOperation(PUB),

    PNHistoryOperation(HIST),
    PNFetchMessagesOperation(HIST),
    PNDeleteMessagesOperation(HIST),

    PNUnsubscribeOperation(PRES),
    PNWhereNowOperation(PRES),
    PNHereNowOperation(PRES),
    PNHeartbeatOperation(PRES),
    PNSetStateOperation(PRES),
    PNGetState(PRES),

    PNAddChannelsToGroupOperation(CG),
    PNRemoveChannelsFromGroupOperation(CG),
    PNChannelGroupsOperation(CG),
    PNRemoveGroupOperation(CG),
    PNChannelsForGroupOperation(CG),

    PNPushNotificationEnabledChannelsOperation(PUSH),
    PNAddPushNotificationsOnChannelsOperation(PUSH),
    PNRemovePushNotificationsFromChannelsOperation(PUSH),
    PNRemoveAllPushNotificationsOperation(PUSH),

    PNAccessManagerAudit(PAM),
    PNAccessManagerGrant(PAM),

    PNMessageCountOperation(MC),

    PNSignalOperation(SIG),

    PNSetUuidMetadataOperation(OBJ),
    PNGetUuidMetadataOperation(OBJ),
    PNGetAllUuidMetadataOperation(OBJ),
    PNRemoveUuidMetadataOperation(OBJ),
    PNSetChannelMetadataOperation(OBJ),
    PNGetChannelMetadataOperation(OBJ),
    PNGetAllChannelsMetadataOperation(OBJ),
    PNRemoveChannelMetadataOperation(OBJ),
    PNSetMembershipsOperation(OBJ),
    PNGetMembershipsOperation(OBJ),
    PNRemoveMembershipsOperation(OBJ),
    PNManageMembershipsOperation(OBJ),
    PNSetChannelMembersOperation(OBJ),
    PNGetChannelMembersOperation(OBJ),
    PNRemoveChannelMembersOperation(OBJ),
    PNManageChannelMembersOperation(OBJ),

    PNAccessManagerGrantToken(PAMV3),
    PNAccessManagerRevokeToken(PAMV3),

    PNAddMessageAction(MSGA),
    PNGetMessageActions(MSGA),
    PNDeleteMessageAction(MSGA),

    PNFileAction(FILE),

    PNTimeOperation(TIME),

    PNSubscribeOperation;

    override fun toString(): String {
        return this.javaClass.simpleName
    }
}
