//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.logging](../index.md)/[NetworkLog](index.md)

# NetworkLog

sealed class [NetworkLog](index.md)

#### Inheritors

| |
|---|
| [Request](-request/index.md) |
| [Response](-response/index.md) |

## Types

| Name | Summary |
|---|---|
| [Request](-request/index.md) | [jvm]<br>data class [Request](-request/index.md)(val message: [NetworkRequestMessage](../-network-request-message/index.md), val canceled: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false, val failed: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false) : [NetworkLog](index.md) |
| [Response](-response/index.md) | [jvm]<br>data class [Response](-response/index.md)(val message: [NetworkResponseMessage](../-network-response-message/index.md)) : [NetworkLog](index.md) |
