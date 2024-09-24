//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[getSubscribedChannelGroups](get-subscribed-channel-groups.md)

# getSubscribedChannelGroups

[common, native]\
[common]\
expect abstract fun [getSubscribedChannelGroups](get-subscribed-channel-groups.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;

[native]\
actual abstract fun [getSubscribedChannelGroups](get-subscribed-channel-groups.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;

[jvm]\
actual abstract fun [getSubscribedChannelGroups](get-subscribed-channel-groups.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;

Queries the local subscribe loop for channel groups currently in the mix.

#### Return

A list of channel groups the client is currently subscribed to.
