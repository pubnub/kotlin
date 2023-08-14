//[pubnub-kotlin](../../index.md)/[com.pubnub.api.models.consumer.objects.membership](index.md)

# Package com.pubnub.api.models.consumer.objects.membership

## Types

| Name | Summary |
|---|---|
| [ChannelMembershipInput](-channel-membership-input/index.md) | [jvm]<br>interface [ChannelMembershipInput](-channel-membership-input/index.md) |
| [PNChannelDetailsLevel](-p-n-channel-details-level/index.md) | [jvm]<br>enum [PNChannelDetailsLevel](-p-n-channel-details-level/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[PNChannelDetailsLevel](-p-n-channel-details-level/index.md)&gt; |
| [PNChannelMembership](-p-n-channel-membership/index.md) | [jvm]<br>data class [PNChannelMembership](-p-n-channel-membership/index.md)(val channel: [PNChannelMetadata](../com.pubnub.api.models.consumer.objects.channel/-p-n-channel-metadata/index.md)?, val custom: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, val updated: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val eTag: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val status: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?) |
| [PNChannelMembershipArrayResult](-p-n-channel-membership-array-result/index.md) | [jvm]<br>data class [PNChannelMembershipArrayResult](-p-n-channel-membership-array-result/index.md)(val status: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val data: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[PNChannelMembership](-p-n-channel-membership/index.md)&gt;, val totalCount: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?, val next: [PNPage.PNNext](../com.pubnub.api.models.consumer.objects/-p-n-page/-p-n-next/index.md)?, val prev: [PNPage.PNPrev](../com.pubnub.api.models.consumer.objects/-p-n-page/-p-n-prev/index.md)?) |
| [PNChannelWithCustom](-p-n-channel-with-custom/index.md) | [jvm]<br>~~data~~ ~~class~~ [~~PNChannelWithCustom~~](-p-n-channel-with-custom/index.md)~~(~~val channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val custom: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null~~)~~ ~~:~~ [~~ChannelMembershipInput~~](-channel-membership-input/index.md) |
