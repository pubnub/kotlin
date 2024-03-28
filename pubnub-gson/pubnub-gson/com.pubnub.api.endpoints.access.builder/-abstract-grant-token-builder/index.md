//[pubnub-gson](../../../index.md)/[com.pubnub.api.endpoints.access.builder](../index.md)/[AbstractGrantTokenBuilder](index.md)

# AbstractGrantTokenBuilder

abstract class [AbstractGrantTokenBuilder](index.md)&lt;[T](index.md)&gt; : DelegatingEndpoint&lt;T&gt; 

#### Inheritors

| |
|---|
| [GrantTokenObjectsBuilder](../-grant-token-objects-builder/index.md) |
| [GrantTokenBuilder](../-grant-token-builder/index.md) |
| [GrantTokenEntitiesBuilder](../-grant-token-entities-builder/index.md) |

## Constructors

| | |
|---|---|
| [AbstractGrantTokenBuilder](-abstract-grant-token-builder.md) | [jvm]<br>constructor(pubnub: PubNubCore, grantToken: [GrantToken](../../com.pubnub.api.endpoints.access/-grant-token/index.md)) |

## Functions

| Name | Summary |
|---|---|
| [async](../../com.pubnub.api.endpoints.files/-download-file/index.md#1418965989%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [async](../../com.pubnub.api.endpoints.files/-download-file/index.md#1418965989%2FFunctions%2F-395131529)(p: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../pubnub-gson/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&gt;&gt;)<br>open fun [async](../-grant-token-entities-builder/index.md#-1510486822%2FFunctions%2F-395131529)(@NotNullcallback: @NotNull[Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../pubnub-gson/com.pubnub.api.v2.callbacks/-result/index.md)&lt;T&gt;&gt;) |
| [getOperationType](../-grant-token-entities-builder/index.md#1720561945%2FFunctions%2F-395131529) | [jvm]<br>open fun [getOperationType](../-grant-token-entities-builder/index.md#1720561945%2FFunctions%2F-395131529)(): [PNOperationType](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [operationType](../../com.pubnub.api.endpoints.files/-download-file/index.md#1414065386%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.files/-download-file/index.md#1414065386%2FFunctions%2F-395131529)(): [PNOperationType](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md)<br>@NotNull<br>open fun [operationType](../-grant-token-entities-builder/index.md#-2136612235%2FFunctions%2F-395131529)(): @NotNull[PNOperationType](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](../../com.pubnub.api.endpoints.files/-download-file/index.md#2020801116%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.files/-download-file/index.md#2020801116%2FFunctions%2F-395131529)()<br>open fun [retry](../-grant-token-entities-builder/index.md#993822702%2FFunctions%2F-395131529)() |
| [silentCancel](../../com.pubnub.api.endpoints.files/-download-file/index.md#-675955969%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.files/-download-file/index.md#-675955969%2FFunctions%2F-395131529)()<br>open fun [silentCancel](../-grant-token-entities-builder/index.md#1057725301%2FFunctions%2F-395131529)() |
| [sync](../../com.pubnub.api.endpoints.files/-download-file/index.md#40193115%2FFunctions%2F-395131529) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.files/-download-file/index.md#40193115%2FFunctions%2F-395131529)(): [Output](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)<br>open fun [sync](../-grant-token-entities-builder/index.md#2085274761%2FFunctions%2F-395131529)(): T |
