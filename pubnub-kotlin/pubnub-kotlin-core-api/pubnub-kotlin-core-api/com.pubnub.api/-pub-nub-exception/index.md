//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNubException](index.md)

# PubNubException

[common]\
expect class [PubNubException](index.md) : [Exception](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-exception/index.html)

Custom exception wrapper for errors occurred during execution or processing of a PubNub API operation.

[apple, js]\
actual class [PubNubException](index.md) : [Exception](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-exception/index.html)

Custom exception wrapper for errors occurred during execution or processing of a PubNub API operation.

[jvm]\
actual data class [PubNubException](index.md) : [Exception](https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html)

Custom exception wrapper for errors occurred during execution or processing of a PubNub API operation.

## Constructors

| | |
|---|---|
| [PubNubException](-pub-nub-exception.md) | [apple, js]<br>constructor(statusCode: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = 0, errorMessage: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)?)actual constructor(pubnubError: [PubNubError](../-pub-nub-error/index.md), cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)?)actual constructor(errorMessage: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, statusCode: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)?)actual constructor(errorMessage: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)?)<br>[jvm]<br>constructor(pubnubError: [PubNubError](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api/-pub-nub-error/index.md), message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html))constructor(errorMessage: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, pubnubError: [PubNubError](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api/-pub-nub-error/index.md)? = null, jso: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, statusCode: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = 0, affectedCall: Call&lt;*&gt;? = null, retryAfterHeaderValue: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)? = null, affectedChannels: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt; = emptyList(), affectedChannelGroups: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt; = emptyList(), cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)? = null, requestInfo: [PubNubException.RequestInfo](-request-info/index.md)? = null, remoteAction: [ExtendedRemoteAction](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;*&gt;? = null)<br>@[JvmOverloads](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.jvm/-jvm-overloads/index.html)<br>actual constructor(pubnubError: [PubNubError](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api/-pub-nub-error/index.md), cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)?)actual constructor(errorMessage: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, statusCode: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)?)<br>@[JvmOverloads](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.jvm/-jvm-overloads/index.html)<br>actual constructor(errorMessage: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)?)<br>[common]<br>expect constructor(pubnubError: [PubNubError](../-pub-nub-error/index.md), cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)? = null)expect constructor(errorMessage: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, statusCode: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)? = null)expect constructor(errorMessage: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)? = null) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common, apple, js, jvm]<br>[common]<br>expect object [Companion](-companion/index.md)<br>[apple, js, jvm]<br>actual object [Companion](-companion/index.md) |
| [RequestInfo](-request-info/index.md) | [jvm]<br>data class [RequestInfo](-request-info/index.md)(val tlsEnabled: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), val origin: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val uuid: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, val authKey: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, val clientRequest: Request) |

## Properties

| Name | Summary |
|---|---|
| [affectedCall](affected-call.md) | [jvm]<br>val [affectedCall](affected-call.md): Call&lt;*&gt;? = null<br>A reference to the affected call. Useful for calling retry. |
| [affectedChannelGroups](affected-channel-groups.md) | [jvm]<br>val [affectedChannelGroups](affected-channel-groups.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt; |
| [affectedChannels](affected-channels.md) | [jvm]<br>val [affectedChannels](affected-channels.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt; |
| [cause](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api/-pub-nub-exception/cause.md) | [jvm, common, apple, js]<br>[jvm]<br>open override val [cause](cause.md): [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)? = null<br>[common]<br>open val [cause](index.md#-654012527%2FProperties%2F1196661149): [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)?<br>[apple]<br>open val [cause](index.md#-654012527%2FProperties%2F1581906243): [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)?<br>[js]<br>open val [cause](index.md#-654012527%2FProperties%2F1336103183): [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)? |
| [errorMessage](error-message.md) | [jvm]<br>val [errorMessage](error-message.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null<br>The error message received from the server, if any. |
| [jso](jso.md) | [jvm]<br>val [jso](jso.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null<br>The error json received from the server, if any. |
| message | [common, apple, js, jvm]<br>[common]<br>open val [message](index.md#1824300659%2FProperties%2F1196661149): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?<br>[apple]<br>open val [message](index.md#1824300659%2FProperties%2F1581906243): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?<br>[js]<br>open val [message](index.md#1824300659%2FProperties%2F1336103183): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?<br>[jvm]<br>open val [message](index.md#1824300659%2FProperties%2F1141030505): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [pubnubError](pubnub-error.md) | [jvm]<br>val [pubnubError](pubnub-error.md): [PubNubError](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api/-pub-nub-error/index.md)? = null<br>The appropriate matching PubNub error. |
| [remoteAction](remote-action.md) | [jvm]<br>val [remoteAction](remote-action.md): [ExtendedRemoteAction](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;*&gt;? = null |
| [requestInfo](request-info.md) | [jvm]<br>val [requestInfo](request-info.md): [PubNubException.RequestInfo](-request-info/index.md)? = null |
| [retryAfterHeaderValue](retry-after-header-value.md) | [jvm]<br>val [retryAfterHeaderValue](retry-after-header-value.md): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)? = null |
| [statusCode](status-code.md) | [common, apple, js, jvm]<br>[common]<br>expect val [statusCode](status-code.md): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)<br>[apple, js, jvm]<br>actual val [statusCode](status-code.md): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = 0<br>HTTP status code. |

## Functions

| Name | Summary |
|---|---|
| [toString](index.md#1009289620%2FFunctions%2F1336103183) | [js]<br>open override fun [toString](index.md#1009289620%2FFunctions%2F1336103183)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
