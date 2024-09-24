//[pubnub-kotlin-api](../../index.md)/[com.pubnub.api.models.consumer](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [PNBoundedPage](-p-n-bounded-page/index.md) | [common]<br>data class [PNBoundedPage](-p-n-bounded-page/index.md)(val start: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null, val end: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null, val limit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null)<br>The paging object used for pagination |
| [PNPublishResult](-p-n-publish-result/index.md) | [common]<br>class [PNPublishResult](-p-n-publish-result/index.md)(val timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html))<br>Result of the Publish operation |
| [PNStatus](-p-n-status/index.md) | [common]<br>class [PNStatus](-p-n-status/index.md)(val category: [PNStatusCategory](../com.pubnub.api.enums/-p-n-status-category/index.md), val exception: [PubNubException](../com.pubnub.api/-pub-nub-exception/index.md)? = null, val currentTimetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null, val affectedChannels: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptySet(), val affectedChannelGroups: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptySet()) |
| [PNTimeResult](-p-n-time-result/index.md) | [common]<br>class [PNTimeResult](-p-n-time-result/index.md)(val timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html))<br>Result of the Time operation. |
