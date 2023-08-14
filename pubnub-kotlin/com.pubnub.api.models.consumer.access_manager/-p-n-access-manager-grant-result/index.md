//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.consumer.access_manager](../index.md)/[PNAccessManagerGrantResult](index.md)

# PNAccessManagerGrantResult

[jvm]\
class [PNAccessManagerGrantResult](index.md)(val level: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val ttl: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val subscribeKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val channels: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [PNAccessManagerKeyData](../-p-n-access-manager-key-data/index.md)&gt;?&gt;, val channelGroups: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [PNAccessManagerKeyData](../-p-n-access-manager-key-data/index.md)&gt;?&gt;)

Result of the [PubNub.grant](../../com.pubnub.api/-pub-nub/grant.md) operation

## Constructors

| | |
|---|---|
| [PNAccessManagerGrantResult](-p-n-access-manager-grant-result.md) | [jvm]<br>fun [PNAccessManagerGrantResult](-p-n-access-manager-grant-result.md)(level: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), ttl: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), subscribeKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), channels: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [PNAccessManagerKeyData](../-p-n-access-manager-key-data/index.md)&gt;?&gt;, channelGroups: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [PNAccessManagerKeyData](../-p-n-access-manager-key-data/index.md)&gt;?&gt;) |

## Properties

| Name | Summary |
|---|---|
| [channelGroups](channel-groups.md) | [jvm]<br>val [channelGroups](channel-groups.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [PNAccessManagerKeyData](../-p-n-access-manager-key-data/index.md)&gt;?&gt;<br>Access rights per channel group. |
| [channels](channels.md) | [jvm]<br>val [channels](channels.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [PNAccessManagerKeyData](../-p-n-access-manager-key-data/index.md)&gt;?&gt;<br>Access rights per channel. |
| [level](level.md) | [jvm]<br>val [level](level.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Permissions level, one of `subkey`, `subkey+auth`, `channel`, `channel-group`, `channel-group+auth` level. |
| [subscribeKey](subscribe-key.md) | [jvm]<br>val [subscribeKey](subscribe-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The subscribe key. |
| [ttl](ttl.md) | [jvm]<br>val [ttl](ttl.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Time in minutes for which granted permissions are valid. Value of `0` means indefinite. |
