//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.subscribe.eventengine.effect](../index.md)/[LinearPolicy](index.md)

# LinearPolicy

[jvm]\
class [LinearPolicy](index.md)(maxRetries: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 5, fixedDelay: [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html) = Duration.ofSeconds(3)) : [RetryPolicy](../-retry-policy/index.md)

## Constructors

| | |
|---|---|
| [LinearPolicy](-linear-policy.md) | [jvm]<br>fun [LinearPolicy](-linear-policy.md)(maxRetries: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 5, fixedDelay: [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html) = Duration.ofSeconds(3)) |

## Functions

| Name | Summary |
|---|---|
| [nextDelay](../-retry-policy/next-delay.md) | [jvm]<br>fun [nextDelay](../-retry-policy/next-delay.md)(attempt: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html)? |
