//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[manageChannelMembers](manage-channel-members.md)

# manageChannelMembers

[jvm]\
abstract fun [manageChannelMembers](manage-channel-members.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), uuidsToSet: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[MemberInput](../../com.pubnub.api.models.consumer.objects.member/-member-input/index.md)&gt;, uuidsToRemove: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, limit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null, page: [PNPage](../../com.pubnub.api.models.consumer.objects/-p-n-page/index.md)? = null, filter: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, sort: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[PNSortKey](../../com.pubnub.api.models.consumer.objects/-p-n-sort-key/index.md)&lt;[PNMemberKey](../../com.pubnub.api.models.consumer.objects/-p-n-member-key/index.md)&gt;&gt; = listOf(), includeCount: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, includeCustom: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, includeUUIDDetails: [PNUUIDDetailsLevel](../../com.pubnub.api.models.consumer.objects.member/-p-n-u-u-i-d-details-level/index.md)? = null, includeUUIDType: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false): [ManageChannelMembers](../../com.pubnub.api.endpoints.objects.member/-manage-channel-members/index.md)

Set or remove members in a channel.

#### Parameters

jvm

| | |
|---|---|
| channel | Channel name |
| uuidsToSet | Collection of members to add to the channel. @see [com.pubnub.api.models.consumer.objects.member.PNMember.Partial](../../com.pubnub.api.models.consumer.objects.member/-p-n-member/-partial/index.md) |
| uuidsToRemove | Members to remove from channel. |
| limit | Number of objects to return in the response.     Default is 100, which is also the maximum value.     Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count. |
| page | Use for pagination.     - PNNext : Previously-returned cursor bookmark for fetching the next page.     - PNPrev : Previously-returned cursor bookmark for fetching the previous page.                  Ignored if you also supply the start parameter. |
| filter | Expression used to filter the results. Only objects whose properties satisfy the given     expression are returned. |
| sort | List of properties to sort by. Available options are id, name, and updated.     @see PNAsc, PNDesc |
| includeCount | Request totalCount to be included in paginated response. By default, totalCount is omitted.     Default is `false`. |
| includeCustom | Include respective additional fields in the response. |
| includeUUIDDetails | Include custom fields for UUIDs metadata. |
| includeType | Include the type field for UUID metadata |
