//[pubnub-kotlin-core-api](../../../../index.md)/[com.pubnub.api.logging](../../index.md)/[NetworkLog](../index.md)/[Request](index.md)

# Request

[jvm]\
data class [Request](index.md)(val message: [NetworkRequestMessage](../../-network-request-message/index.md), val canceled: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false, val failed: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false) : [NetworkLog](../index.md)

## Constructors

| | |
|---|---|
| [Request](-request.md) | [jvm]<br>constructor(message: [NetworkRequestMessage](../../-network-request-message/index.md), canceled: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false, failed: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false) |

## Properties

| Name | Summary |
|---|---|
| [canceled](canceled.md) | [jvm]<br>@SerializedName(value = &quot;canceled&quot;)<br>val [canceled](canceled.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false |
| [failed](failed.md) | [jvm]<br>@SerializedName(value = &quot;failed&quot;)<br>val [failed](failed.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false |
| [message](message.md) | [jvm]<br>@SerializedName(value = &quot;message&quot;)<br>val [message](message.md): [NetworkRequestMessage](../../-network-request-message/index.md) |
