//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[addMembers](add-members.md)

# addMembers

[jvm]\
abstract fun [~~addMembers~~](add-members.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), uuids: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[MemberInput](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.models.consumer.objects.member/-member-input/index.md)&gt;, limit: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)? = null, page: [PNPage](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects/-p-n-page/index.md)? = null, filter: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, sort: [Collection](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[PNSortKey](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.models.consumer.objects/-p-n-sort-key/index.md)&lt;[PNMemberKey](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.models.consumer.objects/-p-n-member-key/index.md)&gt;&gt; = listOf(), includeCount: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false, includeCustom: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false, includeUUIDDetails: [PNUUIDDetailsLevel](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.models.consumer.objects.member/-p-n-u-u-i-d-details-level/index.md)? = null): [ManageChannelMembers](../../com.pubnub.api.endpoints.objects.member/-manage-channel-members/index.md)

---

### Deprecated

Use setChannelMembers instead

#### Replace with

```kotlin
setChannelMembers(channel = channel, uuids = uuids, limit = limit, page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom,includeUUIDDetails = includeUUIDDetails)
```
---

#### See also

| |
|---|
| [PubNub.setChannelMembers](set-channel-members.md) |
