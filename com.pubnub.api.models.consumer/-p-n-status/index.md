[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer](../index.md) / [PNStatus](./index.md)

# PNStatus

`data class PNStatus`

Metadata related to executed PubNub API operations.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Metadata related to executed PubNub API operations.`PNStatus(category: `[`PNStatusCategory`](../../com.pubnub.api.enums/-p-n-status-category/index.md)`, error: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, operation: `[`PNOperationType`](../../com.pubnub.api.enums/-p-n-operation-type/index.md)`, exception: `[`PubNubException`](../../com.pubnub.api/-pub-nub-exception/index.md)`? = null, statusCode: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, tlsEnabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`? = null, origin: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, uuid: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, authKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, affectedChannels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?> = emptyList(), affectedChannelGroups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?> = emptyList())` |

### Properties

| Name | Summary |
|---|---|
| [affectedChannelGroups](affected-channel-groups.md) | List of channel groups affected by the this API operation.`var affectedChannelGroups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?>` |
| [affectedChannels](affected-channels.md) | List of channels affected by the this API operation.`var affectedChannels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?>` |
| [authKey](auth-key.md) | The authentication key attached to the request, if needed. See more at [PNConfiguration.authKey](../../com.pubnub.api/-p-n-configuration/auth-key.md).`var authKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [category](category.md) | The [PNStatusCategory](../../com.pubnub.api.enums/-p-n-status-category/index.md) of an executed operation.`var category: `[`PNStatusCategory`](../../com.pubnub.api.enums/-p-n-status-category/index.md) |
| [clientRequest](client-request.md) | `var clientRequest: Request?` |
| [error](error.md) | Is `true` if the operation didn't succeed. Always check for it in [async](../../com.pubnub.api/-endpoint/async.md) blocks.`var error: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [exception](exception.md) | Error information if the request didn't succeed.`val exception: `[`PubNubException`](../../com.pubnub.api/-pub-nub-exception/index.md)`?` |
| [operation](operation.md) | The concrete API operation type that's been executed.`val operation: `[`PNOperationType`](../../com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [origin](origin.md) | Origin of the HTTP request. See more at [PNConfiguration.origin](../../com.pubnub.api/-p-n-configuration/origin.md).`var origin: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [statusCode](status-code.md) | HTTP status code.`var statusCode: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [tlsEnabled](tls-enabled.md) | Whether the API operation was executed over HTTPS.`var tlsEnabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?` |
| [uuid](uuid.md) | The UUID which requested the API operation to be executed. See more at [PNConfiguration.uuid](../../com.pubnub.api/-p-n-configuration/uuid.md).`var uuid: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [retry](retry.md) | Execute the API operation again.`fun retry(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
