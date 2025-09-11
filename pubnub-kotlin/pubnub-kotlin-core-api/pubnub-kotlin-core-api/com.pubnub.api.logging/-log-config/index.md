//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.logging](../index.md)/[LogConfig](index.md)

# LogConfig

class [LogConfig](index.md)(val pnInstanceId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val userId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val customLoggers: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[CustomLogger](../-custom-logger/index.md)&gt;? = null)

Configuration for logging system with validation.

#### Parameters

jvm

| | |
|---|---|
| pnInstanceId | The PubNub instance identifier - must not be blank |
| userId | The user identifier - must not be blank |
| customLoggers | Optional list of custom loggers to use |

## Constructors

| | |
|---|---|
| [LogConfig](-log-config.md) | [jvm]<br>constructor(pnInstanceId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), userId: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), customLoggers: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[CustomLogger](../-custom-logger/index.md)&gt;? = null) |

## Properties

| Name | Summary |
|---|---|
| [customLoggers](custom-loggers.md) | [jvm]<br>val [customLoggers](custom-loggers.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[CustomLogger](../-custom-logger/index.md)&gt;? = null |
| [pnInstanceId](pn-instance-id.md) | [jvm]<br>val [pnInstanceId](pn-instance-id.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [userId](user-id.md) | [jvm]<br>val [userId](user-id.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
