//[pubnub-kotlin-core-api](../../../../index.md)/[com.pubnub.api.retry](../../index.md)/[RetryConfiguration](../index.md)/[Linear](index.md)

# Linear

[common]\
class [Linear](index.md) : [RetryConfiguration](../index.md)

This data class represents a linear retry policy for network requests with a delay between retries, a maximum number of retries and a list of operations to exclude from retries.

## Constructors

| | |
|---|---|
| [Linear](-linear.md) | [common]<br>constructor(delayInSec: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = MIN_DELAY, maxRetryNumber: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = MAX_RETRIES, excludedOperations: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[RetryableEndpointGroup](../../-retryable-endpoint-group/index.md)&gt; = emptyList())constructor()constructor(delayInSec: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html))constructor(delayInSec: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), maxRetryNumber: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [delayInSec](delay-in-sec.md) | [common]<br>var [delayInSec](delay-in-sec.md): [Duration](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.time/-duration/index.html)<br>The delay in seconds between retries. Minimum value is 3 seconds. |
| [excludedOperations](excluded-operations.md) | [common]<br>val [excludedOperations](excluded-operations.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[RetryableEndpointGroup](../../-retryable-endpoint-group/index.md)&gt;<br>List of [RetryableEndpointGroup](../../-retryable-endpoint-group/index.md) to be excluded from retry. |
| [maxRetryNumber](max-retry-number.md) | [common]<br>var [maxRetryNumber](max-retry-number.md): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)<br>The maximum number of retries allowed. Maximum value is 10. |
