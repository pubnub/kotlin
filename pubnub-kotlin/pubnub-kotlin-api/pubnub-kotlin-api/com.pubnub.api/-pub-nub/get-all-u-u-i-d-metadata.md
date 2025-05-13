//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[getAllUUIDMetadata](get-all-u-u-i-d-metadata.md)

# getAllUUIDMetadata

[common]\
expect abstract fun [getAllUUIDMetadata](get-all-u-u-i-d-metadata.md)(limit: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)? = null, page: [PNPage](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects/-p-n-page/index.md)? = null, filter: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, sort: [Collection](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[PNSortKey](../../com.pubnub.api.models.consumer.objects/-p-n-sort-key/index.md)&lt;[PNKey](../../com.pubnub.api.models.consumer.objects/-p-n-key/index.md)&gt;&gt; = listOf(), includeCount: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false, includeCustom: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false): [GetAllUUIDMetadata](../../com.pubnub.api.endpoints.objects.uuid/-get-all-u-u-i-d-metadata/index.md)actual abstract fun [getAllUUIDMetadata](get-all-u-u-i-d-metadata.md)(limit: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)?, page: [PNPage](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects/-p-n-page/index.md)?, filter: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, sort: [Collection](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[PNSortKey](../../com.pubnub.api.models.consumer.objects/-p-n-sort-key/index.md)&lt;[PNKey](../../com.pubnub.api.models.consumer.objects/-p-n-key/index.md)&gt;&gt;, includeCount: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), includeCustom: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)): [GetAllUUIDMetadata](../../com.pubnub.api.endpoints.objects.uuid/-get-all-u-u-i-d-metadata/index.md)

[jvm]\
actual abstract fun [getAllUUIDMetadata](get-all-u-u-i-d-metadata.md)(limit: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)?, page: [PNPage](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects/-p-n-page/index.md)?, filter: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, sort: [Collection](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[PNSortKey](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.models.consumer.objects/-p-n-sort-key/index.md)&lt;[PNKey](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.models.consumer.objects/-p-n-key/index.md)&gt;&gt;, includeCount: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), includeCustom: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)): [GetAllUUIDMetadata](../../com.pubnub.api.endpoints.objects.uuid/-get-all-u-u-i-d-metadata/index.md)

Returns a paginated list of UUID Metadata objects, optionally including the custom data object for each.

#### Parameters

jvm

| | |
|---|---|
| limit | Number of objects to return in the response.     Default is 100, which is also the maximum value.     Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count. |
| page | Use for pagination.     - PNNext : Previously-returned cursor bookmark for fetching the next page.     - PNPrev : Previously-returned cursor bookmark for fetching the previous page.                  Ignored if you also supply the start parameter. |
| filter | Expression used to filter the results. Only objects whose properties satisfy the given     expression are returned. |
| sort | List of properties to sort by. Available options are id, name, and updated.     @see PNAsc, PNDesc |
| includeCount | Request totalCount to be included in paginated response. By default, totalCount is omitted.     Default is `false`. |
| includeCustom | Include respective additional fields in the response. |
