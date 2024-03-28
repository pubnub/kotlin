//[pubnub-gson](../../../index.md)/[com.pubnub.api.endpoints](../index.md)/[FetchMessages](index.md)

# FetchMessages

[jvm]\
interface [FetchMessages](index.md) : [Endpoint](../-endpoint/index.md)&lt;[T](../-endpoint/index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [async](../../com.pubnub.api.endpoints.files/-download-file/index.md#1418965989%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [async](../../com.pubnub.api.endpoints.files/-download-file/index.md#1418965989%2FFunctions%2F-395131529)(p: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../pubnub-gson/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&gt;&gt;) |
| [channels](channels.md) | [jvm]<br>abstract fun [channels](channels.md)(channels: [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)&gt;): [FetchMessages](index.md) |
| [end](end.md) | [jvm]<br>abstract fun [end](end.md)(end: [Long](https://docs.oracle.com/javase/8/docs/api/java/lang/Long.html)): [FetchMessages](index.md) |
| [includeMessageActions](include-message-actions.md) | [jvm]<br>abstract fun [includeMessageActions](include-message-actions.md)(includeMessageActions: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [FetchMessages](index.md) |
| [includeMessageType](include-message-type.md) | [jvm]<br>abstract fun [includeMessageType](include-message-type.md)(includeMessageType: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [FetchMessages](index.md) |
| [includeMeta](include-meta.md) | [jvm]<br>abstract fun [includeMeta](include-meta.md)(includeMeta: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [FetchMessages](index.md) |
| [includeUUID](include-u-u-i-d.md) | [jvm]<br>abstract fun [includeUUID](include-u-u-i-d.md)(includeUUID: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [FetchMessages](index.md) |
| [maximumPerChannel](maximum-per-channel.md) | [jvm]<br>abstract fun [maximumPerChannel](maximum-per-channel.md)(maximumPerChannel: [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)): [FetchMessages](index.md) |
| [operationType](../../com.pubnub.api.endpoints.files/-download-file/index.md#1414065386%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.files/-download-file/index.md#1414065386%2FFunctions%2F-395131529)(): [PNOperationType](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](../../com.pubnub.api.endpoints.files/-download-file/index.md#2020801116%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.files/-download-file/index.md#2020801116%2FFunctions%2F-395131529)() |
| [silentCancel](../../com.pubnub.api.endpoints.files/-download-file/index.md#-675955969%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.files/-download-file/index.md#-675955969%2FFunctions%2F-395131529)() |
| [start](start.md) | [jvm]<br>abstract fun [start](start.md)(start: [Long](https://docs.oracle.com/javase/8/docs/api/java/lang/Long.html)): [FetchMessages](index.md) |
| [sync](../../com.pubnub.api.endpoints.files/-download-file/index.md#40193115%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.files/-download-file/index.md#40193115%2FFunctions%2F-395131529)(): [Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md) |
