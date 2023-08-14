//[pubnub-kotlin](../../index.md)/[com.pubnub.api.models.consumer.objects.channel](index.md)

# Package com.pubnub.api.models.consumer.objects.channel

## Types

| Name | Summary |
|---|---|
| [PNChannelMetadata](-p-n-channel-metadata/index.md) | [jvm]<br>data class [PNChannelMetadata](-p-n-channel-metadata/index.md)(val id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, val description: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, val custom: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, val updated: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, val eTag: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, val type: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, val status: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?) |
| [PNChannelMetadataArrayResult](-p-n-channel-metadata-array-result/index.md) | [jvm]<br>data class [PNChannelMetadataArrayResult](-p-n-channel-metadata-array-result/index.md)(val status: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val data: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[PNChannelMetadata](-p-n-channel-metadata/index.md)&gt;, val totalCount: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?, val next: [PNPage.PNNext](../com.pubnub.api.models.consumer.objects/-p-n-page/-p-n-next/index.md)?, val prev: [PNPage.PNPrev](../com.pubnub.api.models.consumer.objects/-p-n-page/-p-n-prev/index.md)?) |
| [PNChannelMetadataResult](-p-n-channel-metadata-result/index.md) | [jvm]<br>data class [PNChannelMetadataResult](-p-n-channel-metadata-result/index.md)(val status: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val data: [PNChannelMetadata](-p-n-channel-metadata/index.md)?) |
