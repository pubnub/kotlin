//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNubException](index.md)/[PubNubException](-pub-nub-exception.md)

# PubNubException

[apple, js, jvm, common]\
[apple, js]\
constructor(statusCode: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = 0, errorMessage: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)?)

[jvm]\
constructor(pubnubError: [PubNubError](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api/-pub-nub-error/index.md), message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html))

[jvm]\
constructor(errorMessage: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, pubnubError: [PubNubError](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api/-pub-nub-error/index.md)? = null, jso: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, statusCode: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = 0, affectedCall: Call&lt;*&gt;? = null, retryAfterHeaderValue: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)? = null, affectedChannels: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt; = emptyList(), affectedChannelGroups: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt; = emptyList(), cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)? = null, requestInfo: [PubNubException.RequestInfo](-request-info/index.md)? = null, remoteAction: [ExtendedRemoteAction](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;*&gt;? = null)

[common]\
expect constructor(pubnubError: [PubNubError](../-pub-nub-error/index.md), cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)? = null)

[apple, js]\
actual constructor(pubnubError: [PubNubError](../-pub-nub-error/index.md), cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)?)

[jvm]\

@[JvmOverloads](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.jvm/-jvm-overloads/index.html)

actual constructor(pubnubError: [PubNubError](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api/-pub-nub-error/index.md), cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)?)

[common]\
expect constructor(errorMessage: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, statusCode: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)? = null)

[apple, js, jvm]\
actual constructor(errorMessage: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, statusCode: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)?)

[common]\
expect constructor(errorMessage: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)? = null)

[apple, js]\
actual constructor(errorMessage: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)?)

[jvm]\

@[JvmOverloads](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.jvm/-jvm-overloads/index.html)

actual constructor(errorMessage: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)?)
