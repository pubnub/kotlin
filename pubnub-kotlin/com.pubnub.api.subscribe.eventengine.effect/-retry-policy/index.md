//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.subscribe.eventengine.effect](../index.md)/[RetryPolicy](index.md)

# RetryPolicy

[jvm]\
abstract class [RetryPolicy](index.md)

## Constructors

| | |
|---|---|
| [RetryPolicy](-retry-policy.md) | [jvm]<br>fun [RetryPolicy](-retry-policy.md)() |

## Functions

| Name | Summary |
|---|---|
| [nextDelay](next-delay.md) | [jvm]<br>fun [nextDelay](next-delay.md)(attempt: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html)? |

## Inheritors

| Name |
|---|
| [NoRetriesPolicy](../-no-retries-policy/index.md) |
| [LinearPolicy](../-linear-policy/index.md) |
| [ExponentialPolicy](../-exponential-policy/index.md) |
