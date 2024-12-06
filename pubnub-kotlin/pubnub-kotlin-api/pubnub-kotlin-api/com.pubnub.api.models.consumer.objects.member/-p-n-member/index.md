//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.models.consumer.objects.member](../index.md)/[PNMember](index.md)

# PNMember

[common]\
data class [PNMember](index.md)(val uuid: [PNUUIDMetadata](../../com.pubnub.api.models.consumer.objects.uuid/-p-n-u-u-i-d-metadata/index.md), val custom: [PatchValue](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.utils/-patch-value/index.md)&lt;[Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?&gt;?&gt;? = null, val updated: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val eTag: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val status: [PatchValue](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;?, val type: [PatchValue](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;?)

## Constructors

| | |
|---|---|
| [PNMember](-p-n-member.md) | [common]<br>constructor(uuid: [PNUUIDMetadata](../../com.pubnub.api.models.consumer.objects.uuid/-p-n-u-u-i-d-metadata/index.md), custom: [PatchValue](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.utils/-patch-value/index.md)&lt;[Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?&gt;?&gt;? = null, updated: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), eTag: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), status: [PatchValue](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;?, type: [PatchValue](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;?) |

## Types

| Name | Summary |
|---|---|
| [Partial](-partial/index.md) | [common]<br>data class [Partial](-partial/index.md)(val uuidId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val custom: [CustomObject](../../com.pubnub.kmp/-custom-object/index.md)? = null, val status: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, val type: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null) : [MemberInput](../-member-input/index.md) |

## Properties

| Name | Summary |
|---|---|
| [custom](custom.md) | [common]<br>val [custom](custom.md): [PatchValue](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.utils/-patch-value/index.md)&lt;[Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?&gt;?&gt;? = null |
| [eTag](e-tag.md) | [common]<br>val [eTag](e-tag.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [status](status.md) | [common]<br>val [status](status.md): [PatchValue](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;? |
| [type](type.md) | [common]<br>val [type](type.md): [PatchValue](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;? |
| [updated](updated.md) | [common]<br>val [updated](updated.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [uuid](uuid.md) | [common]<br>val [uuid](uuid.md): [PNUUIDMetadata](../../com.pubnub.api.models.consumer.objects.uuid/-p-n-u-u-i-d-metadata/index.md) |
