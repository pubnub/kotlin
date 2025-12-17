//[pubnub-kotlin-core-api](../../../../index.md)/[com.pubnub.api.enums](../../index.md)/[LogLevel](../index.md)/[Companion](index.md)/[custom](custom.md)

# custom

[apple]\
fun [custom](custom.md)(vararg levels: [LogLevel.Level](../-level/index.md)): [LogLevel](../index.md)

Create custom combination of log levels.

Example:

```kotlin
val customLogs = LogLevel.custom(
    Level.ERROR,
    Level.WARN,
    Level.EVENT
)
```
