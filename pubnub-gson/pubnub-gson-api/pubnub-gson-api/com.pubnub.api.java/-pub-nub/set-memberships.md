//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java](../index.md)/[PubNub](index.md)/[setMemberships](set-memberships.md)

# setMemberships

[jvm]\
abstract fun [setMemberships](set-memberships.md)(channelMemberships: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[PNChannelMembership](../../com.pubnub.api.java.models.consumer.objects_api.membership/-p-n-channel-membership/index.md)&gt;): [SetMembershipsBuilder](../../com.pubnub.api.java.endpoints.objects_api.memberships/-set-memberships-builder/index.md)

Set channel memberships for a User.

[jvm]\
abstract fun [~~setMemberships~~](set-memberships.md)(): [SetMemberships.Builder](../../com.pubnub.api.java.endpoints.objects_api.memberships/-set-memberships/-builder/index.md)

---

### Deprecated

Use setMemberships(Collection&lt;PNChannelMembership&gt;) instead.

#### Replace with

```kotlin
setMemberships(channelMemberships)
```
---

Set channel memberships for a UUID.
