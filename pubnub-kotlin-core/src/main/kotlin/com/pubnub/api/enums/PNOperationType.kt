package com.pubnub.api.enums

sealed class PNOperationType(open val queryParam: String? = null) {

    open class PublishOperation : PNOperationType("pub")
    open class HistoryOperation : PNOperationType("hist")
    open class PresenceOperation : PNOperationType("pres")
    open class ChannelGroupOperation : PNOperationType("cg")
    open class PushNotificationsOperation : PNOperationType("push")
    open class PAMOperation : PNOperationType("pam")
    open class MessageCountsOperation : PNOperationType("mc")
    open class SignalsOperation : PNOperationType("sig")
    open class ObjectsOperation : PNOperationType("obj")
    open class PAMV3Operation : PNOperationType("pamv3")
    open class MessageActionsOperation : PNOperationType("msga")
    open class TimeOperation : PNOperationType("time")
    object FileOperation : PNOperationType("file")
    object SpaceOperation : PNOperationType("obj")
    object UserOperation : PNOperationType("obj")
    object MembershipOperation : PNOperationType("obj")

    object PNSubscribeOperation : PNOperationType()
    object PNDisconnectOperation : PNOperationType()

    object PNPublishOperation : PublishOperation()

    object PNHistoryOperation : HistoryOperation()
    object PNFetchMessagesOperation : HistoryOperation()
    object PNDeleteMessagesOperation : HistoryOperation()

    object PNUnsubscribeOperation : PresenceOperation()
    object PNWhereNowOperation : PresenceOperation()
    object PNHereNowOperation : PresenceOperation()
    object PNHeartbeatOperation : PresenceOperation()
    object PNSetStateOperation : PresenceOperation()
    object PNGetState : PresenceOperation()

    object PNAddChannelsToGroupOperation : ChannelGroupOperation()
    object PNRemoveChannelsFromGroupOperation : ChannelGroupOperation()
    object PNChannelGroupsOperation : ChannelGroupOperation()
    object PNRemoveGroupOperation : ChannelGroupOperation()
    object PNChannelsForGroupOperation : ChannelGroupOperation()

    object PNPushNotificationEnabledChannelsOperation : PushNotificationsOperation()
    object PNAddPushNotificationsOnChannelsOperation : PushNotificationsOperation()
    object PNRemovePushNotificationsFromChannelsOperation : PushNotificationsOperation()
    object PNRemoveAllPushNotificationsOperation : PushNotificationsOperation()

    object PNAccessManagerAudit : PAMOperation()
    object PNAccessManagerGrant : PAMOperation()

    object PNMessageCountOperation : MessageCountsOperation()

    object PNSignalOperation : SignalsOperation()

    object PNSetUUIDMetadataOperation : ObjectsOperation()
    object PNGetUUIDMetadataOperation : ObjectsOperation()
    object PNGetAllUUIDMetadataOperation : ObjectsOperation()
    object PNRemoveUUIDMetadataOperation : ObjectsOperation()
    object PNSetChannelMetadataOperation : ObjectsOperation()
    object PNGetChannelMetadataOperation : ObjectsOperation()
    object PNGetAllChannelsMetadataOperation : ObjectsOperation()
    object PNRemoveChannelMetadataOperation : ObjectsOperation()
    object PNGetMembershipsOperation : ObjectsOperation()
    object PNSetMembershipsOperation : ObjectsOperation()
    object PNUpdateMembershipsOperation : ObjectsOperation()
    object PNManageMemberships : ObjectsOperation()

    object PNAccessManagerGrantToken : PAMV3Operation()
    object PNAccessManagerRevokeToken : PAMV3Operation()

    object PNAddMessageAction : MessageActionsOperation()
    object PNGetMessageActions : MessageActionsOperation()
    object PNDeleteMessageAction : MessageActionsOperation()

    object PNTimeOperation : TimeOperation()

    override fun toString(): String {
        return this.javaClass.simpleName
    }

    companion object {

        @kotlin.jvm.JvmField
        val FileOperation: FileOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$FileOperation").getField("INSTANCE")
                .get(null) as FileOperation

        @kotlin.jvm.JvmField
        val SpaceOperation: SpaceOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$SpaceOperation").getField("INSTANCE")
                .get(null) as SpaceOperation

        @kotlin.jvm.JvmField
        val UserOperation: UserOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$UserOperation").getField("INSTANCE")
                .get(null) as UserOperation

        @kotlin.jvm.JvmField
        val MembershipOperation: MembershipOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$MembershipOperation").getField("INSTANCE")
                .get(null) as MembershipOperation

        @kotlin.jvm.JvmField
        val PNSubscribeOperation: PNSubscribeOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNSubscribeOperation").getField("INSTANCE")
                .get(null) as PNSubscribeOperation

        @kotlin.jvm.JvmField
        val PNDisconnectOperation: PNDisconnectOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNDisconnectOperation").getField("INSTANCE")
                .get(null) as PNDisconnectOperation

        @kotlin.jvm.JvmField
        val PNPublishOperation: PNPublishOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNPublishOperation").getField("INSTANCE")
                .get(null) as PNPublishOperation

        @kotlin.jvm.JvmField
        val PNHistoryOperation: PNHistoryOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNHistoryOperation").getField("INSTANCE")
                .get(null) as PNHistoryOperation

        @kotlin.jvm.JvmField
        val PNFetchMessagesOperation: PNFetchMessagesOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNFetchMessagesOperation").getField("INSTANCE")
                .get(null) as PNFetchMessagesOperation

        @kotlin.jvm.JvmField
        val PNDeleteMessagesOperation: PNDeleteMessagesOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNDeleteMessagesOperation").getField("INSTANCE")
                .get(null) as PNDeleteMessagesOperation

        @kotlin.jvm.JvmField
        val PNUnsubscribeOperation: PNUnsubscribeOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNUnsubscribeOperation").getField("INSTANCE")
                .get(null) as PNUnsubscribeOperation

        @kotlin.jvm.JvmField
        val PNWhereNowOperation: PNWhereNowOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNWhereNowOperation").getField("INSTANCE")
                .get(null) as PNWhereNowOperation

        @kotlin.jvm.JvmField
        val PNHereNowOperation: PNHereNowOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNHereNowOperation").getField("INSTANCE")
                .get(null) as PNHereNowOperation

        @kotlin.jvm.JvmField
        val PNHeartbeatOperation: PNHeartbeatOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNHeartbeatOperation").getField("INSTANCE")
                .get(null) as PNHeartbeatOperation

        @kotlin.jvm.JvmField
        val PNSetStateOperation: PNSetStateOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNSetStateOperation").getField("INSTANCE")
                .get(null) as PNSetStateOperation

        @kotlin.jvm.JvmField
        val PNGetState: PNGetState =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNGetState").getField("INSTANCE")
                .get(null) as PNGetState

        @kotlin.jvm.JvmField
        val PNAddChannelsToGroupOperation: PNAddChannelsToGroupOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNAddChannelsToGroupOperation").getField("INSTANCE")
                .get(null) as PNAddChannelsToGroupOperation

        @kotlin.jvm.JvmField
        val PNRemoveChannelsFromGroupOperation: PNRemoveChannelsFromGroupOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNRemoveChannelsFromGroupOperation")
                .getField("INSTANCE").get(null) as PNRemoveChannelsFromGroupOperation

        @kotlin.jvm.JvmField
        val PNChannelGroupsOperation: PNChannelGroupsOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNChannelGroupsOperation").getField("INSTANCE")
                .get(null) as PNChannelGroupsOperation

        @kotlin.jvm.JvmField
        val PNRemoveGroupOperation: PNRemoveGroupOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNRemoveGroupOperation").getField("INSTANCE")
                .get(null) as PNRemoveGroupOperation

        @kotlin.jvm.JvmField
        val PNChannelsForGroupOperation: PNChannelsForGroupOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNChannelsForGroupOperation").getField("INSTANCE")
                .get(null) as PNChannelsForGroupOperation

        @kotlin.jvm.JvmField
        val PNPushNotificationEnabledChannelsOperation: PNPushNotificationEnabledChannelsOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNPushNotificationEnabledChannelsOperation")
                .getField("INSTANCE").get(null) as PNPushNotificationEnabledChannelsOperation

        @kotlin.jvm.JvmField
        val PNAddPushNotificationsOnChannelsOperation: PNAddPushNotificationsOnChannelsOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNAddPushNotificationsOnChannelsOperation")
                .getField("INSTANCE").get(null) as PNAddPushNotificationsOnChannelsOperation

        @kotlin.jvm.JvmField
        val PNRemovePushNotificationsFromChannelsOperation: PNRemovePushNotificationsFromChannelsOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNRemovePushNotificationsFromChannelsOperation")
                .getField("INSTANCE").get(null) as PNRemovePushNotificationsFromChannelsOperation

        @kotlin.jvm.JvmField
        val PNRemoveAllPushNotificationsOperation: PNRemoveAllPushNotificationsOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNRemoveAllPushNotificationsOperation")
                .getField("INSTANCE").get(null) as PNRemoveAllPushNotificationsOperation

        @kotlin.jvm.JvmField
        val PNAccessManagerAudit: PNAccessManagerAudit =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNAccessManagerAudit").getField("INSTANCE")
                .get(null) as PNAccessManagerAudit

        @kotlin.jvm.JvmField
        val PNAccessManagerGrant: PNAccessManagerGrant =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNAccessManagerGrant").getField("INSTANCE")
                .get(null) as PNAccessManagerGrant

        @kotlin.jvm.JvmField
        val PNMessageCountOperation: PNMessageCountOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNMessageCountOperation").getField("INSTANCE")
                .get(null) as PNMessageCountOperation

        @kotlin.jvm.JvmField
        val PNSignalOperation: PNSignalOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNSignalOperation").getField("INSTANCE")
                .get(null) as PNSignalOperation

        @kotlin.jvm.JvmField
        val PNSetUUIDMetadataOperation: PNSetUUIDMetadataOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNSetUUIDMetadataOperation").getField("INSTANCE")
                .get(null) as PNSetUUIDMetadataOperation

        @kotlin.jvm.JvmField
        val PNGetUUIDMetadataOperation: PNGetUUIDMetadataOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNGetUUIDMetadataOperation").getField("INSTANCE")
                .get(null) as PNGetUUIDMetadataOperation

        @kotlin.jvm.JvmField
        val PNGetAllUUIDMetadataOperation: PNGetAllUUIDMetadataOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNGetAllUUIDMetadataOperation").getField("INSTANCE")
                .get(null) as PNGetAllUUIDMetadataOperation

        @kotlin.jvm.JvmField
        val PNRemoveUUIDMetadataOperation: PNRemoveUUIDMetadataOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNRemoveUUIDMetadataOperation").getField("INSTANCE")
                .get(null) as PNRemoveUUIDMetadataOperation

        @kotlin.jvm.JvmField
        val PNSetChannelMetadataOperation: PNSetChannelMetadataOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNSetChannelMetadataOperation").getField("INSTANCE")
                .get(null) as PNSetChannelMetadataOperation

        @kotlin.jvm.JvmField
        val PNGetChannelMetadataOperation: PNGetChannelMetadataOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNGetChannelMetadataOperation").getField("INSTANCE")
                .get(null) as PNGetChannelMetadataOperation

        @kotlin.jvm.JvmField
        val PNGetAllChannelsMetadataOperation: PNGetAllChannelsMetadataOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNGetAllChannelsMetadataOperation")
                .getField("INSTANCE").get(null) as PNGetAllChannelsMetadataOperation

        @kotlin.jvm.JvmField
        val PNRemoveChannelMetadataOperation: PNRemoveChannelMetadataOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNRemoveChannelMetadataOperation").getField("INSTANCE")
                .get(null) as PNRemoveChannelMetadataOperation

        @kotlin.jvm.JvmField
        val PNGetMembershipsOperation: PNGetMembershipsOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNGetMembershipsOperation").getField("INSTANCE")
                .get(null) as PNGetMembershipsOperation

        @kotlin.jvm.JvmField
        val PNSetMembershipsOperation: PNSetMembershipsOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNSetMembershipsOperation").getField("INSTANCE")
                .get(null) as PNSetMembershipsOperation

        @kotlin.jvm.JvmField
        val PNUpdateMembershipsOperation: PNUpdateMembershipsOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNUpdateMembershipsOperation").getField("INSTANCE")
                .get(null) as PNUpdateMembershipsOperation

        @kotlin.jvm.JvmField
        val PNManageMemberships: PNManageMemberships =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNManageMemberships").getField("INSTANCE")
                .get(null) as PNManageMemberships

        @kotlin.jvm.JvmField
        val PNAccessManagerGrantToken: PNAccessManagerGrantToken =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNAccessManagerGrantToken").getField("INSTANCE")
                .get(null) as PNAccessManagerGrantToken

        @kotlin.jvm.JvmField
        val PNAccessManagerRevokeToken: PNAccessManagerRevokeToken =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNAccessManagerRevokeToken").getField("INSTANCE")
                .get(null) as PNAccessManagerRevokeToken

        @kotlin.jvm.JvmField
        val PNAddMessageAction: PNAddMessageAction =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNAddMessageAction").getField("INSTANCE")
                .get(null) as PNAddMessageAction

        @kotlin.jvm.JvmField
        val PNGetMessageActions: PNGetMessageActions =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNGetMessageActions").getField("INSTANCE")
                .get(null) as PNGetMessageActions

        @kotlin.jvm.JvmField
        val PNDeleteMessageAction: PNDeleteMessageAction =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNDeleteMessageAction").getField("INSTANCE")
                .get(null) as PNDeleteMessageAction

        @kotlin.jvm.JvmField
        val PNTimeOperation: PNTimeOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNTimeOperation").getField("INSTANCE")
                .get(null) as PNTimeOperation
    }
}
