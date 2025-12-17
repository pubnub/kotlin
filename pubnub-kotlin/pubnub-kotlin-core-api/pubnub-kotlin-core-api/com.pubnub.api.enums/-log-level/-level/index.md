//[pubnub-kotlin-core-api](../../../../index.md)/[com.pubnub.api.enums](../../index.md)/[LogLevel](../index.md)/[Level](index.md)

# Level

[apple]\
enum [Level](index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[LogLevel.Level](index.md)&gt; 

Individual log level flags that can be combined. Each level corresponds to a specific bitmask in the Swift SDK.

## Entries

| | |
|---|---|
| [NONE](-n-o-n-e/index.md) | [apple]<br>[NONE](-n-o-n-e/index.md)<br>Logging is disabled (bitmask: 0). Default setting. |
| [TRACE](-t-r-a-c-e/index.md) | [apple]<br>[TRACE](-t-r-a-c-e/index.md)<br>Internal operations: method calls, state-machine transitions, detailed execution flow. Bitmask: 1 << 0 |
| [DEBUG](-d-e-b-u-g/index.md) | [apple]<br>[DEBUG](-d-e-b-u-g/index.md)<br>User inputs, API parameters, HTTP requests and responses, operation results. Bitmask: 1 << 1 |
| [INFO](-i-n-f-o/index.md) | [apple]<br>[INFO](-i-n-f-o/index.md)<br>Significant events including successful initialization and configuration changes. Bitmask: 1 << 2 |
| [EVENT](-e-v-e-n-t/index.md) | [apple]<br>[EVENT](-e-v-e-n-t/index.md)<br>Internal PubNub operations or events. Bitmask: 1 << 3 |
| [WARN](-w-a-r-n/index.md) | [apple]<br>[WARN](-w-a-r-n/index.md)<br>Unusual conditions and non-breaking validation warnings. Bitmask: 1 << 4 |
| [ERROR](-e-r-r-o-r/index.md) | [apple]<br>[ERROR](-e-r-r-o-r/index.md)<br>Errors, exceptions, and configuration conflicts. Bitmask: 1 << 5 |
| [ALL](-a-l-l/index.md) | [apple]<br>[ALL](-a-l-l/index.md)<br>All log levels will be captured. Bitmask: UInt32.max |

## Properties

| Name | Summary |
|---|---|
| [entries](entries.md) | [apple]<br>val [entries](entries.md): [EnumEntries](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.enums/-enum-entries/index.html)&lt;[LogLevel.Level](index.md)&gt;<br>Returns a representation of an immutable list of all enum entries, in the order they're declared. |
| [name](-a-l-l/index.md#-372974862%2FProperties%2F1581906243) | [apple]<br>val [name](-a-l-l/index.md#-372974862%2FProperties%2F1581906243): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](-a-l-l/index.md#-739389684%2FProperties%2F1581906243) | [apple]<br>val [ordinal](-a-l-l/index.md#-739389684%2FProperties%2F1581906243): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [value](value.md) | [apple]<br>val [value](value.md): <!---  GfmCommand {"@class":"org.jetbrains.dokka.gfm.ResolveLinkGfmCommand","dri":{"packageName":"","classNames":"<Error class: unknown class>","callable":null,"target":{"@class":"org.jetbrains.dokka.links.PointingToDeclaration"},"extra":null}} --->&lt;Error class: unknown class&gt;<!--- ---> |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [apple]<br>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [LogLevel.Level](index.md)<br>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [apple]<br>fun [values](values.md)(): [Array](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-array/index.html)&lt;[LogLevel.Level](index.md)&gt;<br>Returns an array containing the constants of this enum type, in the order they're declared. |
