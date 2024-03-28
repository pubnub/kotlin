//[pubnub-kotlin](../../../index.md)/[com.pubnub.api](../index.md)/[Endpoint](index.md)

# Endpoint

interface [Endpoint](index.md)&lt;[OUTPUT](index.md)&gt; : [ExtendedRemoteAction](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;[OUTPUT](index.md)&gt; 

#### Inheritors

| |
|---|
| [DeleteMessages](../../com.pubnub.api.endpoints/-delete-messages/index.md) |
| [FetchMessages](../../com.pubnub.api.endpoints/-fetch-messages/index.md) |
| [History](../../com.pubnub.api.endpoints/-history/index.md) |
| [MessageCounts](../../com.pubnub.api.endpoints/-message-counts/index.md) |
| [Time](../../com.pubnub.api.endpoints/-time/index.md) |
| [Grant](../../com.pubnub.api.endpoints.access/-grant/index.md) |
| [GrantToken](../../com.pubnub.api.endpoints.access/-grant-token/index.md) |
| [RevokeToken](../../com.pubnub.api.endpoints.access/-revoke-token/index.md) |
| [AddChannelChannelGroup](../../com.pubnub.api.endpoints.channel_groups/-add-channel-channel-group/index.md) |
| [AllChannelsChannelGroup](../../com.pubnub.api.endpoints.channel_groups/-all-channels-channel-group/index.md) |
| [DeleteChannelGroup](../../com.pubnub.api.endpoints.channel_groups/-delete-channel-group/index.md) |
| [ListAllChannelGroup](../../com.pubnub.api.endpoints.channel_groups/-list-all-channel-group/index.md) |
| [RemoveChannelChannelGroup](../../com.pubnub.api.endpoints.channel_groups/-remove-channel-channel-group/index.md) |
| [DeleteFile](../../com.pubnub.api.endpoints.files/-delete-file/index.md) |
| [DownloadFile](../../com.pubnub.api.endpoints.files/-download-file/index.md) |
| [GetFileUrl](../../com.pubnub.api.endpoints.files/-get-file-url/index.md) |
| [ListFiles](../../com.pubnub.api.endpoints.files/-list-files/index.md) |
| [PublishFileMessage](../../com.pubnub.api.endpoints.files/-publish-file-message/index.md) |
| [SendFile](../../com.pubnub.api.endpoints.files/-send-file/index.md) |
| [AddMessageAction](../../com.pubnub.api.endpoints.message_actions/-add-message-action/index.md) |
| [GetMessageActions](../../com.pubnub.api.endpoints.message_actions/-get-message-actions/index.md) |
| [RemoveMessageAction](../../com.pubnub.api.endpoints.message_actions/-remove-message-action/index.md) |
| [GetAllChannelMetadata](../../com.pubnub.api.endpoints.objects.channel/-get-all-channel-metadata/index.md) |
| [GetChannelMetadata](../../com.pubnub.api.endpoints.objects.channel/-get-channel-metadata/index.md) |
| [RemoveChannelMetadata](../../com.pubnub.api.endpoints.objects.channel/-remove-channel-metadata/index.md) |
| [SetChannelMetadata](../../com.pubnub.api.endpoints.objects.channel/-set-channel-metadata/index.md) |
| [GetChannelMembers](../../com.pubnub.api.endpoints.objects.member/-get-channel-members/index.md) |
| [ManageChannelMembers](../../com.pubnub.api.endpoints.objects.member/-manage-channel-members/index.md) |
| [GetMemberships](../../com.pubnub.api.endpoints.objects.membership/-get-memberships/index.md) |
| [ManageMemberships](../../com.pubnub.api.endpoints.objects.membership/-manage-memberships/index.md) |
| [GetAllUUIDMetadata](../../com.pubnub.api.endpoints.objects.uuid/-get-all-u-u-i-d-metadata/index.md) |
| [GetUUIDMetadata](../../com.pubnub.api.endpoints.objects.uuid/-get-u-u-i-d-metadata/index.md) |
| [RemoveUUIDMetadata](../../com.pubnub.api.endpoints.objects.uuid/-remove-u-u-i-d-metadata/index.md) |
| [SetUUIDMetadata](../../com.pubnub.api.endpoints.objects.uuid/-set-u-u-i-d-metadata/index.md) |
| [GetState](../../com.pubnub.api.endpoints.presence/-get-state/index.md) |
| [HereNow](../../com.pubnub.api.endpoints.presence/-here-now/index.md) |
| [SetState](../../com.pubnub.api.endpoints.presence/-set-state/index.md) |
| [WhereNow](../../com.pubnub.api.endpoints.presence/-where-now/index.md) |
| [Publish](../../com.pubnub.api.endpoints.pubsub/-publish/index.md) |
| [Signal](../../com.pubnub.api.endpoints.pubsub/-signal/index.md) |
| [AddChannelsToPush](../../com.pubnub.api.endpoints.push/-add-channels-to-push/index.md) |
| [ListPushProvisions](../../com.pubnub.api.endpoints.push/-list-push-provisions/index.md) |
| [RemoveAllPushChannelsForDevice](../../com.pubnub.api.endpoints.push/-remove-all-push-channels-for-device/index.md) |
| [RemoveChannelsFromPush](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md) |

## Functions

| Name | Summary |
|---|---|
| [async](index.md#149557464%2FFunctions%2F51989805) | [jvm]<br>abstract fun [async](index.md#149557464%2FFunctions%2F51989805)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../pubnub-gson/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[OUTPUT](index.md)&gt;&gt;) |
| [operationType](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#1414065386%2FFunctions%2F51989805) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#1414065386%2FFunctions%2F51989805)(): [PNOperationType](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#2020801116%2FFunctions%2F51989805) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#2020801116%2FFunctions%2F51989805)() |
| [silentCancel](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#-675955969%2FFunctions%2F51989805) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#-675955969%2FFunctions%2F51989805)() |
| [sync](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#40193115%2FFunctions%2F51989805) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md#40193115%2FFunctions%2F51989805)(): [OUTPUT](index.md) |
