//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.endpoints.presence](../index.md)/[HereNow](index.md)

# HereNow

interface [HereNow](index.md) : [Endpoint](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&gt; 

Obtain information about the current state of channels including a list of unique user IDs currently subscribed and the total occupancy count. 

#### See also

| |
|---|
| [PNHereNowResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.presence/-p-n-here-now-result/index.md) |

## Functions

| Name | Summary |
|---|---|
| [async](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#1418965989%2FFunctions%2F126356644) | [jvm]<br>abstract fun [async](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#1418965989%2FFunctions%2F126356644)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;Output&gt;&gt;) |
| [channelGroups](channel-groups.md) | [jvm]<br>abstract fun [channelGroups](channel-groups.md)(channelGroups: [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)&gt;): [HereNow](index.md)<br>Set the channel groups to query for presence information. |
| [channels](channels.md) | [jvm]<br>abstract fun [channels](channels.md)(channels: [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)&gt;): [HereNow](index.md)<br>Set the channels to query for presence information. |
| [includeState](include-state.md) | [jvm]<br>abstract fun [includeState](include-state.md)(includeState: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)): [HereNow](index.md)<br>Whether the response should include presence state information, if available. |
| [includeUUIDs](include-u-u-i-ds.md) | [jvm]<br>abstract fun [includeUUIDs](include-u-u-i-ds.md)(includeUUIDs: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)): [HereNow](index.md)<br>Include the list of UUIDs currently present in each channel. |
| [limit](limit.md) | [jvm]<br>abstract fun [limit](limit.md)(limit: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [HereNow](index.md)<br>Set the maximum number of occupants to return per channel. |
| [offset](offset.md) | [jvm]<br>abstract fun [offset](offset.md)(offset: [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)): [HereNow](index.md)<br>Set the zero-based starting index for pagination through occupants. |
| [operationType](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#1414065386%2FFunctions%2F126356644) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#1414065386%2FFunctions%2F126356644)(): [PNOperationType](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [overrideConfiguration](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#424483198%2FFunctions%2F126356644) | [jvm]<br>abstract fun [overrideConfiguration](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#424483198%2FFunctions%2F126356644)(configuration: [PNConfiguration](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.java.endpoints/-endpoint/index.md)&gt;<br>Allows to override certain configuration options (see [com.pubnub.api.v2.PNConfigurationOverride.Builder](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md)) for this request only. |
| [retry](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#2020801116%2FFunctions%2F126356644) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#2020801116%2FFunctions%2F126356644)() |
| [silentCancel](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#-675955969%2FFunctions%2F126356644) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#-675955969%2FFunctions%2F126356644)() |
| [sync](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#40193115%2FFunctions%2F126356644) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.java.v2.endpoints.pubsub/-signal-builder/index.md#40193115%2FFunctions%2F126356644)(): Output |
