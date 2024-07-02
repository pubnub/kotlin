//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.endpoints.objects_api.memberships](../index.md)/[ManageMemberships](index.md)

# ManageMemberships

[jvm]\
interface [ManageMemberships](index.md) : [Endpoint](../../com.pubnub.api.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.endpoints/-endpoint/index.md)&gt;

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [jvm]<br>interface [Builder](-builder/index.md) : [ObjectsBuilderSteps.RemoveOrSetStep](../../com.pubnub.api.endpoints.objects_api.utils/-objects-builder-steps/-remove-or-set-step/index.md)&lt;[T](../../com.pubnub.api.endpoints.objects_api.utils/-objects-builder-steps/-remove-or-set-step/index.md), [E](../../com.pubnub.api.endpoints.objects_api.utils/-objects-builder-steps/-remove-or-set-step/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [async](../../com.pubnub.api.endpoints.channel_groups/-list-all-channel-group/index.md#1418965989%2FFunctions%2F126356644) | [jvm]<br>abstract fun [async](../../com.pubnub.api.endpoints.channel_groups/-list-all-channel-group/index.md#1418965989%2FFunctions%2F126356644)(callback: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[Result](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&gt;&gt;) |
| [filter](filter.md) | [jvm]<br>abstract fun [filter](filter.md)(filter: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [ManageMemberships](index.md) |
| [includeChannel](include-channel.md) | [jvm]<br>abstract fun [includeChannel](include-channel.md)(includeChannel: [Include.PNChannelDetailsLevel](../../com.pubnub.api.endpoints.objects_api.utils/-include/-p-n-channel-details-level/index.md)): [ManageMemberships](index.md) |
| [includeCustom](include-custom.md) | [jvm]<br>abstract fun [includeCustom](include-custom.md)(includeCustom: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [ManageMemberships](index.md) |
| [includeTotalCount](include-total-count.md) | [jvm]<br>abstract fun [includeTotalCount](include-total-count.md)(includeTotalCount: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [ManageMemberships](index.md) |
| [limit](limit.md) | [jvm]<br>abstract fun [limit](limit.md)(limit: [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)): [ManageMemberships](index.md) |
| [operationType](../../com.pubnub.api.endpoints.channel_groups/-list-all-channel-group/index.md#1414065386%2FFunctions%2F126356644) | [jvm]<br>abstract fun [operationType](../../com.pubnub.api.endpoints.channel_groups/-list-all-channel-group/index.md#1414065386%2FFunctions%2F126356644)(): [PNOperationType](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [overrideConfiguration](../../com.pubnub.api.endpoints/-endpoint/override-configuration.md) | [jvm]<br>abstract fun [overrideConfiguration](../../com.pubnub.api.endpoints/-endpoint/override-configuration.md)(configuration: [PNConfiguration](../../com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](../../com.pubnub.api.endpoints/-endpoint/index.md)&lt;[T](../../com.pubnub.api.endpoints/-endpoint/index.md)&gt;<br>Allows to override certain configuration options (see [com.pubnub.api.v2.BasePNConfigurationOverride.Builder](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2/-base-p-n-configuration-override/-builder/index.md)) for this request only. |
| [page](page.md) | [jvm]<br>abstract fun [page](page.md)(page: [PNPage](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.objects/-p-n-page/index.md)): [ManageMemberships](index.md) |
| [remove](remove.md) | [jvm]<br>abstract fun [remove](remove.md)(remove: [Collection](https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html)&lt;[PNChannelMembership](../../com.pubnub.api.models.consumer.objects_api.membership/-p-n-channel-membership/index.md)&gt;): [ManageMemberships](index.md) |
| [retry](../../com.pubnub.api.endpoints.channel_groups/-list-all-channel-group/index.md#2020801116%2FFunctions%2F126356644) | [jvm]<br>abstract fun [retry](../../com.pubnub.api.endpoints.channel_groups/-list-all-channel-group/index.md#2020801116%2FFunctions%2F126356644)() |
| [set](set.md) | [jvm]<br>abstract fun [set](set.md)(set: [Collection](https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html)&lt;[PNChannelMembership](../../com.pubnub.api.models.consumer.objects_api.membership/-p-n-channel-membership/index.md)&gt;): [ManageMemberships](index.md) |
| [silentCancel](../../com.pubnub.api.endpoints.channel_groups/-list-all-channel-group/index.md#-675955969%2FFunctions%2F126356644) | [jvm]<br>abstract fun [silentCancel](../../com.pubnub.api.endpoints.channel_groups/-list-all-channel-group/index.md#-675955969%2FFunctions%2F126356644)() |
| [sort](sort.md) | [jvm]<br>abstract fun [sort](sort.md)(sort: [Collection](https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html)&lt;[PNSortKey](../../com.pubnub.api.endpoints.objects_api.utils/-p-n-sort-key/index.md)&gt;): [ManageMemberships](index.md) |
| [sync](../../com.pubnub.api.endpoints.channel_groups/-list-all-channel-group/index.md#40193115%2FFunctions%2F126356644) | [jvm]<br>abstract fun [sync](../../com.pubnub.api.endpoints.channel_groups/-list-all-channel-group/index.md#40193115%2FFunctions%2F126356644)(): [Output](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.endpoints.remoteaction/-remote-action/index.md) |
| [uuid](uuid.md) | [jvm]<br>abstract fun [uuid](uuid.md)(uuid: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [ManageMemberships](index.md) |
