//[pubnub-core-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNubException](index.md)

# PubNubException

[jvm]\
data class [PubNubException](index.md)(val errorMessage: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, val pubnubError: [PubNubError](../-pub-nub-error/index.md)? = null, val jso: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, val statusCode: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, val affectedCall: Call&lt;*&gt;? = null, val retryAfterHeaderValue: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null, val affectedChannels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptyList(), val affectedChannelGroups: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptyList(), val cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? = null, val requestInfo: [PubNubException.RequestInfo](-request-info/index.md)? = null, val remoteAction: [ExtendedRemoteAction](../../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;*&gt;? = null) : [Exception](https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html)

Custom exception wrapper for errors occurred during execution or processing of a PubNub API operation.

## Constructors

| | |
|---|---|
| [PubNubException](-pub-nub-exception.md) | [jvm]<br>constructor(pubnubError: [PubNubError](../-pub-nub-error/index.md))constructor(pubnubError: [PubNubError](../-pub-nub-error/index.md), message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))constructor(errorMessage: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, pubnubError: [PubNubError](../-pub-nub-error/index.md)? = null, jso: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, statusCode: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, affectedCall: Call&lt;*&gt;? = null, retryAfterHeaderValue: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null, affectedChannels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptyList(), affectedChannelGroups: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptyList(), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? = null, requestInfo: [PubNubException.RequestInfo](-request-info/index.md)? = null, remoteAction: [ExtendedRemoteAction](../../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;*&gt;? = null) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |
| [RequestInfo](-request-info/index.md) | [jvm]<br>data class [RequestInfo](-request-info/index.md)(val tlsEnabled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val origin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, val authKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, val clientRequest: Request) |

## Properties

| Name | Summary |
|---|---|
| [affectedCall](affected-call.md) | [jvm]<br>val [affectedCall](affected-call.md): Call&lt;*&gt;? = null<br>A reference to the affected call. Useful for calling Endpoint.retry. |
| [affectedChannelGroups](affected-channel-groups.md) | [jvm]<br>val [affectedChannelGroups](affected-channel-groups.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [affectedChannels](affected-channels.md) | [jvm]<br>val [affectedChannels](affected-channels.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [cause](cause.md) | [jvm]<br>open override val [cause](cause.md): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? = null |
| [errorMessage](error-message.md) | [jvm]<br>val [errorMessage](error-message.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null<br>The error message received from the server, if any. |
| [jso](jso.md) | [jvm]<br>val [jso](jso.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null<br>The error json received from the server, if any. |
| [message](index.md#1824300659%2FProperties%2F1454713420) | [jvm]<br>open val [message](index.md#1824300659%2FProperties%2F1454713420): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [pubnubError](pubnub-error.md) | [jvm]<br>val [pubnubError](pubnub-error.md): [PubNubError](../-pub-nub-error/index.md)? = null<br>The appropriate matching PubNub error. |
| [remoteAction](remote-action.md) | [jvm]<br>val [remoteAction](remote-action.md): [ExtendedRemoteAction](../../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;*&gt;? = null |
| [requestInfo](request-info.md) | [jvm]<br>val [requestInfo](request-info.md): [PubNubException.RequestInfo](-request-info/index.md)? = null |
| [retryAfterHeaderValue](retry-after-header-value.md) | [jvm]<br>val [retryAfterHeaderValue](retry-after-header-value.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null |
| [statusCode](status-code.md) | [jvm]<br>val [statusCode](status-code.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0<br>HTTP status code. |

## Functions

| Name | Summary |
|---|---|
| [addSuppressed](index.md#282858770%2FFunctions%2F1454713420) | [jvm]<br>fun [addSuppressed](index.md#282858770%2FFunctions%2F1454713420)(p0: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) |
| [fillInStackTrace](index.md#-1102069925%2FFunctions%2F1454713420) | [jvm]<br>open fun [fillInStackTrace](index.md#-1102069925%2FFunctions%2F1454713420)(): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html) |
| [getLocalizedMessage](index.md#1043865560%2FFunctions%2F1454713420) | [jvm]<br>open fun [getLocalizedMessage](index.md#1043865560%2FFunctions%2F1454713420)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getStackTrace](index.md#2050903719%2FFunctions%2F1454713420) | [jvm]<br>open fun [getStackTrace](index.md#2050903719%2FFunctions%2F1454713420)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[StackTraceElement](https://docs.oracle.com/javase/8/docs/api/java/lang/StackTraceElement.html)&gt; |
| [getSuppressed](index.md#672492560%2FFunctions%2F1454713420) | [jvm]<br>fun [getSuppressed](index.md#672492560%2FFunctions%2F1454713420)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)&gt; |
| [initCause](index.md#-418225042%2FFunctions%2F1454713420) | [jvm]<br>open fun [initCause](index.md#-418225042%2FFunctions%2F1454713420)(p0: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html) |
| [printStackTrace](index.md#-1769529168%2FFunctions%2F1454713420) | [jvm]<br>open fun [printStackTrace](index.md#-1769529168%2FFunctions%2F1454713420)()<br>open fun [printStackTrace](index.md#1841853697%2FFunctions%2F1454713420)(p0: [PrintStream](https://docs.oracle.com/javase/8/docs/api/java/io/PrintStream.html))<br>open fun [printStackTrace](index.md#1175535278%2FFunctions%2F1454713420)(p0: [PrintWriter](https://docs.oracle.com/javase/8/docs/api/java/io/PrintWriter.html)) |
| [setStackTrace](index.md#2135801318%2FFunctions%2F1454713420) | [jvm]<br>open fun [setStackTrace](index.md#2135801318%2FFunctions%2F1454713420)(p0: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[StackTraceElement](https://docs.oracle.com/javase/8/docs/api/java/lang/StackTraceElement.html)&gt;) |
