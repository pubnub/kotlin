//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[Endpoint](index.md)

# Endpoint

interface [Endpoint](index.md)&lt;[OUTPUT](index.md)&gt; : [ExtendedRemoteAction](../../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;[OUTPUT](index.md)&gt; 

#### Inheritors

| |
|---|
| [History](../../com.pubnub.api.endpoints/-history/index.md) |
| [DeleteMessages](../../com.pubnub.api.endpoints/-delete-messages/index.md) |
| [FetchMessages](../../com.pubnub.api.endpoints/-fetch-messages/index.md) |
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
| [alsoAsync](../../com.pubnub.kmp/also-async.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/also-async.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/also-async.md)&gt;.[alsoAsync](../../com.pubnub.kmp/also-async.md)(action: ([T](../../com.pubnub.kmp/also-async.md)) -&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;*&gt;): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/also-async.md)&gt;<br>Execute a second PNFuture after this PNFuture completes successfully, and return the *original* value of this PNFuture after the second PNFuture completes successfully. |
| [async](index.md#149557464%2FFunctions%2F-167468485) | [jvm]<br>abstract override fun [async](index.md#149557464%2FFunctions%2F-167468485)(callback: [Consumer](../../com.pubnub.api.v2.callbacks/-consumer/index.md)&lt;[Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[OUTPUT](index.md)&gt;&gt;)<br>Run the action asynchronously, without blocking the calling thread and delivering the result through the [callback](index.md#149557464%2FFunctions%2F-167468485). |
| [catch](../../com.pubnub.kmp/catch.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/catch.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/catch.md)&gt;.[catch](../../com.pubnub.kmp/catch.md)(action: ([Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)) -&gt; [Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[T](../../com.pubnub.kmp/catch.md)&gt;): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/catch.md)&gt; |
| [map](../../com.pubnub.api.endpoints.remoteaction/map.md) | [jvm]<br>fun &lt;[T](../../com.pubnub.api.endpoints.remoteaction/map.md), [U](../../com.pubnub.api.endpoints.remoteaction/map.md)&gt; [ExtendedRemoteAction](../../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;[T](../../com.pubnub.api.endpoints.remoteaction/map.md)&gt;.[map](../../com.pubnub.api.endpoints.remoteaction/map.md)(function: ([T](../../com.pubnub.api.endpoints.remoteaction/map.md)) -&gt; [U](../../com.pubnub.api.endpoints.remoteaction/map.md)): [ExtendedRemoteAction](../../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;[U](../../com.pubnub.api.endpoints.remoteaction/map.md)&gt; |
| [operationType](../../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/operation-type.md) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/operation-type.md)(): [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md)<br>Return the type of this operation from the values defined in [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md). |
| [overrideConfiguration](override-configuration.md) | [jvm]<br>abstract fun [overrideConfiguration](override-configuration.md)(configuration: [PNConfiguration](../../com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](index.md)&lt;[OUTPUT](index.md)&gt;<br>abstract fun [overrideConfiguration](override-configuration.md)(action: [PNConfigurationOverride.Builder](../../com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [Endpoint](index.md)&lt;[OUTPUT](index.md)&gt;<br>Allows to override certain configuration options (see [PNConfigurationOverride.Builder](../../com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md)) for this request only. |
| [remember](../../com.pubnub.kmp/remember.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/remember.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/remember.md)&gt;.[remember](../../com.pubnub.kmp/remember.md)(): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/remember.md)&gt; |
| [retry](../../com.pubnub.api.endpoints.remoteaction/-remote-action/retry.md) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.remoteaction/-remote-action/retry.md)()<br>Attempt to retry the action and deliver the result to a callback registered with a previous call to [async](index.md#149557464%2FFunctions%2F-167468485). |
| [silentCancel](../../com.pubnub.api.endpoints.remoteaction/-cancelable/silent-cancel.md) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.remoteaction/-cancelable/silent-cancel.md)()<br>Cancel the action without reporting any further results. |
| [sync](../../com.pubnub.api.endpoints.remoteaction/-remote-action/sync.md) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.remoteaction/-remote-action/sync.md)(): [OUTPUT](index.md)<br>Run the action synchronously, potentially blocking the calling thread. |
| [then](../../com.pubnub.kmp/then.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/then.md), [U](../../com.pubnub.kmp/then.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/then.md)&gt;.[then](../../com.pubnub.kmp/then.md)(action: ([T](../../com.pubnub.kmp/then.md)) -&gt; [U](../../com.pubnub.kmp/then.md)): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[U](../../com.pubnub.kmp/then.md)&gt; |
| [thenAsync](../../com.pubnub.kmp/then-async.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/then-async.md), [U](../../com.pubnub.kmp/then-async.md)&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/then-async.md)&gt;.[thenAsync](../../com.pubnub.kmp/then-async.md)(action: ([T](../../com.pubnub.kmp/then-async.md)) -&gt; [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[U](../../com.pubnub.kmp/then-async.md)&gt;): [PNFuture](../../com.pubnub.kmp/-p-n-future/index.md)&lt;[U](../../com.pubnub.kmp/then-async.md)&gt; |
