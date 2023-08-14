//[pubnub-kotlin](../../index.md)/[com.pubnub.api](index.md)

# Package com.pubnub.api

## Types

| Name | Summary |
|---|---|
| [Endpoint](-endpoint/index.md) | [jvm]<br>abstract class [Endpoint](-endpoint/index.md)&lt;[Input](-endpoint/index.md), [Output](-endpoint/index.md)&gt; : [ExtendedRemoteAction](../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)&lt;[Output](-endpoint/index.md)&gt; <br>Base class for all PubNub API operation implementations. |
| [PNConfiguration](-p-n-configuration/index.md) | [jvm]<br>open class [PNConfiguration](-p-n-configuration/index.md)(userId: [UserId](-user-id/index.md), enableSubscribeBeta: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false)<br>A storage for user-provided information which describe further PubNub client behaviour. Configuration instance contains additional set of properties which allow performing precise PubNub client configuration. |
| [PubNub](-pub-nub/index.md) | [jvm]<br>class [PubNub](-pub-nub/index.md) |
| [PubNubError](-pub-nub-error/index.md) | [jvm]<br>enum [PubNubError](-pub-nub-error/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[PubNubError](-pub-nub-error/index.md)&gt; <br>List of known PubNub errors. Observe them in [PubNubException.pubnubError](-pub-nub-exception/pubnub-error.md) in [PNStatus.exception](../com.pubnub.api.models.consumer/-p-n-status/exception.md). |
| [PubNubException](-pub-nub-exception/index.md) | [jvm]<br>data class [PubNubException](-pub-nub-exception/index.md)(val errorMessage: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, val pubnubError: [PubNubError](-pub-nub-error/index.md)? = null, val jso: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, val statusCode: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, val affectedCall: Call&lt;*&gt;? = null) : [Exception](https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html)<br>Custom exception wrapper for errors occurred during execution or processing of a PubNub API operation. |
| [SpaceId](-space-id/index.md) | [jvm]<br>data class [SpaceId](-space-id/index.md)(val value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |
| [UserId](-user-id/index.md) | [jvm]<br>data class [UserId](-user-id/index.md)(val value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |
