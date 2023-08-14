//[pubnub-kotlin](../../index.md)/[com.pubnub.api.models.consumer.objects.member](index.md)

# Package com.pubnub.api.models.consumer.objects.member

## Types

| Name | Summary |
|---|---|
| [MemberInput](-member-input/index.md) | [jvm]<br>interface [MemberInput](-member-input/index.md) |
| [PNMember](-p-n-member/index.md) | [jvm]<br>data class [PNMember](-p-n-member/index.md)(val uuid: [PNUUIDMetadata](../com.pubnub.api.models.consumer.objects.uuid/-p-n-u-u-i-d-metadata/index.md)?, val custom: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null, val updated: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val eTag: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val status: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?) |
| [PNMemberArrayResult](-p-n-member-array-result/index.md) | [jvm]<br>data class [PNMemberArrayResult](-p-n-member-array-result/index.md)(val status: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val data: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[PNMember](-p-n-member/index.md)&gt;, val totalCount: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?, val next: [PNPage.PNNext](../com.pubnub.api.models.consumer.objects/-p-n-page/-p-n-next/index.md)?, val prev: [PNPage.PNPrev](../com.pubnub.api.models.consumer.objects/-p-n-page/-p-n-prev/index.md)?) |
| [PNUUIDDetailsLevel](-p-n-u-u-i-d-details-level/index.md) | [jvm]<br>enum [PNUUIDDetailsLevel](-p-n-u-u-i-d-details-level/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[PNUUIDDetailsLevel](-p-n-u-u-i-d-details-level/index.md)&gt; |
| [PNUUIDWithCustom](-p-n-u-u-i-d-with-custom/index.md) | [jvm]<br>~~data~~ ~~class~~ [~~PNUUIDWithCustom~~](-p-n-u-u-i-d-with-custom/index.md)~~(~~val uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val custom: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null~~)~~ ~~:~~ [~~MemberInput~~](-member-input/index.md) |
