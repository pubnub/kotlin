//[pubnub-kotlin](../../index.md)/[com.pubnub.api.endpoints.objects.member](index.md)

# Package com.pubnub.api.endpoints.objects.member

## Types

| Name | Summary |
|---|---|
| [GetChannelMembers](-get-channel-members/index.md) | [jvm]<br>class [GetChannelMembers](-get-channel-members/index.md) : [Endpoint](../com.pubnub.api/-endpoint/index.md)&lt;[EntityArrayEnvelope](../com.pubnub.api.models.server.objects_api/-entity-array-envelope/index.md)&lt;[PNMember](../com.pubnub.api.models.consumer.objects.member/-p-n-member/index.md)&gt;, [PNMemberArrayResult](../com.pubnub.api.models.consumer.objects.member/-p-n-member-array-result/index.md)&gt; |
| [ManageChannelMembers](-manage-channel-members/index.md) | [jvm]<br>class [ManageChannelMembers](-manage-channel-members/index.md)(pubnub: [PubNub](../com.pubnub.api/-pub-nub/index.md), uuidsToSet: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[MemberInput](../com.pubnub.api.models.consumer.objects.member/-member-input/index.md)&gt;, uuidsToRemove: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), collectionQueryParameters: [CollectionQueryParameters](../com.pubnub.api.endpoints.objects.internal/-collection-query-parameters/index.md), includeQueryParam: [IncludeQueryParam](../com.pubnub.api.endpoints.objects.internal/-include-query-param/index.md)) : [Endpoint](../com.pubnub.api/-endpoint/index.md)&lt;[EntityArrayEnvelope](../com.pubnub.api.models.server.objects_api/-entity-array-envelope/index.md)&lt;[PNMember](../com.pubnub.api.models.consumer.objects.member/-p-n-member/index.md)&gt;, [PNMemberArrayResult](../com.pubnub.api.models.consumer.objects.member/-p-n-member-array-result/index.md)&gt; |
