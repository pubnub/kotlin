//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java](../index.md)/[PubNub](index.md)/[removeMemberships](remove-memberships.md)

# removeMemberships

[jvm]\
abstract fun [removeMemberships](remove-memberships.md)(channelMemberships: [Collection](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;): [RemoveMembershipsBuilder](../../com.pubnub.api.java.endpoints.objects_api.memberships/-remove-memberships-builder/index.md)

Remove channel memberships for a UUID.

[jvm]\
abstract fun [~~removeMemberships~~](remove-memberships.md)(): [RemoveMemberships.Builder](../../com.pubnub.api.java.endpoints.objects_api.memberships/-remove-memberships/-builder/index.md)

---

### Deprecated

Use removeMemberships(Collection&lt;PNChannelMembership&gt;) instead.

#### Replace with

```kotlin
removeMemberships(channelMemberships)
```
---

Remove channel memberships for a UUID.
