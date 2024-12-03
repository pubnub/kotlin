//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.endpoints](../index.md)/[Endpoint](index.md)

# Endpoint

interface [Endpoint](index.md)&lt;[T](index.md)&gt; : [ExtendedRemoteAction](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;[T](index.md)&gt; 

#### Inheritors

| |
|---|
| [FetchMessages](../-fetch-messages/index.md) |
| [DeleteMessages](../-delete-messages/index.md) |
| [MessageCounts](../-message-counts/index.md) |
| [History](../-history/index.md) |
| [GrantToken](../../com.pubnub.api.java.endpoints.access/-grant-token/index.md) |
| [Grant](../../com.pubnub.api.java.endpoints.access/-grant/index.md) |
| [RevokeToken](../../com.pubnub.api.java.endpoints.access/-revoke-token/index.md) |
| [AbstractGrantTokenBuilder](../../com.pubnub.api.java.endpoints.access.builder/-abstract-grant-token-builder/index.md) |
| [GrantTokenBuilder](../../com.pubnub.api.java.endpoints.access.builder/-grant-token-builder/index.md) |
| [GrantTokenObjectsBuilder](../../com.pubnub.api.java.endpoints.access.builder/-grant-token-objects-builder/index.md) |
| [GrantTokenEntitiesBuilder](../../com.pubnub.api.java.endpoints.access.builder/-grant-token-entities-builder/index.md) |
| [RemoveAllPushChannelsForDevice](../../com.pubnub.api.java.endpoints.push/-remove-all-push-channels-for-device/index.md) |
| [ListPushProvisions](../../com.pubnub.api.java.endpoints.push/-list-push-provisions/index.md) |
| [AddChannelsToPush](../../com.pubnub.api.java.endpoints.push/-add-channels-to-push/index.md) |
| [RemoveChannelsFromPush](../../com.pubnub.api.java.endpoints.push/-remove-channels-from-push/index.md) |
| [SetState](../../com.pubnub.api.java.endpoints.presence/-set-state/index.md) |
| [Leave](../../com.pubnub.api.java.endpoints.presence/-leave/index.md) |
| [WhereNow](../../com.pubnub.api.java.endpoints.presence/-where-now/index.md) |
| [HereNow](../../com.pubnub.api.java.endpoints.presence/-here-now/index.md) |
| [Heartbeat](../../com.pubnub.api.java.endpoints.presence/-heartbeat/index.md) |
| [GetState](../../com.pubnub.api.java.endpoints.presence/-get-state/index.md) |
| [RemoveMessageAction](../../com.pubnub.api.java.endpoints.message_actions/-remove-message-action/index.md) |
| [GetMessageActions](../../com.pubnub.api.java.endpoints.message_actions/-get-message-actions/index.md) |
| [AddMessageAction](../../com.pubnub.api.java.endpoints.message_actions/-add-message-action/index.md) |
| [GetAllChannelsMetadata](../../com.pubnub.api.java.endpoints.objects_api.channel/-get-all-channels-metadata/index.md) |
| [GetChannelMetadata](../../com.pubnub.api.java.endpoints.objects_api.channel/-get-channel-metadata/index.md) |
| [RemoveChannelMetadata](../../com.pubnub.api.java.endpoints.objects_api.channel/-remove-channel-metadata/index.md) |
| [SetChannelMetadata](../../com.pubnub.api.java.endpoints.objects_api.channel/-set-channel-metadata/index.md) |
| [SetMemberships](../../com.pubnub.api.java.endpoints.objects_api.memberships/-set-memberships/index.md) |
| [ManageMemberships](../../com.pubnub.api.java.endpoints.objects_api.memberships/-manage-memberships/index.md) |
| [GetMemberships](../../com.pubnub.api.java.endpoints.objects_api.memberships/-get-memberships/index.md) |
| [RemoveMemberships](../../com.pubnub.api.java.endpoints.objects_api.memberships/-remove-memberships/index.md) |
| [ManageChannelMembers](../../com.pubnub.api.java.endpoints.objects_api.members/-manage-channel-members/index.md) |
| [GetChannelMembers](../../com.pubnub.api.java.endpoints.objects_api.members/-get-channel-members/index.md) |
| [SetChannelMembers](../../com.pubnub.api.java.endpoints.objects_api.members/-set-channel-members/index.md) |
| [RemoveChannelMembers](../../com.pubnub.api.java.endpoints.objects_api.members/-remove-channel-members/index.md) |
| [RemoveUUIDMetadata](../../com.pubnub.api.java.endpoints.objects_api.uuid/-remove-u-u-i-d-metadata/index.md) |
| [GetUUIDMetadata](../../com.pubnub.api.java.endpoints.objects_api.uuid/-get-u-u-i-d-metadata/index.md) |
| [SetUUIDMetadata](../../com.pubnub.api.java.endpoints.objects_api.uuid/-set-u-u-i-d-metadata/index.md) |
| [GetAllUUIDMetadata](../../com.pubnub.api.java.endpoints.objects_api.uuid/-get-all-u-u-i-d-metadata/index.md) |
| [GetFileUrl](../../com.pubnub.api.java.endpoints.files/-get-file-url/index.md) |
| [ListFiles](../../com.pubnub.api.java.endpoints.files/-list-files/index.md) |
| [DeleteFile](../../com.pubnub.api.java.endpoints.files/-delete-file/index.md) |
| [DownloadFile](../../com.pubnub.api.java.endpoints.files/-download-file/index.md) |
| [PublishFileMessage](../../com.pubnub.api.java.endpoints.files/-publish-file-message/index.md) |
| [Publish](../../com.pubnub.api.java.endpoints.pubsub/-publish/index.md) |
| [Signal](../../com.pubnub.api.java.endpoints.pubsub/-signal/index.md) |
| [DeleteChannelGroup](../../com.pubnub.api.java.endpoints.channel_groups/-delete-channel-group/index.md) |
| [AllChannelsChannelGroup](../../com.pubnub.api.java.endpoints.channel_groups/-all-channels-channel-group/index.md) |
| [AddChannelChannelGroup](../../com.pubnub.api.java.endpoints.channel_groups/-add-channel-channel-group/index.md) |
| [RemoveChannelChannelGroup](../../com.pubnub.api.java.endpoints.channel_groups/-remove-channel-channel-group/index.md) |
| [PublishBuilder](../../com.pubnub.api.java.v2.endpoints.pubsub/-publish-builder/index.md) |
| [SignalBuilder](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md) |

## Functions

| Name | Summary |
|---|---|
| [async](index.md#149557464%2FFunctions%2F126356644) | [jvm]<br>abstract override fun [async](index.md#149557464%2FFunctions%2F126356644)(callback: [Consumer](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.callbacks/-consumer/index.md)&lt;[Result](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[T](index.md)&gt;&gt;) |
| [operationType](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#1414065386%2FFunctions%2F126356644) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#1414065386%2FFunctions%2F126356644)(): [PNOperationType](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [overrideConfiguration](override-configuration.md) | [jvm]<br>abstract fun [overrideConfiguration](override-configuration.md)(configuration: [PNConfiguration](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](index.md)&lt;[T](index.md)&gt;<br>Allows to override certain configuration options (see [com.pubnub.api.v2.PNConfigurationOverride.Builder](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md)) for this request only. |
| [retry](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#2020801116%2FFunctions%2F126356644) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#2020801116%2FFunctions%2F126356644)() |
| [silentCancel](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#-675955969%2FFunctions%2F126356644) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#-675955969%2FFunctions%2F126356644)() |
| [sync](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#40193115%2FFunctions%2F126356644) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#40193115%2FFunctions%2F126356644)(): [T](index.md) |
