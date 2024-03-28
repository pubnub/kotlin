//[pubnub-gson](../../../index.md)/[com.pubnub.api.endpoints.files](../index.md)/[SendFile](index.md)

# SendFile

[jvm]\
interface [SendFile](index.md) : [Endpoint](../../com.pubnub.api.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.endpoints/-endpoint/index.md)&gt;

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [jvm]<br>interface [Builder](-builder/index.md) : [BuilderSteps.ChannelStep](../../com.pubnub.api.endpoints/-builder-steps/-channel-step/index.md)&lt;[T](../../com.pubnub.api.endpoints/-builder-steps/-channel-step/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [async](../-download-file/index.md#1418965989%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [async](../-download-file/index.md#1418965989%2FFunctions%2F-395131529)(p: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../pubnub-gson/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&gt;&gt;) |
| [cipherKey](cipher-key.md) | [jvm]<br>abstract fun [cipherKey](cipher-key.md)(cipherKey: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [SendFile](index.md) |
| [message](message.md) | [jvm]<br>abstract fun [message](message.md)(message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [SendFile](index.md) |
| [meta](meta.md) | [jvm]<br>abstract fun [meta](meta.md)(meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [SendFile](index.md) |
| [operationType](../-download-file/index.md#1414065386%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [operationType](../-download-file/index.md#1414065386%2FFunctions%2F-395131529)(): [PNOperationType](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](../-download-file/index.md#2020801116%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [retry](../-download-file/index.md#2020801116%2FFunctions%2F-395131529)() |
| [shouldStore](should-store.md) | [jvm]<br>abstract fun [shouldStore](should-store.md)(shouldStore: [Boolean](https://docs.oracle.com/javase/8/docs/api/java/lang/Boolean.html)): [SendFile](index.md) |
| [silentCancel](../-download-file/index.md#-675955969%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [silentCancel](../-download-file/index.md#-675955969%2FFunctions%2F-395131529)() |
| [sync](../-download-file/index.md#40193115%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [sync](../-download-file/index.md#40193115%2FFunctions%2F-395131529)(): [Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md) |
| [ttl](ttl.md) | [jvm]<br>abstract fun [ttl](ttl.md)(ttl: [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)): [SendFile](index.md) |
