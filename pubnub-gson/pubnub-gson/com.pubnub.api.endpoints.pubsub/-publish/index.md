//[pubnub-gson](../../../index.md)/[com.pubnub.api.endpoints.pubsub](../index.md)/[Publish](index.md)

# Publish

[jvm]\
interface [Publish](index.md) : [Endpoint](../../com.pubnub.api.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.endpoints/-endpoint/index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [async](../../com.pubnub.api.endpoints.files/-download-file/index.md#1418965989%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [async](../../com.pubnub.api.endpoints.files/-download-file/index.md#1418965989%2FFunctions%2F-395131529)(p: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../pubnub-gson/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&gt;&gt;) |
| [channel](channel.md) | [jvm]<br>abstract fun [channel](channel.md)(channel: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [Publish](index.md) |
| [message](message.md) | [jvm]<br>abstract fun [message](message.md)(message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [Publish](index.md) |
| [meta](meta.md) | [jvm]<br>abstract fun [meta](meta.md)(meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [Publish](index.md) |
| [operationType](../../com.pubnub.api.endpoints.files/-download-file/index.md#1414065386%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.files/-download-file/index.md#1414065386%2FFunctions%2F-395131529)(): [PNOperationType](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [replicate](replicate.md) | [jvm]<br>abstract fun [replicate](replicate.md)(replicate: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [Publish](index.md) |
| [retry](../../com.pubnub.api.endpoints.files/-download-file/index.md#2020801116%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.files/-download-file/index.md#2020801116%2FFunctions%2F-395131529)() |
| [shouldStore](should-store.md) | [jvm]<br>abstract fun [shouldStore](should-store.md)(shouldStore: [Boolean](https://docs.oracle.com/javase/8/docs/api/java/lang/Boolean.html)): [Publish](index.md) |
| [silentCancel](../../com.pubnub.api.endpoints.files/-download-file/index.md#-675955969%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.files/-download-file/index.md#-675955969%2FFunctions%2F-395131529)() |
| [sync](../../com.pubnub.api.endpoints.files/-download-file/index.md#40193115%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.files/-download-file/index.md#40193115%2FFunctions%2F-395131529)(): [Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md) |
| [ttl](ttl.md) | [jvm]<br>abstract fun [ttl](ttl.md)(ttl: [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)): [Publish](index.md) |
| [usePOST](use-p-o-s-t.md) | [jvm]<br>abstract fun [usePOST](use-p-o-s-t.md)(usePOST: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [Publish](index.md) |
