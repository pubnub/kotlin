//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.endpoints.access](../index.md)/[GrantToken](index.md)

# GrantToken

[jvm]\
class [GrantToken](index.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), val ttl: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, authorizedUUID: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, channels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[ChannelGrant](../../com.pubnub.api.models.consumer.access_manager.v3/-channel-grant/index.md)&gt;, channelGroups: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[ChannelGroupGrant](../../com.pubnub.api.models.consumer.access_manager.v3/-channel-group-grant/index.md)&gt;, uuids: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[UUIDGrant](../../com.pubnub.api.models.consumer.access_manager.v3/-u-u-i-d-grant/index.md)&gt;) : [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[GrantTokenResponse](../../com.pubnub.api.models.server.access_manager.v3/-grant-token-response/index.md), [PNGrantTokenResult](../../com.pubnub.api.models.consumer.access_manager.v3/-p-n-grant-token-result/index.md)&gt;

## Constructors

| | |
|---|---|
| [GrantToken](-grant-token.md) | [jvm]<br>fun [GrantToken](-grant-token.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), ttl: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, authorizedUUID: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, channels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[ChannelGrant](../../com.pubnub.api.models.consumer.access_manager.v3/-channel-grant/index.md)&gt;, channelGroups: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[ChannelGroupGrant](../../com.pubnub.api.models.consumer.access_manager.v3/-channel-group-grant/index.md)&gt;, uuids: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[UUIDGrant](../../com.pubnub.api.models.consumer.access_manager.v3/-u-u-i-d-grant/index.md)&gt;) |

## Functions

| Name | Summary |
|---|---|
| [async](index.md#2116719855%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [async](index.md#2116719855%2FFunctions%2F-1216412040)(callback: (result: [PNGrantTokenResult](../../com.pubnub.api.models.consumer.access_manager.v3/-p-n-grant-token-result/index.md)?, status: [PNStatus](../../com.pubnub.api.models.consumer/-p-n-status/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))<br>Executes the call asynchronously. This function does not block the thread. |
| [operationType](operation-type.md) | [jvm]<br>open override fun [operationType](operation-type.md)(): [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](../../com.pubnub.api/-endpoint/retry.md) | [jvm]<br>open override fun [retry](../../com.pubnub.api/-endpoint/retry.md)() |
| [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md) | [jvm]<br>open override fun [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md)()<br>Cancel the operation but do not alert anybody, useful for restarting the heartbeats and subscribe loops. |
| [sync](../../com.pubnub.api/-endpoint/sync.md) | [jvm]<br>open override fun [sync](../../com.pubnub.api/-endpoint/sync.md)(): [PNGrantTokenResult](../../com.pubnub.api.models.consumer.access_manager.v3/-p-n-grant-token-result/index.md)?<br>Executes the call synchronously. This function blocks the thread. |

## Properties

| Name | Summary |
|---|---|
| [queryParam](../../com.pubnub.api/-endpoint/query-param.md) | [jvm]<br>val [queryParam](../../com.pubnub.api/-endpoint/query-param.md): [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;<br>Key-value object to pass with every PubNub API operation. Used for debugging purposes. todo: it should be removed! |
| [ttl](ttl.md) | [jvm]<br>val [ttl](ttl.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
