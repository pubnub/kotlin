//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.endpoints.channel_groups](../index.md)/[AllChannelsChannelGroup](index.md)

# AllChannelsChannelGroup

[jvm]\
class [AllChannelsChannelGroup](index.md) : [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[Envelope](../../com.pubnub.api.models.server/-envelope/index.md)&lt;[Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;&gt;, [PNChannelGroupsAllChannelsResult](../../com.pubnub.api.models.consumer.channel_group/-p-n-channel-groups-all-channels-result/index.md)&gt;

## See also

jvm

| | |
|---|---|
| [com.pubnub.api.PubNub](../../com.pubnub.api/-pub-nub/list-channels-for-channel-group.md) |  |

## Functions

| Name | Summary |
|---|---|
| [async](index.md#-1860530084%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [async](index.md#-1860530084%2FFunctions%2F-1216412040)(callback: (result: [PNChannelGroupsAllChannelsResult](../../com.pubnub.api.models.consumer.channel_group/-p-n-channel-groups-all-channels-result/index.md)?, status: [PNStatus](../../com.pubnub.api.models.consumer/-p-n-status/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))<br>Executes the call asynchronously. This function does not block the thread. |
| [operationType](operation-type.md) | [jvm]<br>open override fun [operationType](operation-type.md)(): [PNOperationType.PNChannelsForGroupOperation](../../com.pubnub.api.enums/-p-n-operation-type/-p-n-channels-for-group-operation/index.md) |
| [retry](../../com.pubnub.api/-endpoint/retry.md) | [jvm]<br>open override fun [retry](../../com.pubnub.api/-endpoint/retry.md)() |
| [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md) | [jvm]<br>open override fun [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md)()<br>Cancel the operation but do not alert anybody, useful for restarting the heartbeats and subscribe loops. |
| [sync](../../com.pubnub.api/-endpoint/sync.md) | [jvm]<br>open override fun [sync](../../com.pubnub.api/-endpoint/sync.md)(): [PNChannelGroupsAllChannelsResult](../../com.pubnub.api.models.consumer.channel_group/-p-n-channel-groups-all-channels-result/index.md)?<br>Executes the call synchronously. This function blocks the thread. |

## Properties

| Name | Summary |
|---|---|
| [channelGroup](channel-group.md) | [jvm]<br>val [channelGroup](channel-group.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [queryParam](../../com.pubnub.api/-endpoint/query-param.md) | [jvm]<br>val [queryParam](../../com.pubnub.api/-endpoint/query-param.md): [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;<br>Key-value object to pass with every PubNub API operation. Used for debugging purposes. todo: it should be removed! |
