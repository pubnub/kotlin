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

    // What follows is a terrible hack to support the legacy Java SDK field access on the PNOperationType class
    // Otherwise customers would have to do .INSTANCE every time, but with this we are source-compatible!
    companion object {
        @JvmField
        val FileOperation: FileOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$FileOperation").getField("INSTANCE").get(null) as FileOperation

        @JvmField
        val SpaceOperation: SpaceOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$SpaceOperation").getField("INSTANCE").get(null) as SpaceOperation

        @JvmField
        val UserOperation: UserOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$UserOperation").getField("INSTANCE").get(null) as UserOperation

        @JvmField
        val MembershipOperation: MembershipOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$MembershipOperation").getField("INSTANCE").get(null) as MembershipOperation

        @JvmField
        val PNSubscribeOperation: PNSubscribeOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNSubscribeOperation").getField("INSTANCE").get(null) as PNSubscribeOperation

        @JvmField
        val PNDisconnectOperation: PNDisconnectOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNDisconnectOperation").getField("INSTANCE").get(null) as PNDisconnectOperation

        @JvmField
        val PNPublishOperation: PNPublishOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNPublishOperation").getField("INSTANCE").get(null) as PNPublishOperation

        @JvmField
        val PNHistoryOperation: PNHistoryOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNHistoryOperation").getField("INSTANCE").get(null) as PNHistoryOperation

        @JvmField
        val PNFetchMessagesOperation: PNFetchMessagesOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNFetchMessagesOperation").getField("INSTANCE").get(null) as PNFetchMessagesOperation

        @JvmField
        val PNDeleteMessagesOperation: PNDeleteMessagesOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNDeleteMessagesOperation").getField("INSTANCE").get(null) as PNDeleteMessagesOperation

        @JvmField
        val PNUnsubscribeOperation: PNUnsubscribeOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNUnsubscribeOperation").getField("INSTANCE").get(null) as PNUnsubscribeOperation

        @JvmField
        val PNWhereNowOperation: PNWhereNowOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNWhereNowOperation").getField("INSTANCE").get(null) as PNWhereNowOperation

        @JvmField
        val PNHereNowOperation: PNHereNowOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNHereNowOperation").getField("INSTANCE").get(null) as PNHereNowOperation

        @JvmField
        val PNHeartbeatOperation: PNHeartbeatOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNHeartbeatOperation").getField("INSTANCE").get(null) as PNHeartbeatOperation

        @JvmField
        val PNSetStateOperation: PNSetStateOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNSetStateOperation").getField("INSTANCE").get(null) as PNSetStateOperation

        @JvmField
        val PNGetState: PNGetState = Class.forName("com.pubnub.api.enums.PNOperationType\$PNGetState").getField("INSTANCE").get(null) as PNGetState

        @JvmField
        val PNAddChannelsToGroupOperation: PNAddChannelsToGroupOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNAddChannelsToGroupOperation").getField("INSTANCE").get(null) as PNAddChannelsToGroupOperation

        @JvmField
        val PNRemoveChannelsFromGroupOperation: PNRemoveChannelsFromGroupOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNRemoveChannelsFromGroupOperation").getField("INSTANCE").get(null) as PNRemoveChannelsFromGroupOperation

        @JvmField
        val PNChannelGroupsOperation: PNChannelGroupsOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNChannelGroupsOperation").getField("INSTANCE").get(null) as PNChannelGroupsOperation

        @JvmField
        val PNRemoveGroupOperation: PNRemoveGroupOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNRemoveGroupOperation").getField("INSTANCE").get(null) as PNRemoveGroupOperation

        @JvmField
        val PNChannelsForGroupOperation: PNChannelsForGroupOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNChannelsForGroupOperation").getField("INSTANCE").get(null) as PNChannelsForGroupOperation

        @JvmField
        val PNPushNotificationEnabledChannelsOperation: PNPushNotificationEnabledChannelsOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNPushNotificationEnabledChannelsOperation").getField("INSTANCE").get(null) as PNPushNotificationEnabledChannelsOperation

        @JvmField
        val PNAddPushNotificationsOnChannelsOperation: PNAddPushNotificationsOnChannelsOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNAddPushNotificationsOnChannelsOperation").getField("INSTANCE").get(null) as PNAddPushNotificationsOnChannelsOperation

        @JvmField
        val PNRemovePushNotificationsFromChannelsOperation: PNRemovePushNotificationsFromChannelsOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNRemovePushNotificationsFromChannelsOperation").getField("INSTANCE").get(null) as PNRemovePushNotificationsFromChannelsOperation

        @JvmField
        val PNRemoveAllPushNotificationsOperation: PNRemoveAllPushNotificationsOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNRemoveAllPushNotificationsOperation").getField("INSTANCE").get(null) as PNRemoveAllPushNotificationsOperation

        @JvmField
        val PNAccessManagerAudit: PNAccessManagerAudit = Class.forName("com.pubnub.api.enums.PNOperationType\$PNAccessManagerAudit").getField("INSTANCE").get(null) as PNAccessManagerAudit

        @JvmField
        val PNAccessManagerGrant: PNAccessManagerGrant = Class.forName("com.pubnub.api.enums.PNOperationType\$PNAccessManagerGrant").getField("INSTANCE").get(null) as PNAccessManagerGrant

        @JvmField
        val PNMessageCountOperation: PNMessageCountOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNMessageCountOperation").getField("INSTANCE").get(null) as PNMessageCountOperation

        @JvmField
        val PNSignalOperation: PNSignalOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNSignalOperation").getField("INSTANCE").get(null) as PNSignalOperation

        @JvmField
        val PNSetUUIDMetadataOperation: PNSetUUIDMetadataOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNSetUUIDMetadataOperation").getField("INSTANCE").get(null) as PNSetUUIDMetadataOperation

        @JvmField
        val PNGetUUIDMetadataOperation: PNGetUUIDMetadataOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNGetUUIDMetadataOperation").getField("INSTANCE").get(null) as PNGetUUIDMetadataOperation

        @JvmField
        val PNGetAllUUIDMetadataOperation: PNGetAllUUIDMetadataOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNGetAllUUIDMetadataOperation").getField("INSTANCE").get(null) as PNGetAllUUIDMetadataOperation

        @JvmField
        val PNRemoveUUIDMetadataOperation: PNRemoveUUIDMetadataOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNRemoveUUIDMetadataOperation").getField("INSTANCE").get(null) as PNRemoveUUIDMetadataOperation

        @JvmField
        val PNSetChannelMetadataOperation: PNSetChannelMetadataOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNSetChannelMetadataOperation").getField("INSTANCE").get(null) as PNSetChannelMetadataOperation

        @JvmField
        val PNGetChannelMetadataOperation: PNGetChannelMetadataOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNGetChannelMetadataOperation").getField("INSTANCE").get(null) as PNGetChannelMetadataOperation

        @JvmField
        val PNGetAllChannelsMetadataOperation: PNGetAllChannelsMetadataOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNGetAllChannelsMetadataOperation").getField("INSTANCE").get(null) as PNGetAllChannelsMetadataOperation

        @JvmField
        val PNRemoveChannelMetadataOperation: PNRemoveChannelMetadataOperation =
            Class.forName("com.pubnub.api.enums.PNOperationType\$PNRemoveChannelMetadataOperation").getField("INSTANCE").get(null) as PNRemoveChannelMetadataOperation

        @JvmField
        val PNGetMembershipsOperation: PNGetMembershipsOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNGetMembershipsOperation").getField("INSTANCE").get(null) as PNGetMembershipsOperation

        @JvmField
        val PNSetMembershipsOperation: PNSetMembershipsOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNSetMembershipsOperation").getField("INSTANCE").get(null) as PNSetMembershipsOperation

        @JvmField
        val PNUpdateMembershipsOperation: PNUpdateMembershipsOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNUpdateMembershipsOperation").getField("INSTANCE").get(null) as PNUpdateMembershipsOperation

        @JvmField
        val PNManageMemberships: PNManageMemberships = Class.forName("com.pubnub.api.enums.PNOperationType\$PNManageMemberships").getField("INSTANCE").get(null) as PNManageMemberships

        @JvmField
        val PNAccessManagerGrantToken: PNAccessManagerGrantToken = Class.forName("com.pubnub.api.enums.PNOperationType\$PNAccessManagerGrantToken").getField("INSTANCE").get(null) as PNAccessManagerGrantToken

        @JvmField
        val PNAccessManagerRevokeToken: PNAccessManagerRevokeToken = Class.forName("com.pubnub.api.enums.PNOperationType\$PNAccessManagerRevokeToken").getField("INSTANCE").get(null) as PNAccessManagerRevokeToken

        @JvmField
        val PNAddMessageAction: PNAddMessageAction = Class.forName("com.pubnub.api.enums.PNOperationType\$PNAddMessageAction").getField("INSTANCE").get(null) as PNAddMessageAction

        @JvmField
        val PNGetMessageActions: PNGetMessageActions = Class.forName("com.pubnub.api.enums.PNOperationType\$PNGetMessageActions").getField("INSTANCE").get(null) as PNGetMessageActions

        @JvmField
        val PNDeleteMessageAction: PNDeleteMessageAction = Class.forName("com.pubnub.api.enums.PNOperationType\$PNDeleteMessageAction").getField("INSTANCE").get(null) as PNDeleteMessageAction

        @JvmField
        val PNTimeOperation: PNTimeOperation = Class.forName("com.pubnub.api.enums.PNOperationType\$PNTimeOperation").getField("INSTANCE").get(null) as PNTimeOperation
    }
}
