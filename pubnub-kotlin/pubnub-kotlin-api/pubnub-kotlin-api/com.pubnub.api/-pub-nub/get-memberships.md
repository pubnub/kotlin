//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[getMemberships](get-memberships.md)

# getMemberships

[common, native]\
[common]\
expect abstract fun [getMemberships](get-memberships.md)(uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, limit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null, page: [PNPage](../../com.pubnub.api.models.consumer.objects/-p-n-page/index.md)? = null, filter: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, sort: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[PNSortKey](../../com.pubnub.api.models.consumer.objects/-p-n-sort-key/index.md)&lt;[PNMembershipKey](../../com.pubnub.api.models.consumer.objects/-p-n-membership-key/index.md)&gt;&gt; = listOf(), includeCount: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, includeCustom: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, includeChannelDetails: [PNChannelDetailsLevel](../../com.pubnub.api.models.consumer.objects.membership/-p-n-channel-details-level/index.md)? = null, includeType: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false): [GetMemberships](../../com.pubnub.api.endpoints.objects.membership/-get-memberships/index.md)

[native]\
actual abstract fun [getMemberships](get-memberships.md)(uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, limit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?, page: [PNPage](../../com.pubnub.api.models.consumer.objects/-p-n-page/index.md)?, filter: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, sort: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[PNSortKey](../../com.pubnub.api.models.consumer.objects/-p-n-sort-key/index.md)&lt;[PNMembershipKey](../../com.pubnub.api.models.consumer.objects/-p-n-membership-key/index.md)&gt;&gt;, includeCount: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), includeCustom: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), includeChannelDetails: [PNChannelDetailsLevel](../../com.pubnub.api.models.consumer.objects.membership/-p-n-channel-details-level/index.md)?, includeType: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [GetMemberships](../../com.pubnub.api.endpoints.objects.membership/-get-memberships/index.md)

[jvm]\
actual abstract fun [getMemberships](get-memberships.md)(uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, limit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?, page: [PNPage](../../com.pubnub.api.models.consumer.objects/-p-n-page/index.md)?, filter: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, sort: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[PNSortKey](../../com.pubnub.api.models.consumer.objects/-p-n-sort-key/index.md)&lt;[PNMembershipKey](../../com.pubnub.api.models.consumer.objects/-p-n-membership-key/index.md)&gt;&gt;, includeCount: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), includeCustom: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), includeChannelDetails: [PNChannelDetailsLevel](../../com.pubnub.api.models.consumer.objects.membership/-p-n-channel-details-level/index.md)?, includeType: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [GetMemberships](../../com.pubnub.api.endpoints.objects.membership/-get-memberships/index.md)

The method returns a list of channel memberships for a user. This method doesn't return a user's subscriptions.

#### Parameters

jvm

| | |
|---|---|
| uuid | Unique user identifier. If not supplied then current user’s uuid is used. |
| limit | Number of objects to return in the response.     Default is 100, which is also the maximum value.     Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count. |
| page | Use for pagination.     - PNNext : Previously-returned cursor bookmark for fetching the next page.     - PNPrev : Previously-returned cursor bookmark for fetching the previous page.                  Ignored if you also supply the start parameter. |
| filter | Expression used to filter the results. Only objects whose properties satisfy the given     expression are returned. |
| sort | List of properties to sort by. Available options are id, name, and updated.     @see PNAsc, PNDesc |
| includeCount | Request totalCount to be included in paginated response. By default, totalCount is omitted.     Default is `false`. |
| includeCustom | Include respective additional fields in the response. |
| includeChannelDetails | Include custom fields for channels metadata. |
