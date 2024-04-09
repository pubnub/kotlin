//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[addMemberships](add-memberships.md)

# addMemberships

[jvm]\
abstract fun [~~addMemberships~~](add-memberships.md)(channels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[ChannelMembershipInput](../../com.pubnub.api.models.consumer.objects.membership/-channel-membership-input/index.md)&gt;, uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, limit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null, page: [PNPage](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.objects/-p-n-page/index.md)? = null, filter: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, sort: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[PNSortKey](../../com.pubnub.api.models.consumer.objects/-p-n-sort-key/index.md)&lt;[PNMembershipKey](../../com.pubnub.api.models.consumer.objects/-p-n-membership-key/index.md)&gt;&gt; = listOf(), includeCount: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, includeCustom: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, includeChannelDetails: [PNChannelDetailsLevel](../../com.pubnub.api.models.consumer.objects.membership/-p-n-channel-details-level/index.md)? = null): [ManageMemberships](../../com.pubnub.api.endpoints.objects.membership/-manage-memberships/index.md)

---

### Deprecated

Use setMemberships instead

#### Replace with

```kotlin
setMemberships(channels = channels, uuid = uuid, limit = limit, page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom,includeChannelDetails = includeChannelDetails)
```
---

#### See also

| |
|---|
| [PubNub.setMemberships](set-memberships.md) |
