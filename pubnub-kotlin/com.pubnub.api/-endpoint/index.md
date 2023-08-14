//[pubnub-kotlin](../../../index.md)/[com.pubnub.api](../index.md)/[Endpoint](index.md)

# Endpoint

[jvm]\
abstract class [Endpoint](index.md)&lt;[Input](index.md), [Output](index.md)&gt; : [ExtendedRemoteAction](../../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;[Output](index.md)&gt; 

Base class for all PubNub API operation implementations.

## Parameters

jvm

| | |
|---|---|
| Input | Server's response. |
| Output | Parsed and encapsulated response for endusers. |

## Functions

| Name | Summary |
|---|---|
| [async](async.md) | [jvm]<br>open override fun [async](async.md)(callback: (result: [Output](index.md)?, status: [PNStatus](../../com.pubnub.api.models.consumer/-p-n-status/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))<br>Executes the call asynchronously. This function does not block the thread. |
| [operationType](../../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/operation-type.md) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/operation-type.md)(): [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](retry.md) | [jvm]<br>open override fun [retry](retry.md)() |
| [silentCancel](silent-cancel.md) | [jvm]<br>open override fun [silentCancel](silent-cancel.md)()<br>Cancel the operation but do not alert anybody, useful for restarting the heartbeats and subscribe loops. |
| [sync](sync.md) | [jvm]<br>open override fun [sync](sync.md)(): [Output](index.md)?<br>Executes the call synchronously. This function blocks the thread. |

## Properties

| Name | Summary |
|---|---|
| [queryParam](query-param.md) | [jvm]<br>val [queryParam](query-param.md): [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;<br>Key-value object to pass with every PubNub API operation. Used for debugging purposes. todo: it should be removed! |

## Inheritors

| Name |
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
| [Heartbeat](../../com.pubnub.api.endpoints.presence/-heartbeat/index.md) |
| [HereNow](../../com.pubnub.api.endpoints.presence/-here-now/index.md) |
| [Leave](../../com.pubnub.api.endpoints.presence/-leave/index.md) |
| [SetState](../../com.pubnub.api.endpoints.presence/-set-state/index.md) |
| [WhereNow](../../com.pubnub.api.endpoints.presence/-where-now/index.md) |
| [Publish](../../com.pubnub.api.endpoints.pubsub/-publish/index.md) |
| [Signal](../../com.pubnub.api.endpoints.pubsub/-signal/index.md) |
| [Subscribe](../../com.pubnub.api.endpoints.pubsub/-subscribe/index.md) |
| [AddChannelsToPush](../../com.pubnub.api.endpoints.push/-add-channels-to-push/index.md) |
| [ListPushProvisions](../../com.pubnub.api.endpoints.push/-list-push-provisions/index.md) |
| [RemoveAllPushChannelsForDevice](../../com.pubnub.api.endpoints.push/-remove-all-push-channels-for-device/index.md) |
| [RemoveChannelsFromPush](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md) |
