//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.endpoints.push](../index.md)/[AddChannelsToPush](index.md)

# AddChannelsToPush

[jvm]\
class [AddChannelsToPush](index.md) : [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[Void](https://docs.oracle.com/javase/8/docs/api/java/lang/Void.html), [PNPushAddChannelResult](../../com.pubnub.api.models.consumer.push/-p-n-push-add-channel-result/index.md)&gt;

## See also

jvm

| | |
|---|---|
| [com.pubnub.api.PubNub](../../com.pubnub.api/-pub-nub/add-push-notifications-on-channels.md) |  |

## Functions

| Name | Summary |
|---|---|
| [async](index.md#-1369205529%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [async](index.md#-1369205529%2FFunctions%2F-1216412040)(callback: (result: [PNPushAddChannelResult](../../com.pubnub.api.models.consumer.push/-p-n-push-add-channel-result/index.md)?, status: [PNStatus](../../com.pubnub.api.models.consumer/-p-n-status/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))<br>Executes the call asynchronously. This function does not block the thread. |
| [operationType](operation-type.md) | [jvm]<br>open override fun [operationType](operation-type.md)(): [PNOperationType.PNAddPushNotificationsOnChannelsOperation](../../com.pubnub.api.enums/-p-n-operation-type/-p-n-add-push-notifications-on-channels-operation/index.md) |
| [retry](../../com.pubnub.api/-endpoint/retry.md) | [jvm]<br>open override fun [retry](../../com.pubnub.api/-endpoint/retry.md)() |
| [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md) | [jvm]<br>open override fun [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md)()<br>Cancel the operation but do not alert anybody, useful for restarting the heartbeats and subscribe loops. |
| [sync](../../com.pubnub.api/-endpoint/sync.md) | [jvm]<br>open override fun [sync](../../com.pubnub.api/-endpoint/sync.md)(): [PNPushAddChannelResult](../../com.pubnub.api.models.consumer.push/-p-n-push-add-channel-result/index.md)?<br>Executes the call synchronously. This function blocks the thread. |

## Properties

| Name | Summary |
|---|---|
| [channels](channels.md) | [jvm]<br>val [channels](channels.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [deviceId](device-id.md) | [jvm]<br>val [deviceId](device-id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [environment](environment.md) | [jvm]<br>val [environment](environment.md): [PNPushEnvironment](../../com.pubnub.api.enums/-p-n-push-environment/index.md) |
| [pushType](push-type.md) | [jvm]<br>val [pushType](push-type.md): [PNPushType](../../com.pubnub.api.enums/-p-n-push-type/index.md) |
| [queryParam](../../com.pubnub.api/-endpoint/query-param.md) | [jvm]<br>val [queryParam](../../com.pubnub.api/-endpoint/query-param.md): [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;<br>Key-value object to pass with every PubNub API operation. Used for debugging purposes. todo: it should be removed! |
| [topic](topic.md) | [jvm]<br>val [topic](topic.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null |
