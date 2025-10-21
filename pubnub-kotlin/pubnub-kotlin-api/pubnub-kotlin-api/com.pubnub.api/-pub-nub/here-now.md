//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[hereNow](here-now.md)

# hereNow

[common]\
expect abstract fun [hereNow](here-now.md)(channels: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt; = emptyList(), channelGroups: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt; = emptyList(), includeState: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false, includeUUIDs: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = true, limit: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = 1000, offset: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)? = null): [HereNow](../../com.pubnub.api.endpoints.presence/-here-now/index.md)actual abstract fun [hereNow](here-now.md)(channels: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;, channelGroups: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;, includeState: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), includeUUIDs: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), limit: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), offset: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)?): [HereNow](../../com.pubnub.api.endpoints.presence/-here-now/index.md)

[jvm]\
actual abstract fun [hereNow](here-now.md)(channels: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;, channelGroups: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;, includeState: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), includeUUIDs: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), limit: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), offset: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)?): [HereNow](../../com.pubnub.api.endpoints.presence/-here-now/index.md)

Obtain information about the current state of a channel including a list of unique user IDs currently subscribed to the channel and the total occupancy count of the channel.

#### Parameters

jvm

| | |
|---|---|
| channels | The channels to get the 'here now' details of.     Leave empty for a 'global here now'. |
| channelGroups | The channel groups to get the 'here now' details of.     Leave empty for a 'global here now'. |
| includeState | Whether the response should include presence state information, if available.     Defaults to `false`. |
| includeUUIDs | Whether the response should include UUIDs od connected clients.     Defaults to `true`. |
| limit | Maximum number of occupants to return per channel. Valid range: 0-1000.     - Default: 1000     - Use 0 to get occupancy counts without user details |
| offset | Zero-based starting index for pagination. Returns occupants starting from this position in the list. Must be >= 0.     - Default: null (no offset) |
