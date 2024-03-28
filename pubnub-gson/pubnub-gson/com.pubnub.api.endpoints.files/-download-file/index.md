//[pubnub-gson](../../../index.md)/[com.pubnub.api.endpoints.files](../index.md)/[DownloadFile](index.md)

# DownloadFile

[jvm]\
interface [DownloadFile](index.md) : [Endpoint](../../com.pubnub.api.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.endpoints/-endpoint/index.md)&gt;

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [jvm]<br>interface [Builder](-builder/index.md) : [BuilderSteps.ChannelStep](../../com.pubnub.api.endpoints/-builder-steps/-channel-step/index.md)&lt;[T](../../com.pubnub.api.endpoints/-builder-steps/-channel-step/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [async](index.md#1418965989%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [async](index.md#1418965989%2FFunctions%2F-395131529)(p: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../pubnub-gson/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&gt;&gt;) |
| [cipherKey](cipher-key.md) | [jvm]<br>abstract fun [cipherKey](cipher-key.md)(cipherKey: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [DownloadFile](index.md) |
| [operationType](index.md#1414065386%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [operationType](index.md#1414065386%2FFunctions%2F-395131529)(): [PNOperationType](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](index.md#2020801116%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [retry](index.md#2020801116%2FFunctions%2F-395131529)() |
| [silentCancel](index.md#-675955969%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [silentCancel](index.md#-675955969%2FFunctions%2F-395131529)() |
| [sync](index.md#40193115%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [sync](index.md#40193115%2FFunctions%2F-395131529)(): [Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md) |
