//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.consumer](../index.md)/[PNStatus](index.md)

# PNStatus

[jvm]\
data class [PNStatus](index.md)(var category: [PNStatusCategory](../../com.pubnub.api.enums/-p-n-status-category/index.md), var error: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val operation: [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md), val exception: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)? = null, var statusCode: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null, var tlsEnabled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? = null, var origin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, var uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, var authKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, var affectedChannels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt; = emptyList(), var affectedChannelGroups: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt; = emptyList())

Metadata related to executed PubNub API operations.

## Constructors

| | |
|---|---|
| [PNStatus](-p-n-status.md) | [jvm]<br>fun [PNStatus](-p-n-status.md)(category: [PNStatusCategory](../../com.pubnub.api.enums/-p-n-status-category/index.md), error: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), operation: [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md), exception: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)? = null, statusCode: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null, tlsEnabled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? = null, origin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, authKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, affectedChannels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt; = emptyList(), affectedChannelGroups: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt; = emptyList()) |

## Functions

| Name | Summary |
|---|---|
| [retry](retry.md) | [jvm]<br>fun [retry](retry.md)()<br>Execute the API operation again. |

## Properties

| Name | Summary |
|---|---|
| [affectedChannelGroups](affected-channel-groups.md) | [jvm]<br>var [affectedChannelGroups](affected-channel-groups.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;<br>List of channel groups affected by the this API operation. |
| [affectedChannels](affected-channels.md) | [jvm]<br>var [affectedChannels](affected-channels.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;<br>List of channels affected by the this API operation. |
| [authKey](auth-key.md) | [jvm]<br>var [authKey](auth-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null<br>The authentication key attached to the request, if needed. See more at PNConfiguration.authKey. |
| [category](category.md) | [jvm]<br>var [category](category.md): [PNStatusCategory](../../com.pubnub.api.enums/-p-n-status-category/index.md)<br>The [PNStatusCategory](../../com.pubnub.api.enums/-p-n-status-category/index.md) of an executed operation. |
| [clientRequest](client-request.md) | [jvm]<br>var [clientRequest](client-request.md): Request? = null |
| [error](error.md) | [jvm]<br>var [error](error.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Is `true` if the operation didn't succeed. Always check for it in [async](../../com.pubnub.api/-endpoint/async.md) blocks. |
| [exception](exception.md) | [jvm]<br>val [exception](exception.md): [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)? = null<br>Error information if the request didn't succeed. |
| [operation](operation.md) | [jvm]<br>val [operation](operation.md): [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md)<br>The concrete API operation type that's been executed. |
| [origin](origin.md) | [jvm]<br>var [origin](origin.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null<br>Origin of the HTTP request. See more at PNConfiguration.origin. |
| [statusCode](status-code.md) | [jvm]<br>var [statusCode](status-code.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null<br>HTTP status code. |
| [tlsEnabled](tls-enabled.md) | [jvm]<br>var [tlsEnabled](tls-enabled.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? = null<br>Whether the API operation was executed over HTTPS. |
| [uuid](uuid.md) | [jvm]<br>var [uuid](uuid.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null<br>The UUID which requested the API operation to be executed. See more at PNConfiguration.uuid. |
