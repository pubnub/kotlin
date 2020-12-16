[pubnub-kotlin](../../index.md) / [com.pubnub.api.enums](../index.md) / [PNOperationType](./index.md)

# PNOperationType

`sealed class PNOperationType`

### Types

| Name | Summary |
|---|---|
| [ChannelGroupOperation](-channel-group-operation/index.md) | `open class ChannelGroupOperation : `[`PNOperationType`](./index.md) |
| [FileOperation](-file-operation.md) | `object FileOperation : `[`PNOperationType`](./index.md) |
| [HistoryOperation](-history-operation/index.md) | `open class HistoryOperation : `[`PNOperationType`](./index.md) |
| [MessageActionsOperation](-message-actions-operation/index.md) | `open class MessageActionsOperation : `[`PNOperationType`](./index.md) |
| [MessageCountsOperation](-message-counts-operation/index.md) | `open class MessageCountsOperation : `[`PNOperationType`](./index.md) |
| [ObjectsOperation](-objects-operation/index.md) | `open class ObjectsOperation : `[`PNOperationType`](./index.md) |
| [PAMOperation](-p-a-m-operation/index.md) | `open class PAMOperation : `[`PNOperationType`](./index.md) |
| [PAMV3Operation](-p-a-m-v3-operation/index.md) | `open class PAMV3Operation : `[`PNOperationType`](./index.md) |
| [PNAccessManagerAudit](-p-n-access-manager-audit.md) | `object PNAccessManagerAudit : PAMOperation` |
| [PNAccessManagerGrant](-p-n-access-manager-grant.md) | `object PNAccessManagerGrant : PAMOperation` |
| [PNAccessManagerGrantToken](-p-n-access-manager-grant-token.md) | `object PNAccessManagerGrantToken : PAMV3Operation` |
| [PNAddChannelsToGroupOperation](-p-n-add-channels-to-group-operation.md) | `object PNAddChannelsToGroupOperation : ChannelGroupOperation` |
| [PNAddMessageAction](-p-n-add-message-action.md) | `object PNAddMessageAction : MessageActionsOperation` |
| [PNAddPushNotificationsOnChannelsOperation](-p-n-add-push-notifications-on-channels-operation.md) | `object PNAddPushNotificationsOnChannelsOperation : PushNotificationsOperation` |
| [PNChannelGroupsOperation](-p-n-channel-groups-operation.md) | `object PNChannelGroupsOperation : ChannelGroupOperation` |
| [PNChannelsForGroupOperation](-p-n-channels-for-group-operation.md) | `object PNChannelsForGroupOperation : ChannelGroupOperation` |
| [PNDeleteMessageAction](-p-n-delete-message-action.md) | `object PNDeleteMessageAction : MessageActionsOperation` |
| [PNDeleteMessagesOperation](-p-n-delete-messages-operation.md) | `object PNDeleteMessagesOperation : HistoryOperation` |
| [PNFetchMessagesOperation](-p-n-fetch-messages-operation.md) | `object PNFetchMessagesOperation : HistoryOperation` |
| [PNGetAllChannelsMetadataOperation](-p-n-get-all-channels-metadata-operation.md) | `object PNGetAllChannelsMetadataOperation : ObjectsOperation` |
| [PNGetAllUUIDMetadataOperation](-p-n-get-all-u-u-i-d-metadata-operation.md) | `object PNGetAllUUIDMetadataOperation : ObjectsOperation` |
| [PNGetChannelMetadataOperation](-p-n-get-channel-metadata-operation.md) | `object PNGetChannelMetadataOperation : ObjectsOperation` |
| [PNGetMembershipsOperation](-p-n-get-memberships-operation.md) | `object PNGetMembershipsOperation : ObjectsOperation` |
| [PNGetMessageActions](-p-n-get-message-actions.md) | `object PNGetMessageActions : MessageActionsOperation` |
| [PNGetState](-p-n-get-state.md) | `object PNGetState : PresenceOperation` |
| [PNGetUUIDMetadataOperation](-p-n-get-u-u-i-d-metadata-operation.md) | `object PNGetUUIDMetadataOperation : ObjectsOperation` |
| [PNHeartbeatOperation](-p-n-heartbeat-operation.md) | `object PNHeartbeatOperation : PresenceOperation` |
| [PNHereNowOperation](-p-n-here-now-operation.md) | `object PNHereNowOperation : PresenceOperation` |
| [PNHistoryOperation](-p-n-history-operation.md) | `object PNHistoryOperation : HistoryOperation` |
| [PNManageMemberships](-p-n-manage-memberships.md) | `object PNManageMemberships : ObjectsOperation` |
| [PNMessageCountOperation](-p-n-message-count-operation.md) | `object PNMessageCountOperation : MessageCountsOperation` |
| [PNPublishOperation](-p-n-publish-operation.md) | `object PNPublishOperation : PublishOperation` |
| [PNPushNotificationEnabledChannelsOperation](-p-n-push-notification-enabled-channels-operation.md) | `object PNPushNotificationEnabledChannelsOperation : PushNotificationsOperation` |
| [PNRemoveAllPushNotificationsOperation](-p-n-remove-all-push-notifications-operation.md) | `object PNRemoveAllPushNotificationsOperation : PushNotificationsOperation` |
| [PNRemoveChannelMetadataOperation](-p-n-remove-channel-metadata-operation.md) | `object PNRemoveChannelMetadataOperation : ObjectsOperation` |
| [PNRemoveChannelsFromGroupOperation](-p-n-remove-channels-from-group-operation.md) | `object PNRemoveChannelsFromGroupOperation : ChannelGroupOperation` |
| [PNRemoveGroupOperation](-p-n-remove-group-operation.md) | `object PNRemoveGroupOperation : ChannelGroupOperation` |
| [PNRemovePushNotificationsFromChannelsOperation](-p-n-remove-push-notifications-from-channels-operation.md) | `object PNRemovePushNotificationsFromChannelsOperation : PushNotificationsOperation` |
| [PNRemoveUUIDMetadataOperation](-p-n-remove-u-u-i-d-metadata-operation.md) | `object PNRemoveUUIDMetadataOperation : ObjectsOperation` |
| [PNSetChannelMetadataOperation](-p-n-set-channel-metadata-operation.md) | `object PNSetChannelMetadataOperation : ObjectsOperation` |
| [PNSetMembershipsOperation](-p-n-set-memberships-operation.md) | `object PNSetMembershipsOperation : ObjectsOperation` |
| [PNSetStateOperation](-p-n-set-state-operation.md) | `object PNSetStateOperation : PresenceOperation` |
| [PNSetUUIDMetadataOperation](-p-n-set-u-u-i-d-metadata-operation.md) | `object PNSetUUIDMetadataOperation : ObjectsOperation` |
| [PNSignalOperation](-p-n-signal-operation.md) | `object PNSignalOperation : SignalsOperation` |
| [PNSubscribeOperation](-p-n-subscribe-operation.md) | `object PNSubscribeOperation : `[`PNOperationType`](./index.md) |
| [PNTimeOperation](-p-n-time-operation.md) | `object PNTimeOperation : TimeOperation` |
| [PNUnsubscribeOperation](-p-n-unsubscribe-operation.md) | `object PNUnsubscribeOperation : PresenceOperation` |
| [PNUpdateMembershipsOperation](-p-n-update-memberships-operation.md) | `object PNUpdateMembershipsOperation : ObjectsOperation` |
| [PNWhereNowOperation](-p-n-where-now-operation.md) | `object PNWhereNowOperation : PresenceOperation` |
| [PresenceOperation](-presence-operation/index.md) | `open class PresenceOperation : `[`PNOperationType`](./index.md) |
| [PublishOperation](-publish-operation/index.md) | `open class PublishOperation : `[`PNOperationType`](./index.md) |
| [PushNotificationsOperation](-push-notifications-operation/index.md) | `open class PushNotificationsOperation : `[`PNOperationType`](./index.md) |
| [SignalsOperation](-signals-operation/index.md) | `open class SignalsOperation : `[`PNOperationType`](./index.md) |
| [TimeOperation](-time-operation/index.md) | `open class TimeOperation : `[`PNOperationType`](./index.md) |

### Properties

| Name | Summary |
|---|---|
| [queryParam](query-param.md) | `open val queryParam: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [toString](to-string.md) | `open fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
