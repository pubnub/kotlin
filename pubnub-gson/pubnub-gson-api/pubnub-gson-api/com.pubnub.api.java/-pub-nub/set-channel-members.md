//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java](../index.md)/[PubNub](index.md)/[setChannelMembers](set-channel-members.md)

# setChannelMembers

[jvm]\
abstract fun [setChannelMembers](set-channel-members.md)(channelId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), channelMembers: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[PNUser](../../com.pubnub.api.java.models.consumer.objects_api.member/-p-n-user/index.md)&gt;): [SetChannelMembersBuilder](../../com.pubnub.api.java.endpoints.objects_api.members/-set-channel-members-builder/index.md)

This method sets members in a channel.

[jvm]\
abstract fun [~~setChannelMembers~~](set-channel-members.md)(): [SetChannelMembers.Builder](../../com.pubnub.api.java.endpoints.objects_api.members/-set-channel-members/-builder/index.md)

---

### Deprecated

Use setChannelMembers(String, Collection&lt;PNUUID&gt;) instead.

#### Replace with

```kotlin
setChannelMembers(channelId, channelMembers)
```
---

This method sets members in a channel.
