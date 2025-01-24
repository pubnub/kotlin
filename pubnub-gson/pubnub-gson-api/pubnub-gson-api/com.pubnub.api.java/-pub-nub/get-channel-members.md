//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java](../index.md)/[PubNub](index.md)/[getChannelMembers](get-channel-members.md)

# getChannelMembers

[jvm]\
abstract fun [getChannelMembers](get-channel-members.md)(channelId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [GetChannelMembersBuilder](../../com.pubnub.api.java.endpoints.objects_api.members/-get-channel-members-builder/index.md)

The method returns a list of members in a channel. The list will include user metadata for members that have additional metadata stored in the database.

[jvm]\
abstract fun [~~getChannelMembers~~](get-channel-members.md)(): [GetChannelMembers.Builder](../../com.pubnub.api.java.endpoints.objects_api.members/-get-channel-members/-builder/index.md)

---

### Deprecated

Use getChannelMembers(String) instead.

#### Replace with

```kotlin
getChannelMembers(channelId)
```
---

The method returns a list of members in a channel. The list will include user metadata for members that have additional metadata stored in the database.
