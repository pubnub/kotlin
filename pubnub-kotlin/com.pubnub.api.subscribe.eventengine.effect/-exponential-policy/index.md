//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.subscribe.eventengine.effect](../index.md)/[ExponentialPolicy](index.md)

# ExponentialPolicy

[jvm]\
class [ExponentialPolicy](index.md)(maxRetries: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 5) : [RetryPolicy](../-retry-policy/index.md)

## Constructors

| | |
|---|---|
| [ExponentialPolicy](-exponential-policy.md) | [jvm]<br>fun [ExponentialPolicy](-exponential-policy.md)(maxRetries: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 5) |

## Functions

| Name | Summary |
|---|---|
| [nextDelay](../-retry-policy/next-delay.md) | [jvm]<br>fun [nextDelay](../-retry-policy/next-delay.md)(attempt: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html)? |
