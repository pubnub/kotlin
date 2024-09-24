//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.kmp](../index.md)/[PNFuture](index.md)

# PNFuture

fun interface [PNFuture](index.md)&lt;out [OUTPUT](index.md)&gt;

#### Inheritors

| |
|---|
| [DeleteMessages](../../com.pubnub.api.endpoints/-delete-messages/index.md) |
| [FetchMessages](../../com.pubnub.api.endpoints/-fetch-messages/index.md) |
| [MessageCounts](../../com.pubnub.api.endpoints/-message-counts/index.md) |
| [Time](../../com.pubnub.api.endpoints/-time/index.md) |
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
| [RemoteAction](../../com.pubnub.api.endpoints.remoteaction/-remote-action/index.md) |

## Functions

| Name | Summary |
|---|---|
| [alsoAsync](../also-async.md) | [common]<br>fun &lt;[T](../also-async.md)&gt; [PNFuture](index.md)&lt;[T](../also-async.md)&gt;.[alsoAsync](../also-async.md)(action: ([T](../also-async.md)) -&gt; [PNFuture](index.md)&lt;*&gt;): [PNFuture](index.md)&lt;[T](../also-async.md)&gt;<br>Execute a second PNFuture after this PNFuture completes successfully, and return the *original* value of this PNFuture after the second PNFuture completes successfully. |
| [async](async.md) | [common]<br>abstract fun [async](async.md)(callback: [Consumer](../../com.pubnub.api.v2.callbacks/-consumer/index.md)&lt;[Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;@[UnsafeVariance](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unsafe-variance/index.html)[OUTPUT](index.md)&gt;&gt;) |
| [catch](../catch.md) | [common]<br>fun &lt;[T](../catch.md)&gt; [PNFuture](index.md)&lt;[T](../catch.md)&gt;.[catch](../catch.md)(action: ([Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)) -&gt; [Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[T](../catch.md)&gt;): [PNFuture](index.md)&lt;[T](../catch.md)&gt; |
| [remember](../remember.md) | [common]<br>fun &lt;[T](../remember.md)&gt; [PNFuture](index.md)&lt;[T](../remember.md)&gt;.[remember](../remember.md)(): [PNFuture](index.md)&lt;[T](../remember.md)&gt; |
| [then](../then.md) | [common]<br>fun &lt;[T](../then.md), [U](../then.md)&gt; [PNFuture](index.md)&lt;[T](../then.md)&gt;.[then](../then.md)(action: ([T](../then.md)) -&gt; [U](../then.md)): [PNFuture](index.md)&lt;[U](../then.md)&gt; |
| [thenAsync](../then-async.md) | [common]<br>fun &lt;[T](../then-async.md), [U](../then-async.md)&gt; [PNFuture](index.md)&lt;[T](../then-async.md)&gt;.[thenAsync](../then-async.md)(action: ([T](../then-async.md)) -&gt; [PNFuture](index.md)&lt;[U](../then-async.md)&gt;): [PNFuture](index.md)&lt;[U](../then-async.md)&gt; |
