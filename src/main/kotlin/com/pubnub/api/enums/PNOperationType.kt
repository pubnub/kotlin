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

    object PNSubscribeOperation : PNOperationType()

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

    object PNCreateUserOperation : ObjectsOperation()
    object PNGetUserOperation : ObjectsOperation()
    object PNGetUsersOperation : ObjectsOperation()
    object PNUpdateUserOperation : ObjectsOperation()
    object PNDeleteUserOperation : ObjectsOperation()
    object PNCreateSpaceOperation : ObjectsOperation()
    object PNGetSpaceOperation : ObjectsOperation()
    object PNGetSpacesOperation : ObjectsOperation()
    object PNUpdateSpaceOperation : ObjectsOperation()
    object PNDeleteSpaceOperation : ObjectsOperation()
    object PNGetMembers : ObjectsOperation()
    object PNManageMembers : ObjectsOperation()
    object PNGetMemberships : ObjectsOperation()
    object PNManageMemberships : ObjectsOperation()

    object PNAccessManagerGrantToken : PAMV3Operation()

    object PNAddMessageAction : MessageActionsOperation()
    object PNGetMessageActions : MessageActionsOperation()
    object PNDeleteMessageAction : MessageActionsOperation()

    object PNTimeOperation : TimeOperation()

    override fun toString(): String {
        return this.javaClass.simpleName
    }
}
