//[pubnub-gson](../../../index.md)/[com.pubnub.api.endpoints.message_actions](../index.md)/[AddMessageAction](index.md)

# AddMessageAction

[jvm]\
interface [AddMessageAction](index.md) : [Endpoint](../../com.pubnub.api.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.endpoints/-endpoint/index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [async](../../com.pubnub.api.endpoints.files/-download-file/index.md#1418965989%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [async](../../com.pubnub.api.endpoints.files/-download-file/index.md#1418965989%2FFunctions%2F-395131529)(p: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../pubnub-gson/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&gt;&gt;) |
| [channel](channel.md) | [jvm]<br>abstract fun [channel](channel.md)(channel: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [AddMessageAction](index.md) |
| [messageAction](message-action.md) | [jvm]<br>abstract fun [messageAction](message-action.md)(messageAction: [PNMessageAction](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.md)): [AddMessageAction](index.md) |
| [operationType](../../com.pubnub.api.endpoints.files/-download-file/index.md#1414065386%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.files/-download-file/index.md#1414065386%2FFunctions%2F-395131529)(): [PNOperationType](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](../../com.pubnub.api.endpoints.files/-download-file/index.md#2020801116%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.files/-download-file/index.md#2020801116%2FFunctions%2F-395131529)() |
| [silentCancel](../../com.pubnub.api.endpoints.files/-download-file/index.md#-675955969%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.files/-download-file/index.md#-675955969%2FFunctions%2F-395131529)() |
| [sync](../../com.pubnub.api.endpoints.files/-download-file/index.md#40193115%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.files/-download-file/index.md#40193115%2FFunctions%2F-395131529)(): [Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md) |
