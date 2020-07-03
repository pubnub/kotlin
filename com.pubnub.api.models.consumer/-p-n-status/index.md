---
title: PNStatus - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.models.consumer](../index.html) / [PNStatus](./index.html)

# PNStatus

`data class PNStatus`

### Constructors

| [&lt;init&gt;](-init-.html) | `PNStatus(category: `[`PNStatusCategory`](../../com.pubnub.api.enums/-p-n-status-category/index.html)`, error: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, operation: `[`PNOperationType`](../../com.pubnub.api.enums/-p-n-operation-type/index.html)`, exception: `[`PubNubException`](../../com.pubnub.api/-pub-nub-exception/index.html)`? = null, statusCode: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, tlsEnabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`? = null, origin: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, uuid: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, authKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, affectedChannels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?> = emptyList(), affectedChannelGroups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?> = emptyList())` |

### Properties

| [affectedChannelGroups](affected-channel-groups.html) | `var affectedChannelGroups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?>` |
| [affectedChannels](affected-channels.html) | `var affectedChannels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?>` |
| [authKey](auth-key.html) | `var authKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [category](category.html) | `var category: `[`PNStatusCategory`](../../com.pubnub.api.enums/-p-n-status-category/index.html) |
| [clientRequest](client-request.html) | `var clientRequest: Request?` |
| [error](error.html) | `var error: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [exception](exception.html) | `val exception: `[`PubNubException`](../../com.pubnub.api/-pub-nub-exception/index.html)`?` |
| [executedEndpoint](executed-endpoint.html) | `var executedEndpoint: `[`Endpoint`](../../com.pubnub.api/-endpoint/index.html)`<*, *>?` |
| [operation](operation.html) | `val operation: `[`PNOperationType`](../../com.pubnub.api.enums/-p-n-operation-type/index.html) |
| [origin](origin.html) | `var origin: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [statusCode](status-code.html) | `var statusCode: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [tlsEnabled](tls-enabled.html) | `var tlsEnabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?` |
| [uuid](uuid.html) | `var uuid: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |

### Functions

| [retry](retry.html) | `fun retry(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

