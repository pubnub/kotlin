//[pubnub-gson](../../../index.md)/[com.pubnub.api.endpoints](../index.md)/[Endpoint](index.md)

# Endpoint

interface [Endpoint](index.md)&lt;[T](index.md)&gt; : [ExtendedRemoteAction](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;[Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&gt; 

#### Inheritors

| |
|---|
| [RemoveChannelsFromPush](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md) |
| [RemoveAllPushChannelsForDevice](../../com.pubnub.api.endpoints.push/-remove-all-push-channels-for-device/index.md) |
| [ListPushProvisions](../../com.pubnub.api.endpoints.push/-list-push-provisions/index.md) |
| [AddChannelsToPush](../../com.pubnub.api.endpoints.push/-add-channels-to-push/index.md) |
| [RemoveChannelChannelGroup](../../com.pubnub.api.endpoints.channel_groups/-remove-channel-channel-group/index.md) |
| [ListAllChannelGroup](../../com.pubnub.api.endpoints.channel_groups/-list-all-channel-group/index.md) |
| [AddChannelChannelGroup](../../com.pubnub.api.endpoints.channel_groups/-add-channel-channel-group/index.md) |
| [AllChannelsChannelGroup](../../com.pubnub.api.endpoints.channel_groups/-all-channels-channel-group/index.md) |
| [DeleteChannelGroup](../../com.pubnub.api.endpoints.channel_groups/-delete-channel-group/index.md) |
| [SetMemberships](../../com.pubnub.api.endpoints.objects_api.memberships/-set-memberships/index.md) |
| [ManageMemberships](../../com.pubnub.api.endpoints.objects_api.memberships/-manage-memberships/index.md) |
| [GetMemberships](../../com.pubnub.api.endpoints.objects_api.memberships/-get-memberships/index.md) |
| [RemoveMemberships](../../com.pubnub.api.endpoints.objects_api.memberships/-remove-memberships/index.md) |
| [GetUUIDMetadata](../../com.pubnub.api.endpoints.objects_api.uuid/-get-u-u-i-d-metadata/index.md) |
| [SetUUIDMetadata](../../com.pubnub.api.endpoints.objects_api.uuid/-set-u-u-i-d-metadata/index.md) |
| [GetAllUUIDMetadata](../../com.pubnub.api.endpoints.objects_api.uuid/-get-all-u-u-i-d-metadata/index.md) |
| [RemoveUUIDMetadata](../../com.pubnub.api.endpoints.objects_api.uuid/-remove-u-u-i-d-metadata/index.md) |
| [GetChannelMembers](../../com.pubnub.api.endpoints.objects_api.members/-get-channel-members/index.md) |
| [RemoveChannelMembers](../../com.pubnub.api.endpoints.objects_api.members/-remove-channel-members/index.md) |
| [SetChannelMembers](../../com.pubnub.api.endpoints.objects_api.members/-set-channel-members/index.md) |
| [ManageChannelMembers](../../com.pubnub.api.endpoints.objects_api.members/-manage-channel-members/index.md) |
| [SetChannelMetadata](../../com.pubnub.api.endpoints.objects_api.channel/-set-channel-metadata/index.md) |
| [RemoveChannelMetadata](../../com.pubnub.api.endpoints.objects_api.channel/-remove-channel-metadata/index.md) |
| [GetChannelMetadata](../../com.pubnub.api.endpoints.objects_api.channel/-get-channel-metadata/index.md) |
| [GetAllChannelsMetadata](../../com.pubnub.api.endpoints.objects_api.channel/-get-all-channels-metadata/index.md) |
| [History](../-history/index.md) |
| [FetchMessages](../-fetch-messages/index.md) |
| [MessageCounts](../-message-counts/index.md) |
| [DeleteMessages](../-delete-messages/index.md) |
| [Time](../-time/index.md) |
| [GetMessageActions](../../com.pubnub.api.endpoints.message_actions/-get-message-actions/index.md) |
| [AddMessageAction](../../com.pubnub.api.endpoints.message_actions/-add-message-action/index.md) |
| [RemoveMessageAction](../../com.pubnub.api.endpoints.message_actions/-remove-message-action/index.md) |
| [Signal](../../com.pubnub.api.endpoints.pubsub/-signal/index.md) |
| [Publish](../../com.pubnub.api.endpoints.pubsub/-publish/index.md) |
| [GrantToken](../../com.pubnub.api.endpoints.access/-grant-token/index.md) |
| [RevokeToken](../../com.pubnub.api.endpoints.access/-revoke-token/index.md) |
| [Grant](../../com.pubnub.api.endpoints.access/-grant/index.md) |
| [WhereNow](../../com.pubnub.api.endpoints.presence/-where-now/index.md) |
| [Heartbeat](../../com.pubnub.api.endpoints.presence/-heartbeat/index.md) |
| [HereNow](../../com.pubnub.api.endpoints.presence/-here-now/index.md) |
| [SetState](../../com.pubnub.api.endpoints.presence/-set-state/index.md) |
| [Leave](../../com.pubnub.api.endpoints.presence/-leave/index.md) |
| [GetState](../../com.pubnub.api.endpoints.presence/-get-state/index.md) |
| [GetFileUrl](../../com.pubnub.api.endpoints.files/-get-file-url/index.md) |
| [ListFiles](../../com.pubnub.api.endpoints.files/-list-files/index.md) |
| [DeleteFile](../../com.pubnub.api.endpoints.files/-delete-file/index.md) |
| [PublishFileMessage](../../com.pubnub.api.endpoints.files/-publish-file-message/index.md) |
| [SendFile](../../com.pubnub.api.endpoints.files/-send-file/index.md) |
| [DownloadFile](../../com.pubnub.api.endpoints.files/-download-file/index.md) |

## Functions

| Name | Summary |
|---|---|
| [async](../../com.pubnub.api.endpoints.files/-download-file/index.md#1418965989%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [async](../../com.pubnub.api.endpoints.files/-download-file/index.md#1418965989%2FFunctions%2F-395131529)(p: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../pubnub-gson/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&gt;&gt;) |
| [operationType](../../com.pubnub.api.endpoints.files/-download-file/index.md#1414065386%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.files/-download-file/index.md#1414065386%2FFunctions%2F-395131529)(): [PNOperationType](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](../../com.pubnub.api.endpoints.files/-download-file/index.md#2020801116%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.files/-download-file/index.md#2020801116%2FFunctions%2F-395131529)() |
| [silentCancel](../../com.pubnub.api.endpoints.files/-download-file/index.md#-675955969%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.files/-download-file/index.md#-675955969%2FFunctions%2F-395131529)() |
| [sync](../../com.pubnub.api.endpoints.files/-download-file/index.md#40193115%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.files/-download-file/index.md#40193115%2FFunctions%2F-395131529)(): [Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md) |
