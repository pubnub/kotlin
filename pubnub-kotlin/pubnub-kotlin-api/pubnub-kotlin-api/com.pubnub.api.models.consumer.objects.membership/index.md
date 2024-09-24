//[pubnub-kotlin-api](../../index.md)/[com.pubnub.api.models.consumer.objects.membership](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [ChannelMembershipInput](-channel-membership-input/index.md) | [common]<br>interface [ChannelMembershipInput](-channel-membership-input/index.md) |
| [PNChannelDetailsLevel](-p-n-channel-details-level/index.md) | [common]<br>enum [PNChannelDetailsLevel](-p-n-channel-details-level/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[PNChannelDetailsLevel](-p-n-channel-details-level/index.md)&gt; |
| [PNChannelMembership](-p-n-channel-membership/index.md) | [common]<br>data class [PNChannelMembership](-p-n-channel-membership/index.md)(val channel: [PNChannelMetadata](../com.pubnub.api.models.consumer.objects.channel/-p-n-channel-metadata/index.md), val custom: [PatchValue](../com.pubnub.api.utils/-patch-value/index.md)&lt;[Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?&gt;?&gt;? = null, val updated: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val eTag: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val status: [PatchValue](../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;? = null) |
| [PNChannelMembershipArrayResult](-p-n-channel-membership-array-result/index.md) | [common]<br>data class [PNChannelMembershipArrayResult](-p-n-channel-membership-array-result/index.md)(val status: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val data: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[PNChannelMembership](-p-n-channel-membership/index.md)&gt;, val totalCount: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?, val next: [PNPage.PNNext](../com.pubnub.api.models.consumer.objects/-p-n-page/-p-n-next/index.md)?, val prev: [PNPage.PNPrev](../com.pubnub.api.models.consumer.objects/-p-n-page/-p-n-prev/index.md)?) |
