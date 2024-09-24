//[pubnub-kotlin-api](../../../../index.md)/[com.pubnub.api.retry](../../index.md)/[RetryConfiguration](../index.md)/[Exponential](index.md)

# Exponential

[common]\
class [Exponential](index.md) : [RetryConfiguration](../index.md)

This class represents an exponential retry policy with a minimum and maximum delay between retries, a maximum number of retries, and a list of operations to exclude from retry attempts.

## Constructors

| | |
|---|---|
| [Exponential](-exponential.md) | [common]<br>constructor(minDelayInSec: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = MIN_DELAY, maxDelayInSec: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = MAX_DELAY, maxRetryNumber: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = MAX_RETRIES, excludedOperations: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[RetryableEndpointGroup](../../-retryable-endpoint-group/index.md)&gt; = emptyList())constructor()constructor(minDelayInSec: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), maxDelayInSec: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))constructor(minDelayInSec: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), maxDelayInSec: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), maxRetryNumber: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [excludedOperations](excluded-operations.md) | [common]<br>val [excludedOperations](excluded-operations.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[RetryableEndpointGroup](../../-retryable-endpoint-group/index.md)&gt;<br>List of [RetryableEndpointGroup](../../-retryable-endpoint-group/index.md) to be excluded from retry. |
| [maxDelayInSec](max-delay-in-sec.md) | [common]<br>var [maxDelayInSec](max-delay-in-sec.md): [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html)<br>The maximum delay in seconds between retries. |
| [maxRetryNumber](max-retry-number.md) | [common]<br>var [maxRetryNumber](max-retry-number.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The maximum number of retries allowed. Maximum value is 10. |
| [minDelayInSec](min-delay-in-sec.md) | [common]<br>var [minDelayInSec](min-delay-in-sec.md): [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html)<br>The minimum delay in seconds between retries. Minimum value is 3 seconds. |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [common]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [common]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
