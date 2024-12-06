//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java](../index.md)/[PubNub](index.md)/[removeChannelMembers](remove-channel-members.md)

# removeChannelMembers

[jvm]\
abstract fun [removeChannelMembers](remove-channel-members.md)(channelId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), channelMembers: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;): [RemoveChannelMembersBuilder](../../com.pubnub.api.java.endpoints.objects_api.members/-remove-channel-members-builder/index.md)

Remove members from a Channel.

[jvm]\
abstract fun [~~removeChannelMembers~~](remove-channel-members.md)(): [RemoveChannelMembers.Builder](../../com.pubnub.api.java.endpoints.objects_api.members/-remove-channel-members/-builder/index.md)

---

### Deprecated

Use removeChannelMembers(String, Collection&lt;PNUUID&gt;) instead.

#### Replace with

```kotlin
removeChannelMembers(channelId, channelMembers)
```
---

Remove members from a Channel.
