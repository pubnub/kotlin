//[pubnub-kotlin](../../../index.md)/[com.pubnub.api](../index.md)/[PubNubException](index.md)

# PubNubException

[jvm]\
data class [PubNubException](index.md)(val errorMessage: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, val pubnubError: [PubNubError](../-pub-nub-error/index.md)? = null, val jso: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, val statusCode: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, val affectedCall: Call&lt;*&gt;? = null, val retryAfterHeaderValue: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null) : [Exception](https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html)

Custom exception wrapper for errors occurred during execution or processing of a PubNub API operation.

## Constructors

| | |
|---|---|
| [PubNubException](-pub-nub-exception.md) | [jvm]<br>fun [PubNubException](-pub-nub-exception.md)(errorMessage: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, pubnubError: [PubNubError](../-pub-nub-error/index.md)? = null, jso: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, statusCode: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, affectedCall: Call&lt;*&gt;? = null, retryAfterHeaderValue: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null) |

## Functions

| Name | Summary |
|---|---|
| [addSuppressed](index.md#282858770%2FFunctions%2F-1216412040) | [jvm]<br>fun [addSuppressed](index.md#282858770%2FFunctions%2F-1216412040)(p0: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) |
| [fillInStackTrace](index.md#-1102069925%2FFunctions%2F-1216412040) | [jvm]<br>open fun [fillInStackTrace](index.md#-1102069925%2FFunctions%2F-1216412040)(): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html) |
| [getLocalizedMessage](index.md#1043865560%2FFunctions%2F-1216412040) | [jvm]<br>open fun [getLocalizedMessage](index.md#1043865560%2FFunctions%2F-1216412040)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getStackTrace](index.md#2050903719%2FFunctions%2F-1216412040) | [jvm]<br>open fun [getStackTrace](index.md#2050903719%2FFunctions%2F-1216412040)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[StackTraceElement](https://docs.oracle.com/javase/8/docs/api/java/lang/StackTraceElement.html)&gt; |
| [getSuppressed](index.md#672492560%2FFunctions%2F-1216412040) | [jvm]<br>fun [getSuppressed](index.md#672492560%2FFunctions%2F-1216412040)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)&gt; |
| [initCause](index.md#-418225042%2FFunctions%2F-1216412040) | [jvm]<br>open fun [initCause](index.md#-418225042%2FFunctions%2F-1216412040)(p0: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html) |
| [printStackTrace](index.md#-1769529168%2FFunctions%2F-1216412040) | [jvm]<br>open fun [printStackTrace](index.md#-1769529168%2FFunctions%2F-1216412040)()<br>open fun [printStackTrace](index.md#1841853697%2FFunctions%2F-1216412040)(p0: [PrintStream](https://docs.oracle.com/javase/8/docs/api/java/io/PrintStream.html))<br>open fun [printStackTrace](index.md#1175535278%2FFunctions%2F-1216412040)(p0: [PrintWriter](https://docs.oracle.com/javase/8/docs/api/java/io/PrintWriter.html)) |
| [setStackTrace](index.md#2135801318%2FFunctions%2F-1216412040) | [jvm]<br>open fun [setStackTrace](index.md#2135801318%2FFunctions%2F-1216412040)(p0: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[StackTraceElement](https://docs.oracle.com/javase/8/docs/api/java/lang/StackTraceElement.html)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [affectedCall](affected-call.md) | [jvm]<br>val [affectedCall](affected-call.md): Call&lt;*&gt;? = null<br>A reference to the affected call. Useful for calling [retry](../-endpoint/retry.md). |
| [cause](index.md#-654012527%2FProperties%2F-1216412040) | [jvm]<br>open val [cause](index.md#-654012527%2FProperties%2F-1216412040): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? |
| [errorMessage](error-message.md) | [jvm]<br>val [errorMessage](error-message.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null<br>The error message received from the server, if any. |
| [jso](jso.md) | [jvm]<br>val [jso](jso.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null<br>The error json received from the server, if any. |
| [message](index.md#1824300659%2FProperties%2F-1216412040) | [jvm]<br>open val [message](index.md#1824300659%2FProperties%2F-1216412040): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [pubnubError](pubnub-error.md) | [jvm]<br>val [pubnubError](pubnub-error.md): [PubNubError](../-pub-nub-error/index.md)? = null<br>The appropriate matching PubNub error. |
| [retryAfterHeaderValue](retry-after-header-value.md) | [jvm]<br>val [retryAfterHeaderValue](retry-after-header-value.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null |
| [statusCode](status-code.md) | [jvm]<br>val [statusCode](status-code.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0<br>HTTP status code. |
