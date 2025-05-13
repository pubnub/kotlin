//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[getChannelMembers](get-channel-members.md)

# getChannelMembers

[common]\
expect abstract fun [getChannelMembers](get-channel-members.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), limit: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)? = null, page: [PNPage](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects/-p-n-page/index.md)? = null, filter: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, sort: [Collection](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[PNSortKey](../../com.pubnub.api.models.consumer.objects/-p-n-sort-key/index.md)&lt;[PNMemberKey](../../com.pubnub.api.models.consumer.objects/-p-n-member-key/index.md)&gt;&gt; = listOf(), include: [MemberInclude](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects.member/-member-include/index.md) = MemberInclude()): [GetChannelMembers](../../com.pubnub.api.endpoints.objects.member/-get-channel-members/index.md)actual abstract fun [getChannelMembers](get-channel-members.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), limit: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)?, page: [PNPage](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects/-p-n-page/index.md)?, filter: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, sort: [Collection](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[PNSortKey](../../com.pubnub.api.models.consumer.objects/-p-n-sort-key/index.md)&lt;[PNMemberKey](../../com.pubnub.api.models.consumer.objects/-p-n-member-key/index.md)&gt;&gt;, include: [MemberInclude](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects.member/-member-include/index.md)): [GetChannelMembers](../../com.pubnub.api.endpoints.objects.member/-get-channel-members/index.md)

expect abstract fun [getChannelMembers](get-channel-members.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), limit: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)? = null, page: [PNPage](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects/-p-n-page/index.md)? = null, filter: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, sort: [Collection](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[PNSortKey](../../com.pubnub.api.models.consumer.objects/-p-n-sort-key/index.md)&lt;[PNMemberKey](../../com.pubnub.api.models.consumer.objects/-p-n-member-key/index.md)&gt;&gt; = listOf(), includeCount: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false, includeCustom: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false, includeUUIDDetails: [PNUUIDDetailsLevel](../../com.pubnub.api.models.consumer.objects.member/-p-n-u-u-i-d-details-level/index.md)? = null, includeType: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false): [GetChannelMembers](../../com.pubnub.api.endpoints.objects.member/-get-channel-members/index.md)actual abstract fun [getChannelMembers](get-channel-members.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), limit: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)?, page: [PNPage](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects/-p-n-page/index.md)?, filter: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, sort: [Collection](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[PNSortKey](../../com.pubnub.api.models.consumer.objects/-p-n-sort-key/index.md)&lt;[PNMemberKey](../../com.pubnub.api.models.consumer.objects/-p-n-member-key/index.md)&gt;&gt;, includeCount: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), includeCustom: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), includeUUIDDetails: [PNUUIDDetailsLevel](../../com.pubnub.api.models.consumer.objects.member/-p-n-u-u-i-d-details-level/index.md)?, includeType: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)): [GetChannelMembers](../../com.pubnub.api.endpoints.objects.member/-get-channel-members/index.md)

[jvm]\
actual abstract fun [getChannelMembers](get-channel-members.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), limit: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)?, page: [PNPage](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects/-p-n-page/index.md)?, filter: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, sort: [Collection](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[PNSortKey](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.models.consumer.objects/-p-n-sort-key/index.md)&lt;[PNMemberKey](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.models.consumer.objects/-p-n-member-key/index.md)&gt;&gt;, include: [MemberInclude](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects.member/-member-include/index.md)): [GetChannelMembers](../../com.pubnub.api.endpoints.objects.member/-get-channel-members/index.md)

The method returns a list of members in a channel. The list will include user metadata for members that have additional metadata stored in the database.

#### Parameters

jvm

| | |
|---|---|
| channel | Channel name |
| limit | Number of objects to return in the response.     Default is 100, which is also the maximum value.     Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count. |
| page | Use for pagination.     - PNNext : Previously-returned cursor bookmark for fetching the next page.     - PNPrev : Previously-returned cursor bookmark for fetching the previous page.                  Ignored if you also supply the start parameter. |
| filter | Expression used to filter the results. Only objects whose properties satisfy the given     expression are returned. |
| sort | List of properties to sort by. Available options are id, name, and updated.     @see PNAsc, PNDesc |
| include | Request specific elements to be available in response. Use [com.pubnub.api.models.consumer.objects.member.MemberInclude](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects.member/-member-include/index.md) to easily create the desired configuration. |

[jvm]\
actual abstract fun [~~getChannelMembers~~](get-channel-members.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), limit: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)?, page: [PNPage](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects/-p-n-page/index.md)?, filter: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, sort: [Collection](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[PNSortKey](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.models.consumer.objects/-p-n-sort-key/index.md)&lt;[PNMemberKey](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.models.consumer.objects/-p-n-member-key/index.md)&gt;&gt;, includeCount: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), includeCustom: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), includeUUIDDetails: [PNUUIDDetailsLevel](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.models.consumer.objects.member/-p-n-u-u-i-d-details-level/index.md)?, includeType: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)): [GetChannelMembers](../../com.pubnub.api.endpoints.objects.member/-get-channel-members/index.md)

---

### Deprecated

This function is deprecated. Use the new getChannelMembers(channel, limit, page, filter, sort, MembershipInclude(...))

#### Replace with

```kotlin
getChannelMembers(channel, limit, page, filter, sort, com.pubnub.api.models.consumer.objects.member.MemberInclude(includeTotalCount = includeCount, includeCustom = includeCustom, includeUser = true, includeUserCustom = true, includeUserType = includeUUIDType)
```
---

The method returns a list of members in a channel. The list will include user metadata for members that have additional metadata stored in the database.

#### Parameters

jvm

| | |
|---|---|
| channel | Channel name |
| limit | Number of objects to return in the response.     Default is 100, which is also the maximum value.     Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count. |
| page | Use for pagination.     - PNNext : Previously-returned cursor bookmark for fetching the next page.     - PNPrev : Previously-returned cursor bookmark for fetching the previous page.                  Ignored if you also supply the start parameter. |
| filter | Expression used to filter the results. Only objects whose properties satisfy the given     expression are returned. |
| sort | List of properties to sort by. Available options are id, name, and updated.     @see PNAsc, PNDesc |
| includeCount | Request totalCount to be included in paginated response. By default, totalCount is omitted.     Default is `false`. |
| includeCustom | Include respective additional fields in the response. |
| includeUUIDDetails | Include custom fields for UUIDs metadata. |
