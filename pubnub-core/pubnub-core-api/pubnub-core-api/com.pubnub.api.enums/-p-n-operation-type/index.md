//[pubnub-core-api](../../../index.md)/[com.pubnub.api.enums](../index.md)/[PNOperationType](index.md)

# PNOperationType

sealed class [PNOperationType](index.md)

#### Inheritors

| |
|---|
| [PublishOperation](-publish-operation/index.md) |
| [HistoryOperation](-history-operation/index.md) |
| [PresenceOperation](-presence-operation/index.md) |
| [ChannelGroupOperation](-channel-group-operation/index.md) |
| [PushNotificationsOperation](-push-notifications-operation/index.md) |
| [PAMOperation](-p-a-m-operation/index.md) |
| [MessageCountsOperation](-message-counts-operation/index.md) |
| [SignalsOperation](-signals-operation/index.md) |
| [ObjectsOperation](-objects-operation/index.md) |
| [PAMV3Operation](-p-a-m-v3-operation/index.md) |
| [MessageActionsOperation](-message-actions-operation/index.md) |
| [TimeOperation](-time-operation/index.md) |
| [FileOperation](-file-operation/index.md) |
| [SpaceOperation](-space-operation/index.md) |
| [UserOperation](-user-operation/index.md) |
| [MembershipOperation](-membership-operation/index.md) |
| [PNSubscribeOperation](-p-n-subscribe-operation/index.md) |
| [PNDisconnectOperation](-p-n-disconnect-operation/index.md) |

## Types

| Name | Summary |
|---|---|
| [ChannelGroupOperation](-channel-group-operation/index.md) | [jvm]<br>open class [ChannelGroupOperation](-channel-group-operation/index.md) : [PNOperationType](index.md) |
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |
| [FileOperation](-file-operation/index.md) | [jvm]<br>object [FileOperation](-file-operation/index.md) : [PNOperationType](index.md) |
| [HistoryOperation](-history-operation/index.md) | [jvm]<br>open class [HistoryOperation](-history-operation/index.md) : [PNOperationType](index.md) |
| [MembershipOperation](-membership-operation/index.md) | [jvm]<br>object [MembershipOperation](-membership-operation/index.md) : [PNOperationType](index.md) |
| [MessageActionsOperation](-message-actions-operation/index.md) | [jvm]<br>open class [MessageActionsOperation](-message-actions-operation/index.md) : [PNOperationType](index.md) |
| [MessageCountsOperation](-message-counts-operation/index.md) | [jvm]<br>open class [MessageCountsOperation](-message-counts-operation/index.md) : [PNOperationType](index.md) |
| [ObjectsOperation](-objects-operation/index.md) | [jvm]<br>open class [ObjectsOperation](-objects-operation/index.md) : [PNOperationType](index.md) |
| [PAMOperation](-p-a-m-operation/index.md) | [jvm]<br>open class [PAMOperation](-p-a-m-operation/index.md) : [PNOperationType](index.md) |
| [PAMV3Operation](-p-a-m-v3-operation/index.md) | [jvm]<br>open class [PAMV3Operation](-p-a-m-v3-operation/index.md) : [PNOperationType](index.md) |
| [PNAccessManagerAudit](-p-n-access-manager-audit/index.md) | [jvm]<br>object [PNAccessManagerAudit](-p-n-access-manager-audit/index.md) : [PNOperationType.PAMOperation](-p-a-m-operation/index.md) |
| [PNAccessManagerGrant](-p-n-access-manager-grant/index.md) | [jvm]<br>object [PNAccessManagerGrant](-p-n-access-manager-grant/index.md) : [PNOperationType.PAMOperation](-p-a-m-operation/index.md) |
| [PNAccessManagerGrantToken](-p-n-access-manager-grant-token/index.md) | [jvm]<br>object [PNAccessManagerGrantToken](-p-n-access-manager-grant-token/index.md) : [PNOperationType.PAMV3Operation](-p-a-m-v3-operation/index.md) |
| [PNAccessManagerRevokeToken](-p-n-access-manager-revoke-token/index.md) | [jvm]<br>object [PNAccessManagerRevokeToken](-p-n-access-manager-revoke-token/index.md) : [PNOperationType.PAMV3Operation](-p-a-m-v3-operation/index.md) |
| [PNAddChannelsToGroupOperation](-p-n-add-channels-to-group-operation/index.md) | [jvm]<br>object [PNAddChannelsToGroupOperation](-p-n-add-channels-to-group-operation/index.md) : [PNOperationType.ChannelGroupOperation](-channel-group-operation/index.md) |
| [PNAddMessageAction](-p-n-add-message-action/index.md) | [jvm]<br>object [PNAddMessageAction](-p-n-add-message-action/index.md) : [PNOperationType.MessageActionsOperation](-message-actions-operation/index.md) |
| [PNAddPushNotificationsOnChannelsOperation](-p-n-add-push-notifications-on-channels-operation/index.md) | [jvm]<br>object [PNAddPushNotificationsOnChannelsOperation](-p-n-add-push-notifications-on-channels-operation/index.md) : [PNOperationType.PushNotificationsOperation](-push-notifications-operation/index.md) |
| [PNChannelGroupsOperation](-p-n-channel-groups-operation/index.md) | [jvm]<br>object [PNChannelGroupsOperation](-p-n-channel-groups-operation/index.md) : [PNOperationType.ChannelGroupOperation](-channel-group-operation/index.md) |
| [PNChannelsForGroupOperation](-p-n-channels-for-group-operation/index.md) | [jvm]<br>object [PNChannelsForGroupOperation](-p-n-channels-for-group-operation/index.md) : [PNOperationType.ChannelGroupOperation](-channel-group-operation/index.md) |
| [PNDeleteMessageAction](-p-n-delete-message-action/index.md) | [jvm]<br>object [PNDeleteMessageAction](-p-n-delete-message-action/index.md) : [PNOperationType.MessageActionsOperation](-message-actions-operation/index.md) |
| [PNDeleteMessagesOperation](-p-n-delete-messages-operation/index.md) | [jvm]<br>object [PNDeleteMessagesOperation](-p-n-delete-messages-operation/index.md) : [PNOperationType.HistoryOperation](-history-operation/index.md) |
| [PNDisconnectOperation](-p-n-disconnect-operation/index.md) | [jvm]<br>object [PNDisconnectOperation](-p-n-disconnect-operation/index.md) : [PNOperationType](index.md) |
| [PNFetchMessagesOperation](-p-n-fetch-messages-operation/index.md) | [jvm]<br>object [PNFetchMessagesOperation](-p-n-fetch-messages-operation/index.md) : [PNOperationType.HistoryOperation](-history-operation/index.md) |
| [PNGetAllChannelsMetadataOperation](-p-n-get-all-channels-metadata-operation/index.md) | [jvm]<br>object [PNGetAllChannelsMetadataOperation](-p-n-get-all-channels-metadata-operation/index.md) : [PNOperationType.ObjectsOperation](-objects-operation/index.md) |
| [PNGetAllUUIDMetadataOperation](-p-n-get-all-u-u-i-d-metadata-operation/index.md) | [jvm]<br>object [PNGetAllUUIDMetadataOperation](-p-n-get-all-u-u-i-d-metadata-operation/index.md) : [PNOperationType.ObjectsOperation](-objects-operation/index.md) |
| [PNGetChannelMetadataOperation](-p-n-get-channel-metadata-operation/index.md) | [jvm]<br>object [PNGetChannelMetadataOperation](-p-n-get-channel-metadata-operation/index.md) : [PNOperationType.ObjectsOperation](-objects-operation/index.md) |
| [PNGetMembershipsOperation](-p-n-get-memberships-operation/index.md) | [jvm]<br>object [PNGetMembershipsOperation](-p-n-get-memberships-operation/index.md) : [PNOperationType.ObjectsOperation](-objects-operation/index.md) |
| [PNGetMessageActions](-p-n-get-message-actions/index.md) | [jvm]<br>object [PNGetMessageActions](-p-n-get-message-actions/index.md) : [PNOperationType.MessageActionsOperation](-message-actions-operation/index.md) |
| [PNGetState](-p-n-get-state/index.md) | [jvm]<br>object [PNGetState](-p-n-get-state/index.md) : [PNOperationType.PresenceOperation](-presence-operation/index.md) |
| [PNGetUUIDMetadataOperation](-p-n-get-u-u-i-d-metadata-operation/index.md) | [jvm]<br>object [PNGetUUIDMetadataOperation](-p-n-get-u-u-i-d-metadata-operation/index.md) : [PNOperationType.ObjectsOperation](-objects-operation/index.md) |
| [PNHeartbeatOperation](-p-n-heartbeat-operation/index.md) | [jvm]<br>object [PNHeartbeatOperation](-p-n-heartbeat-operation/index.md) : [PNOperationType.PresenceOperation](-presence-operation/index.md) |
| [PNHereNowOperation](-p-n-here-now-operation/index.md) | [jvm]<br>object [PNHereNowOperation](-p-n-here-now-operation/index.md) : [PNOperationType.PresenceOperation](-presence-operation/index.md) |
| [PNHistoryOperation](-p-n-history-operation/index.md) | [jvm]<br>object [PNHistoryOperation](-p-n-history-operation/index.md) : [PNOperationType.HistoryOperation](-history-operation/index.md) |
| [PNManageMemberships](-p-n-manage-memberships/index.md) | [jvm]<br>object [PNManageMemberships](-p-n-manage-memberships/index.md) : [PNOperationType.ObjectsOperation](-objects-operation/index.md) |
| [PNMessageCountOperation](-p-n-message-count-operation/index.md) | [jvm]<br>object [PNMessageCountOperation](-p-n-message-count-operation/index.md) : [PNOperationType.MessageCountsOperation](-message-counts-operation/index.md) |
| [PNPublishOperation](-p-n-publish-operation/index.md) | [jvm]<br>object [PNPublishOperation](-p-n-publish-operation/index.md) : [PNOperationType.PublishOperation](-publish-operation/index.md) |
| [PNPushNotificationEnabledChannelsOperation](-p-n-push-notification-enabled-channels-operation/index.md) | [jvm]<br>object [PNPushNotificationEnabledChannelsOperation](-p-n-push-notification-enabled-channels-operation/index.md) : [PNOperationType.PushNotificationsOperation](-push-notifications-operation/index.md) |
| [PNRemoveAllPushNotificationsOperation](-p-n-remove-all-push-notifications-operation/index.md) | [jvm]<br>object [PNRemoveAllPushNotificationsOperation](-p-n-remove-all-push-notifications-operation/index.md) : [PNOperationType.PushNotificationsOperation](-push-notifications-operation/index.md) |
| [PNRemoveChannelMetadataOperation](-p-n-remove-channel-metadata-operation/index.md) | [jvm]<br>object [PNRemoveChannelMetadataOperation](-p-n-remove-channel-metadata-operation/index.md) : [PNOperationType.ObjectsOperation](-objects-operation/index.md) |
| [PNRemoveChannelsFromGroupOperation](-p-n-remove-channels-from-group-operation/index.md) | [jvm]<br>object [PNRemoveChannelsFromGroupOperation](-p-n-remove-channels-from-group-operation/index.md) : [PNOperationType.ChannelGroupOperation](-channel-group-operation/index.md) |
| [PNRemoveGroupOperation](-p-n-remove-group-operation/index.md) | [jvm]<br>object [PNRemoveGroupOperation](-p-n-remove-group-operation/index.md) : [PNOperationType.ChannelGroupOperation](-channel-group-operation/index.md) |
| [PNRemovePushNotificationsFromChannelsOperation](-p-n-remove-push-notifications-from-channels-operation/index.md) | [jvm]<br>object [PNRemovePushNotificationsFromChannelsOperation](-p-n-remove-push-notifications-from-channels-operation/index.md) : [PNOperationType.PushNotificationsOperation](-push-notifications-operation/index.md) |
| [PNRemoveUUIDMetadataOperation](-p-n-remove-u-u-i-d-metadata-operation/index.md) | [jvm]<br>object [PNRemoveUUIDMetadataOperation](-p-n-remove-u-u-i-d-metadata-operation/index.md) : [PNOperationType.ObjectsOperation](-objects-operation/index.md) |
| [PNSetChannelMetadataOperation](-p-n-set-channel-metadata-operation/index.md) | [jvm]<br>object [PNSetChannelMetadataOperation](-p-n-set-channel-metadata-operation/index.md) : [PNOperationType.ObjectsOperation](-objects-operation/index.md) |
| [PNSetMembershipsOperation](-p-n-set-memberships-operation/index.md) | [jvm]<br>object [PNSetMembershipsOperation](-p-n-set-memberships-operation/index.md) : [PNOperationType.ObjectsOperation](-objects-operation/index.md) |
| [PNSetStateOperation](-p-n-set-state-operation/index.md) | [jvm]<br>object [PNSetStateOperation](-p-n-set-state-operation/index.md) : [PNOperationType.PresenceOperation](-presence-operation/index.md) |
| [PNSetUUIDMetadataOperation](-p-n-set-u-u-i-d-metadata-operation/index.md) | [jvm]<br>object [PNSetUUIDMetadataOperation](-p-n-set-u-u-i-d-metadata-operation/index.md) : [PNOperationType.ObjectsOperation](-objects-operation/index.md) |
| [PNSignalOperation](-p-n-signal-operation/index.md) | [jvm]<br>object [PNSignalOperation](-p-n-signal-operation/index.md) : [PNOperationType.SignalsOperation](-signals-operation/index.md) |
| [PNSubscribeOperation](-p-n-subscribe-operation/index.md) | [jvm]<br>object [PNSubscribeOperation](-p-n-subscribe-operation/index.md) : [PNOperationType](index.md) |
| [PNTimeOperation](-p-n-time-operation/index.md) | [jvm]<br>object [PNTimeOperation](-p-n-time-operation/index.md) : [PNOperationType.TimeOperation](-time-operation/index.md) |
| [PNUnsubscribeOperation](-p-n-unsubscribe-operation/index.md) | [jvm]<br>object [PNUnsubscribeOperation](-p-n-unsubscribe-operation/index.md) : [PNOperationType.PresenceOperation](-presence-operation/index.md) |
| [PNUpdateMembershipsOperation](-p-n-update-memberships-operation/index.md) | [jvm]<br>object [PNUpdateMembershipsOperation](-p-n-update-memberships-operation/index.md) : [PNOperationType.ObjectsOperation](-objects-operation/index.md) |
| [PNWhereNowOperation](-p-n-where-now-operation/index.md) | [jvm]<br>object [PNWhereNowOperation](-p-n-where-now-operation/index.md) : [PNOperationType.PresenceOperation](-presence-operation/index.md) |
| [PresenceOperation](-presence-operation/index.md) | [jvm]<br>open class [PresenceOperation](-presence-operation/index.md) : [PNOperationType](index.md) |
| [PublishOperation](-publish-operation/index.md) | [jvm]<br>open class [PublishOperation](-publish-operation/index.md) : [PNOperationType](index.md) |
| [PushNotificationsOperation](-push-notifications-operation/index.md) | [jvm]<br>open class [PushNotificationsOperation](-push-notifications-operation/index.md) : [PNOperationType](index.md) |
| [SignalsOperation](-signals-operation/index.md) | [jvm]<br>open class [SignalsOperation](-signals-operation/index.md) : [PNOperationType](index.md) |
| [SpaceOperation](-space-operation/index.md) | [jvm]<br>object [SpaceOperation](-space-operation/index.md) : [PNOperationType](index.md) |
| [TimeOperation](-time-operation/index.md) | [jvm]<br>open class [TimeOperation](-time-operation/index.md) : [PNOperationType](index.md) |
| [UserOperation](-user-operation/index.md) | [jvm]<br>object [UserOperation](-user-operation/index.md) : [PNOperationType](index.md) |

## Properties

| Name | Summary |
|---|---|
| [queryParam](query-param.md) | [jvm]<br>open val [queryParam](query-param.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null |

## Functions

| Name | Summary |
|---|---|
| [toString](to-string.md) | [jvm]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
