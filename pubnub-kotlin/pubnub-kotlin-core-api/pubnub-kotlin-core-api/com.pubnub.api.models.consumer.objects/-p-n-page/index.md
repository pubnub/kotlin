//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.models.consumer.objects](../index.md)/[PNPage](index.md)

# PNPage

sealed class [PNPage](index.md)

#### Inheritors

| |
|---|
| [PNNext](-p-n-next/index.md) |
| [PNPrev](-p-n-prev/index.md) |

## Types

| Name | Summary |
|---|---|
| [PNNext](-p-n-next/index.md) | [common]<br>data class [PNNext](-p-n-next/index.md)(val pageHash: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [PNPage](index.md) |
| [PNPrev](-p-n-prev/index.md) | [common]<br>data class [PNPrev](-p-n-prev/index.md)(val pageHash: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [PNPage](index.md) |

## Properties

| Name | Summary |
|---|---|
| [hash](hash.md) | [common]<br>val [~~hash~~](hash.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [pageHash](page-hash.md) | [common]<br>abstract val [pageHash](page-hash.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
