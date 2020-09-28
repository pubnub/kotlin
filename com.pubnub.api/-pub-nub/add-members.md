[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNub](index.md) / [addMembers](./add-members.md)

# addMembers

`fun addMembers(channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, uuids: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`PNUUIDWithCustom`](../../com.pubnub.api.models.consumer.objects.member/-p-n-u-u-i-d-with-custom/index.md)`>, limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, page: `[`PNPage`](../../com.pubnub.api.models.consumer.objects/-p-n-page/index.md)`? = null, filter: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, sort: `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`PNSortKey`](../../com.pubnub.api.models.consumer.objects/-p-n-sort-key/index.md)`> = listOf(), includeCount: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, includeCustom: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, includeUUIDDetails: `[`PNUUIDDetailsLevel`](../../com.pubnub.api.models.consumer.objects.member/-p-n-u-u-i-d-details-level/index.md)`? = null): `[`AddMembers`](../../com.pubnub.api.endpoints.objects.member/-add-members/index.md)

This method sets members in a channel.

### Parameters

`channel` - Channel name

`uuids` - List of members to add to the channel. List can contain strings (uuid only)
    or objects (which can include custom data). @see [PNUUIDWithCustom](../../com.pubnub.api.models.consumer.objects.member/-p-n-u-u-i-d-with-custom/index.md)

`limit` - Number of objects to return in the response.
    Default is 100, which is also the maximum value.
    Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count.

`page` - Use for pagination.
    - [PNNext](#) : Previously-returned cursor bookmark for fetching the next page.
    - [PNPrev](#) : Previously-returned cursor bookmark for fetching the previous page.
                 Ignored if you also supply the start parameter.

`filter` - Expression used to filter the results. Only objects whose properties satisfy the given
    expression are returned.

`sort` - List of properties to sort by. Available options are id, name, and updated.
    @see [PNAsc](#), [PNDesc](#)

`includeCount` - Request totalCount to be included in paginated response. By default, totalCount is omitted.
    Default is `false`.

`includeCustom` - Include respective additional fields in the response.

`includeUUIDDetails` - Include custom fields for UUIDs metadata.