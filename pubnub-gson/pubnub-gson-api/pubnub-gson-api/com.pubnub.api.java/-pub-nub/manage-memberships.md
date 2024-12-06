//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java](../index.md)/[PubNub](index.md)/[manageMemberships](manage-memberships.md)

# manageMemberships

[jvm]\
abstract fun [manageMemberships](manage-memberships.md)(channelsToSet: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[PNChannelMembership](../../com.pubnub.api.java.models.consumer.objects_api.membership/-p-n-channel-membership/index.md)&gt;, channelsToDelete: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;): [ManageMembershipsBuilder](../../com.pubnub.api.java.endpoints.objects_api.memberships/-manage-memberships-builder/index.md)

Add and/or remove channel memberships for a UUID.

[jvm]\
abstract fun [~~manageMemberships~~](manage-memberships.md)(): [ManageMemberships.Builder](../../com.pubnub.api.java.endpoints.objects_api.memberships/-manage-memberships/-builder/index.md)

---

### Deprecated

Use manageMemberships(Collection&lt;PNChannelMembership&gt;, Collection&lt;PNChannelMembership&gt;) instead.

#### Replace with

```kotlin
manageMemberships(channelMembershipsToAdd, channelMembershipsToRemove)
```
---

Add and/or remove channel memberships for a UUID.
