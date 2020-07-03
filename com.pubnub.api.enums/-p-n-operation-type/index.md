---
title: PNOperationType - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.enums](../index.html) / [PNOperationType](./index.html)

# PNOperationType

`sealed class PNOperationType`

### Types

| [ChannelGroupOperation](-channel-group-operation/index.html) | `open class ChannelGroupOperation : `[`PNOperationType`](./index.html) |
| [HistoryOperation](-history-operation/index.html) | `open class HistoryOperation : `[`PNOperationType`](./index.html) |
| [MessageActionsOperation](-message-actions-operation/index.html) | `open class MessageActionsOperation : `[`PNOperationType`](./index.html) |
| [MessageCountsOperation](-message-counts-operation/index.html) | `open class MessageCountsOperation : `[`PNOperationType`](./index.html) |
| [ObjectsOperation](-objects-operation/index.html) | `open class ObjectsOperation : `[`PNOperationType`](./index.html) |
| [PAMOperation](-p-a-m-operation/index.html) | `open class PAMOperation : `[`PNOperationType`](./index.html) |
| [PAMV3Operation](-p-a-m-v3-operation/index.html) | `open class PAMV3Operation : `[`PNOperationType`](./index.html) |
| [PNAccessManagerAudit](-p-n-access-manager-audit.html) | `object PNAccessManagerAudit : PAMOperation` |
| [PNAccessManagerGrant](-p-n-access-manager-grant.html) | `object PNAccessManagerGrant : PAMOperation` |
| [PNAccessManagerGrantToken](-p-n-access-manager-grant-token.html) | `object PNAccessManagerGrantToken : PAMV3Operation` |
| [PNAddChannelsToGroupOperation](-p-n-add-channels-to-group-operation.html) | `object PNAddChannelsToGroupOperation : ChannelGroupOperation` |
| [PNAddMessageAction](-p-n-add-message-action.html) | `object PNAddMessageAction : MessageActionsOperation` |
| [PNAddPushNotificationsOnChannelsOperation](-p-n-add-push-notifications-on-channels-operation.html) | `object PNAddPushNotificationsOnChannelsOperation : PushNotificationsOperation` |
| [PNChannelGroupsOperation](-p-n-channel-groups-operation.html) | `object PNChannelGroupsOperation : ChannelGroupOperation` |
| [PNChannelsForGroupOperation](-p-n-channels-for-group-operation.html) | `object PNChannelsForGroupOperation : ChannelGroupOperation` |
| [PNCreateSpaceOperation](-p-n-create-space-operation.html) | `object PNCreateSpaceOperation : ObjectsOperation` |
| [PNCreateUserOperation](-p-n-create-user-operation.html) | `object PNCreateUserOperation : ObjectsOperation` |
| [PNDeleteMessageAction](-p-n-delete-message-action.html) | `object PNDeleteMessageAction : MessageActionsOperation` |
| [PNDeleteMessagesOperation](-p-n-delete-messages-operation.html) | `object PNDeleteMessagesOperation : HistoryOperation` |
| [PNDeleteSpaceOperation](-p-n-delete-space-operation.html) | `object PNDeleteSpaceOperation : ObjectsOperation` |
| [PNDeleteUserOperation](-p-n-delete-user-operation.html) | `object PNDeleteUserOperation : ObjectsOperation` |
| [PNFetchMessagesOperation](-p-n-fetch-messages-operation.html) | `object PNFetchMessagesOperation : HistoryOperation` |
| [PNGetMembers](-p-n-get-members.html) | `object PNGetMembers : ObjectsOperation` |
| [PNGetMemberships](-p-n-get-memberships.html) | `object PNGetMemberships : ObjectsOperation` |
| [PNGetMessageActions](-p-n-get-message-actions.html) | `object PNGetMessageActions : MessageActionsOperation` |
| [PNGetSpaceOperation](-p-n-get-space-operation.html) | `object PNGetSpaceOperation : ObjectsOperation` |
| [PNGetSpacesOperation](-p-n-get-spaces-operation.html) | `object PNGetSpacesOperation : ObjectsOperation` |
| [PNGetState](-p-n-get-state.html) | `object PNGetState : PresenceOperation` |
| [PNGetUserOperation](-p-n-get-user-operation.html) | `object PNGetUserOperation : ObjectsOperation` |
| [PNGetUsersOperation](-p-n-get-users-operation.html) | `object PNGetUsersOperation : ObjectsOperation` |
| [PNHeartbeatOperation](-p-n-heartbeat-operation.html) | `object PNHeartbeatOperation : PresenceOperation` |
| [PNHereNowOperation](-p-n-here-now-operation.html) | `object PNHereNowOperation : PresenceOperation` |
| [PNHistoryOperation](-p-n-history-operation.html) | `object PNHistoryOperation : HistoryOperation` |
| [PNManageMembers](-p-n-manage-members.html) | `object PNManageMembers : ObjectsOperation` |
| [PNManageMemberships](-p-n-manage-memberships.html) | `object PNManageMemberships : ObjectsOperation` |
| [PNMessageCountOperation](-p-n-message-count-operation.html) | `object PNMessageCountOperation : MessageCountsOperation` |
| [PNPublishOperation](-p-n-publish-operation.html) | `object PNPublishOperation : PublishOperation` |
| [PNPushNotificationEnabledChannelsOperation](-p-n-push-notification-enabled-channels-operation.html) | `object PNPushNotificationEnabledChannelsOperation : PushNotificationsOperation` |
| [PNRemoveAllPushNotificationsOperation](-p-n-remove-all-push-notifications-operation.html) | `object PNRemoveAllPushNotificationsOperation : PushNotificationsOperation` |
| [PNRemoveChannelsFromGroupOperation](-p-n-remove-channels-from-group-operation.html) | `object PNRemoveChannelsFromGroupOperation : ChannelGroupOperation` |
| [PNRemoveGroupOperation](-p-n-remove-group-operation.html) | `object PNRemoveGroupOperation : ChannelGroupOperation` |
| [PNRemovePushNotificationsFromChannelsOperation](-p-n-remove-push-notifications-from-channels-operation.html) | `object PNRemovePushNotificationsFromChannelsOperation : PushNotificationsOperation` |
| [PNSetStateOperation](-p-n-set-state-operation.html) | `object PNSetStateOperation : PresenceOperation` |
| [PNSignalOperation](-p-n-signal-operation.html) | `object PNSignalOperation : SignalsOperation` |
| [PNSubscribeOperation](-p-n-subscribe-operation.html) | `object PNSubscribeOperation : `[`PNOperationType`](./index.html) |
| [PNTimeOperation](-p-n-time-operation.html) | `object PNTimeOperation : TimeOperation` |
| [PNUnsubscribeOperation](-p-n-unsubscribe-operation.html) | `object PNUnsubscribeOperation : PresenceOperation` |
| [PNUpdateSpaceOperation](-p-n-update-space-operation.html) | `object PNUpdateSpaceOperation : ObjectsOperation` |
| [PNUpdateUserOperation](-p-n-update-user-operation.html) | `object PNUpdateUserOperation : ObjectsOperation` |
| [PNWhereNowOperation](-p-n-where-now-operation.html) | `object PNWhereNowOperation : PresenceOperation` |
| [PresenceOperation](-presence-operation/index.html) | `open class PresenceOperation : `[`PNOperationType`](./index.html) |
| [PublishOperation](-publish-operation/index.html) | `open class PublishOperation : `[`PNOperationType`](./index.html) |
| [PushNotificationsOperation](-push-notifications-operation/index.html) | `open class PushNotificationsOperation : `[`PNOperationType`](./index.html) |
| [SignalsOperation](-signals-operation/index.html) | `open class SignalsOperation : `[`PNOperationType`](./index.html) |
| [TimeOperation](-time-operation/index.html) | `open class TimeOperation : `[`PNOperationType`](./index.html) |

### Properties

| [queryParam](query-param.html) | `open val queryParam: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |

### Functions

| [toString](to-string.html) | `open fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

