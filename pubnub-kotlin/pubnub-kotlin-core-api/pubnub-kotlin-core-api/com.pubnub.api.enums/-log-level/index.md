//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.enums](../index.md)/[LogLevel](index.md)

# LogLevel

expect data class [LogLevel](index.md)actual data class [LogLevel](index.md)(val levels: [Set](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[LogLevel.Level](-level/index.md)&gt;)actual data class [LogLevel](index.md)actual data class [LogLevel](index.md)

Platform-specific log level configuration.

- 
   **JVM**: This parameter is ignored. Configure logging via slf4j implementation.
- 
   **JS**: Simple hierarchical log levels (NONE, ERROR, WARN, INFO, DEBUG, TRACE).
- 
   **iOS**: Bitmask-based log levels that can be combined.

Swift SDK log level configuration.

Uses bitmask flags that can be combined to create custom logging configurations. Unlike hierarchical logging, each level must be explicitly enabled.

Example:

```kotlin
// Predefined combinations
LogLevel.Standard  // info, event, warn, error

// Custom combination
LogLevel.custom(Level.ERROR, Level.WARN, Level.EVENT)
```

JavaScript SDK log level.

JVM LogLevel (ignored - use slf4j configuration instead).

This parameter exists for API consistency across platforms but has no effect on JVM. To configure logging on JVM, add an slf4j implementation (e.g., logback, log4j2) and configure it according to that implementation's documentation.

#### See also

| | |
|---|---|
|  | [Swift SDK Logging Documentation](https://www.pubnub.com/docs/sdks/swift/logging#log-levels) |

## Constructors

| | |
|---|---|
| [LogLevel](-log-level.md) | [apple]<br>constructor(levels: [Set](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[LogLevel.Level](-level/index.md)&gt;) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common, apple, js, jvm]<br>[common]<br>expect object [Companion](-companion/index.md)<br>[apple, js, jvm]<br>actual object [Companion](-companion/index.md) |
| [Level](-level/index.md) | [apple]<br>enum [Level](-level/index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[LogLevel.Level](-level/index.md)&gt; <br>Individual log level flags that can be combined. Each level corresponds to a specific bitmask in the Swift SDK. |

## Properties

| Name | Summary |
|---|---|
| [levels](levels.md) | [apple]<br>val [levels](levels.md): [Set](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[LogLevel.Level](-level/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [equals](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-log-level/[jvm]equals.md) | [js, jvm]<br>[js]<br>open operator override fun [equals]([js]equals.md)(other: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>[jvm]<br>open operator override fun [equals]([jvm]equals.md)(other: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [hashCode](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-log-level/[jvm]hash-code.md) | [js, jvm]<br>[js]<br>open override fun [hashCode]([js]hash-code.md)(): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)<br>[jvm]<br>open override fun [hashCode]([jvm]hash-code.md)(): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [toString](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-log-level/[jvm]to-string.md) | [js, jvm]<br>[js]<br>open override fun [toString]([js]to-string.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>[jvm]<br>open override fun [toString]([jvm]to-string.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
