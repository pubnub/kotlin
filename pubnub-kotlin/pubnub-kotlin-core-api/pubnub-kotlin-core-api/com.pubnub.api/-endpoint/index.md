//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api](../index.md)/[Endpoint](index.md)

# Endpoint

interface [Endpoint](index.md)&lt;[OUTPUT](index.md)&gt; : [ExtendedRemoteAction](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;[OUTPUT](index.md)&gt; 

#### Inheritors

| |
|---|
| [Time](../../com.pubnub.api.endpoints/-time/index.md) |
| [ListAllChannelGroup](../../com.pubnub.api.endpoints.channel_groups/-list-all-channel-group/index.md) |
| [Signal](../../com.pubnub.api.endpoints.pubsub/-signal/index.md) |

## Functions

| Name | Summary |
|---|---|
| [async](index.md#149557464%2FFunctions%2F1141030505) | [jvm]<br>abstract override fun [async](index.md#149557464%2FFunctions%2F1141030505)(callback: [Consumer](../../com.pubnub.api.v2.callbacks/-consumer/index.md)&lt;[Result](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[OUTPUT](index.md)&gt;&gt;)<br>Run the action asynchronously, without blocking the calling thread and delivering the result through the [callback](index.md#149557464%2FFunctions%2F1141030505). |
| [operationType](../../com.pubnub.api.endpoints.pubsub/-signal/index.md#1414065386%2FFunctions%2F1141030505) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.pubsub/-signal/index.md#1414065386%2FFunctions%2F1141030505)(): [PNOperationType](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md)<br>Return the type of this operation from the values defined in [PNOperationType](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md). |
| [overrideConfiguration](override-configuration.md) | [jvm]<br>abstract fun [overrideConfiguration](override-configuration.md)(configuration: [PNConfiguration](../../com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](index.md)&lt;[OUTPUT](index.md)&gt;<br>abstract fun [overrideConfiguration](override-configuration.md)(action: [PNConfigurationOverride.Builder](../../com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)): [Endpoint](index.md)&lt;[OUTPUT](index.md)&gt;<br>Allows to override certain configuration options (see [PNConfigurationOverride.Builder](../../com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md)) for this request only. |
| [retry](../../com.pubnub.api.endpoints.pubsub/-signal/index.md#2020801116%2FFunctions%2F1141030505) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.pubsub/-signal/index.md#2020801116%2FFunctions%2F1141030505)()<br>Attempt to retry the action and deliver the result to a callback registered with a previous call to [async](index.md#149557464%2FFunctions%2F1141030505). |
| [silentCancel](../../com.pubnub.api.endpoints.pubsub/-signal/index.md#-675955969%2FFunctions%2F1141030505) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.pubsub/-signal/index.md#-675955969%2FFunctions%2F1141030505)()<br>Cancel the action without reporting any further results. |
| [sync](../../com.pubnub.api.endpoints.pubsub/-signal/index.md#40193115%2FFunctions%2F1141030505) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.pubsub/-signal/index.md#40193115%2FFunctions%2F1141030505)(): [OUTPUT](index.md)<br>Run the action synchronously, potentially blocking the calling thread. |
