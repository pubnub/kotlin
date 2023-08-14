//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.endpoints.objects.member](../index.md)/[ManageChannelMembers](index.md)

# ManageChannelMembers

[jvm]\
class [ManageChannelMembers](index.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), uuidsToSet: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[MemberInput](../../com.pubnub.api.models.consumer.objects.member/-member-input/index.md)&gt;, uuidsToRemove: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), collectionQueryParameters: [CollectionQueryParameters](../../com.pubnub.api.endpoints.objects.internal/-collection-query-parameters/index.md), includeQueryParam: [IncludeQueryParam](../../com.pubnub.api.endpoints.objects.internal/-include-query-param/index.md)) : [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[EntityArrayEnvelope](../../com.pubnub.api.models.server.objects_api/-entity-array-envelope/index.md)&lt;[PNMember](../../com.pubnub.api.models.consumer.objects.member/-p-n-member/index.md)&gt;, [PNMemberArrayResult](../../com.pubnub.api.models.consumer.objects.member/-p-n-member-array-result/index.md)&gt;

## See also

jvm

| | |
|---|---|
| [com.pubnub.api.PubNub](../../com.pubnub.api/-pub-nub/manage-channel-members.md) |  |

## Constructors

| | |
|---|---|
| [ManageChannelMembers](-manage-channel-members.md) | [jvm]<br>fun [ManageChannelMembers](-manage-channel-members.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), uuidsToSet: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[MemberInput](../../com.pubnub.api.models.consumer.objects.member/-member-input/index.md)&gt;, uuidsToRemove: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), collectionQueryParameters: [CollectionQueryParameters](../../com.pubnub.api.endpoints.objects.internal/-collection-query-parameters/index.md), includeQueryParam: [IncludeQueryParam](../../com.pubnub.api.endpoints.objects.internal/-include-query-param/index.md)) |

## Functions

| Name | Summary |
|---|---|
| [async](index.md#-1812977128%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [async](index.md#-1812977128%2FFunctions%2F-1216412040)(callback: (result: [PNMemberArrayResult](../../com.pubnub.api.models.consumer.objects.member/-p-n-member-array-result/index.md)?, status: [PNStatus](../../com.pubnub.api.models.consumer/-p-n-status/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))<br>Executes the call asynchronously. This function does not block the thread. |
| [operationType](operation-type.md) | [jvm]<br>open override fun [operationType](operation-type.md)(): [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](../../com.pubnub.api/-endpoint/retry.md) | [jvm]<br>open override fun [retry](../../com.pubnub.api/-endpoint/retry.md)() |
| [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md) | [jvm]<br>open override fun [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md)()<br>Cancel the operation but do not alert anybody, useful for restarting the heartbeats and subscribe loops. |
| [sync](../../com.pubnub.api/-endpoint/sync.md) | [jvm]<br>open override fun [sync](../../com.pubnub.api/-endpoint/sync.md)(): [PNMemberArrayResult](../../com.pubnub.api.models.consumer.objects.member/-p-n-member-array-result/index.md)?<br>Executes the call synchronously. This function blocks the thread. |

## Properties

| Name | Summary |
|---|---|
| [queryParam](../../com.pubnub.api/-endpoint/query-param.md) | [jvm]<br>val [queryParam](../../com.pubnub.api/-endpoint/query-param.md): [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;<br>Key-value object to pass with every PubNub API operation. Used for debugging purposes. todo: it should be removed! |
