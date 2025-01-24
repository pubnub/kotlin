//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java](../index.md)/[PubNub](index.md)/[manageChannelMembers](manage-channel-members.md)

# manageChannelMembers

[jvm]\
abstract fun [manageChannelMembers](manage-channel-members.md)(channelId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), set: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[PNUser](../../com.pubnub.api.java.models.consumer.objects_api.member/-p-n-user/index.md)&gt;, remove: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt;): [ManageChannelMembersBuilder](../../com.pubnub.api.java.endpoints.objects_api.members/-manage-channel-members-builder/index.md)

Set or remove members in a channel.

[jvm]\
abstract fun [~~manageChannelMembers~~](manage-channel-members.md)(): [ManageChannelMembers.Builder](../../com.pubnub.api.java.endpoints.objects_api.members/-manage-channel-members/-builder/index.md)

---

### Deprecated

Use manageChannelMembers(String, Collection&lt;PNUUID&gt;, Collection&lt;PNUUID&gt;) instead.

#### Replace with

```kotlin
manageChannelMembers(channelId, channelMembersToSet, channelMembersToRemove)
```
---

Set or remove members in a channel.
