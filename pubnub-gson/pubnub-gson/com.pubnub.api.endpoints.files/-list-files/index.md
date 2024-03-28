//[pubnub-gson](../../../index.md)/[com.pubnub.api.endpoints.files](../index.md)/[ListFiles](index.md)

# ListFiles

[jvm]\
interface [ListFiles](index.md) : [Endpoint](../../com.pubnub.api.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.endpoints/-endpoint/index.md)&gt;

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [jvm]<br>interface [Builder](-builder/index.md) : [BuilderSteps.ChannelStep](../../com.pubnub.api.endpoints/-builder-steps/-channel-step/index.md)&lt;[T](../../com.pubnub.api.endpoints/-builder-steps/-channel-step/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [async](../-download-file/index.md#1418965989%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [async](../-download-file/index.md#1418965989%2FFunctions%2F-395131529)(p: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../pubnub-gson/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&gt;&gt;) |
| [limit](limit.md) | [jvm]<br>abstract fun [limit](limit.md)(limit: [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)): [ListFiles](index.md) |
| [next](next.md) | [jvm]<br>abstract fun [next](next.md)(next: [PNPage.PNNext](../../../../pubnub-kotlin/com.pubnub.api.models.consumer.objects/-p-n-page/-p-n-next/index.md)): [ListFiles](index.md) |
| [operationType](../-download-file/index.md#1414065386%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [operationType](../-download-file/index.md#1414065386%2FFunctions%2F-395131529)(): [PNOperationType](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](../-download-file/index.md#2020801116%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [retry](../-download-file/index.md#2020801116%2FFunctions%2F-395131529)() |
| [silentCancel](../-download-file/index.md#-675955969%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [silentCancel](../-download-file/index.md#-675955969%2FFunctions%2F-395131529)() |
| [sync](../-download-file/index.md#40193115%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [sync](../-download-file/index.md#40193115%2FFunctions%2F-395131529)(): [Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md) |
