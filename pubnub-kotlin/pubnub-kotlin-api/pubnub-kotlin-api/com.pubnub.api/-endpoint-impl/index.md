//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[EndpointImpl](index.md)

# EndpointImpl

open class [EndpointImpl](index.md)&lt;[T](index.md), [U](index.md)&gt;(promiseFactory: () -&gt; [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[T](index.md)&gt;, responseMapping: ([T](index.md)) -&gt; [U](index.md)) : [PNFuture](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.kmp/-p-n-future/index.md)&lt;[U](index.md)&gt; 

#### Inheritors

| |
|---|
| [DeleteMessagesImpl](../../com.pubnub.api.endpoints/[js]-delete-messages-impl/index.md) |
| [FetchMessagesImpl](../../com.pubnub.api.endpoints/[js]-fetch-messages-impl/index.md) |
| [MessageCountsImpl](../../com.pubnub.api.endpoints/[js]-message-counts-impl/index.md) |
| [TimeImpl](../../com.pubnub.api.endpoints/[js]-time-impl/index.md) |
| [GrantTokenImpl](../../com.pubnub.api.endpoints.access/-grant-token-impl/index.md) |
| [RevokeTokenImpl](../../com.pubnub.api.endpoints.access/-revoke-token-impl/index.md) |
| [AddChannelChannelGroupImpl](../../com.pubnub.api.endpoints.channel_groups/[js]-add-channel-channel-group-impl/index.md) |
| [AllChannelsChannelGroupImpl](../../com.pubnub.api.endpoints.channel_groups/[js]-all-channels-channel-group-impl/index.md) |
| [DeleteChannelGroupImpl](../../com.pubnub.api.endpoints.channel_groups/[js]-delete-channel-group-impl/index.md) |
| [ListAllChannelGroupImpl](../../com.pubnub.api.endpoints.channel_groups/[js]-list-all-channel-group-impl/index.md) |
| [RemoveChannelChannelGroupImpl](../../com.pubnub.api.endpoints.channel_groups/[js]-remove-channel-channel-group-impl/index.md) |
| [DeleteFileImpl](../../com.pubnub.api.endpoints.files/[js]-delete-file-impl/index.md) |
| [DownloadFileImpl](../../com.pubnub.api.endpoints.files/[js]-download-file-impl/index.md) |
| [GetFileUrlImpl](../../com.pubnub.api.endpoints.files/[js]-get-file-url-impl/index.md) |
| [ListFilesImpl](../../com.pubnub.api.endpoints.files/[js]-list-files-impl/index.md) |
| [PublishFileMessageImpl](../../com.pubnub.api.endpoints.files/[js]-publish-file-message-impl/index.md) |
| [SendFileImpl](../../com.pubnub.api.endpoints.files/[js]-send-file-impl/index.md) |
| [AddMessageActionImpl](../../com.pubnub.api.endpoints.message_actions/[js]-add-message-action-impl/index.md) |
| [RemoveMessageActionImpl](../../com.pubnub.api.endpoints.message_actions/[js]-remove-message-action-impl/index.md) |
| [GetMessageActionImpl](../../com.pubnub.api.endpoints.message_actions/-get-message-action-impl/index.md) |
| [GetAllChannelMetadataImpl](../../com.pubnub.api.endpoints.objects.channel/[js]-get-all-channel-metadata-impl/index.md) |
| [GetChannelMetadataImpl](../../com.pubnub.api.endpoints.objects.channel/[js]-get-channel-metadata-impl/index.md) |
| [RemoveChannelMetadataImpl](../../com.pubnub.api.endpoints.objects.channel/[js]-remove-channel-metadata-impl/index.md) |
| [SetChannelMetadataImpl](../../com.pubnub.api.endpoints.objects.channel/[js]-set-channel-metadata-impl/index.md) |
| [GetChannelMembersImpl](../../com.pubnub.api.endpoints.objects.member/[js]-get-channel-members-impl/index.md) |
| [RemoveChannelMembersImpl](../../com.pubnub.api.endpoints.objects.member/[js]-remove-channel-members-impl/index.md) |
| [SetChannelMembersImpl](../../com.pubnub.api.endpoints.objects.member/[js]-set-channel-members-impl/index.md) |
| [GetMembershipsImpl](../../com.pubnub.api.endpoints.objects.membership/[js]-get-memberships-impl/index.md) |
| [RemoveMembershipsImpl](../../com.pubnub.api.endpoints.objects.membership/[js]-remove-memberships-impl/index.md) |
| [SetMembershipsImpl](../../com.pubnub.api.endpoints.objects.membership/-set-memberships-impl/index.md) |
| [GetAllUUIDMetadataImpl](../../com.pubnub.api.endpoints.objects.uuid/[js]-get-all-u-u-i-d-metadata-impl/index.md) |
| [GetUUIDMetadataImpl](../../com.pubnub.api.endpoints.objects.uuid/[js]-get-u-u-i-d-metadata-impl/index.md) |
| [RemoveUUIDMetadataImpl](../../com.pubnub.api.endpoints.objects.uuid/[js]-remove-u-u-i-d-metadata-impl/index.md) |
| [SetUUIDMetadataImpl](../../com.pubnub.api.endpoints.objects.uuid/[js]-set-u-u-i-d-metadata-impl/index.md) |
| [GetStateImpl](../../com.pubnub.api.endpoints.presence/[js]-get-state-impl/index.md) |
| [HereNowImpl](../../com.pubnub.api.endpoints.presence/[js]-here-now-impl/index.md) |
| [SetStateImpl](../../com.pubnub.api.endpoints.presence/[js]-set-state-impl/index.md) |
| [WhereNowImpl](../../com.pubnub.api.endpoints.presence/[js]-where-now-impl/index.md) |
| [PublishImpl](../../com.pubnub.api.endpoints.pubsub/[js]-publish-impl/index.md) |
| [SignalImpl](../../com.pubnub.api.endpoints.pubsub/[js]-signal-impl/index.md) |
| [FireImpl](../../com.pubnub.api.endpoints.pubsub/-fire-impl/index.md) |
| [AddChannelsToPushImpl](../../com.pubnub.api.endpoints.push/[js]-add-channels-to-push-impl/index.md) |
| [ListPushProvisionsImpl](../../com.pubnub.api.endpoints.push/[js]-list-push-provisions-impl/index.md) |
| [RemoveAllPushChannelsForDeviceImpl](../../com.pubnub.api.endpoints.push/[js]-remove-all-push-channels-for-device-impl/index.md) |
| [RemoveChannelsFromPushImpl](../../com.pubnub.api.endpoints.push/[js]-remove-channels-from-push-impl/index.md) |

## Constructors

| | |
|---|---|
| [EndpointImpl](-endpoint-impl.md) | [js]<br>constructor(promiseFactory: () -&gt; [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[T](index.md)&gt;, responseMapping: ([T](index.md)) -&gt; [U](index.md)) |

## Functions

| Name | Summary |
|---|---|
| [alsoAsync](../../com.pubnub.kmp/also-async.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/also-async.md)&gt; [PNFuture](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/also-async.md)&gt;.[alsoAsync](../../com.pubnub.kmp/also-async.md)(action: ([T](../../com.pubnub.kmp/also-async.md)) -&gt; [PNFuture](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.kmp/-p-n-future/index.md)&lt;*&gt;): [PNFuture](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/also-async.md)&gt;<br>Execute a second PNFuture after this PNFuture completes successfully, and return the *original* value of this PNFuture after the second PNFuture completes successfully. |
| [async](async.md) | [js]<br>open override fun [async](async.md)(callback: [Consumer](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.callbacks/-consumer/index.md)&lt;[Result](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[U](index.md)&gt;&gt;) |
| [catch](../../com.pubnub.kmp/catch.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/catch.md)&gt; [PNFuture](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/catch.md)&gt;.[catch](../../com.pubnub.kmp/catch.md)(action: ([Exception](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-exception/index.html)) -&gt; [Result](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[T](../../com.pubnub.kmp/catch.md)&gt;): [PNFuture](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/catch.md)&gt; |
| [remember](../../com.pubnub.kmp/remember.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/remember.md)&gt; [PNFuture](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/remember.md)&gt;.[remember](../../com.pubnub.kmp/remember.md)(): [PNFuture](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/remember.md)&gt; |
| [then](../../com.pubnub.kmp/then.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/then.md), [U](../../com.pubnub.kmp/then.md)&gt; [PNFuture](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/then.md)&gt;.[then](../../com.pubnub.kmp/then.md)(action: ([T](../../com.pubnub.kmp/then.md)) -&gt; [U](../../com.pubnub.kmp/then.md)): [PNFuture](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.kmp/-p-n-future/index.md)&lt;[U](../../com.pubnub.kmp/then.md)&gt; |
| [thenAsync](../../com.pubnub.kmp/then-async.md) | [common]<br>fun &lt;[T](../../com.pubnub.kmp/then-async.md), [U](../../com.pubnub.kmp/then-async.md)&gt; [PNFuture](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.kmp/-p-n-future/index.md)&lt;[T](../../com.pubnub.kmp/then-async.md)&gt;.[thenAsync](../../com.pubnub.kmp/then-async.md)(action: ([T](../../com.pubnub.kmp/then-async.md)) -&gt; [PNFuture](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.kmp/-p-n-future/index.md)&lt;[U](../../com.pubnub.kmp/then-async.md)&gt;): [PNFuture](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.kmp/-p-n-future/index.md)&lt;[U](../../com.pubnub.kmp/then-async.md)&gt; |
