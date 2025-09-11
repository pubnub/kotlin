//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.logging](../index.md)/[CustomLogger](index.md)

# CustomLogger

[jvm]\
interface [CustomLogger](index.md)

Interface for custom logger implementations. Provides both string-based and structured LogMessage-based logging.

## Properties

| Name | Summary |
|---|---|
| [name](name.md) | [jvm]<br>open val [name](name.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>The name of this logger implementation. |

## Functions

| Name | Summary |
|---|---|
| [debug](debug.md) | [jvm]<br>open fun [debug](debug.md)(logMessage: [LogMessage](../-log-message/index.md))<br>Log a debug message with structured data.<br>[jvm]<br>open fun [debug](debug.md)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?)<br>Log a debug message with a string. |
| [error](error.md) | [jvm]<br>open fun [error](error.md)(logMessage: [LogMessage](../-log-message/index.md))<br>Log an error message with structured data.<br>[jvm]<br>open fun [error](error.md)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?)<br>Log an error message with a string. |
| [info](info.md) | [jvm]<br>open fun [info](info.md)(logMessage: [LogMessage](../-log-message/index.md))<br>Log an info message with structured data.<br>[jvm]<br>open fun [info](info.md)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?)<br>Log an info message with a string. |
| [trace](trace.md) | [jvm]<br>open fun [trace](trace.md)(logMessage: [LogMessage](../-log-message/index.md))<br>Log a trace message with structured data.<br>[jvm]<br>open fun [trace](trace.md)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?)<br>Log a trace message with a string. |
| [warn](warn.md) | [jvm]<br>open fun [warn](warn.md)(logMessage: [LogMessage](../-log-message/index.md))<br>Log a warning message with structured data.<br>[jvm]<br>open fun [warn](warn.md)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?)<br>Log a warning message with a string. |
