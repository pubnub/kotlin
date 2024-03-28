//[pubnub-core-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNubException](index.md)/[PubNubException](-pub-nub-exception.md)

# PubNubException

[jvm]\
constructor(pubnubError: [PubNubError](../-pub-nub-error/index.md))

constructor(pubnubError: [PubNubError](../-pub-nub-error/index.md), message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))

constructor(errorMessage: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, pubnubError: [PubNubError](../-pub-nub-error/index.md)? = null, jso: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, statusCode: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, affectedCall: Call&lt;*&gt;? = null, retryAfterHeaderValue: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null, affectedChannels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptyList(), affectedChannelGroups: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptyList(), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? = null, requestInfo: [PubNubException.RequestInfo](-request-info/index.md)? = null, remoteAction: [ExtendedRemoteAction](../../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;*&gt;? = null)
