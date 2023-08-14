//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.consumer.objects](../index.md)/[PNSortKey](index.md)

# PNSortKey

[jvm]\
sealed class [PNSortKey](index.md)&lt;[T](index.md) : [SortField](../-sort-field/index.md)&gt;

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |
| [PNAsc](-p-n-asc/index.md) | [jvm]<br>class [PNAsc](-p-n-asc/index.md)&lt;[T](-p-n-asc/index.md) : [SortField](../-sort-field/index.md)&gt;(key: [T](-p-n-asc/index.md)) : [PNSortKey](index.md)&lt;[T](-p-n-asc/index.md)&gt; |
| [PNDesc](-p-n-desc/index.md) | [jvm]<br>class [PNDesc](-p-n-desc/index.md)&lt;[T](-p-n-desc/index.md) : [SortField](../-sort-field/index.md)&gt;(key: [T](-p-n-desc/index.md)) : [PNSortKey](index.md)&lt;[T](-p-n-desc/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [toSortParameter](to-sort-parameter.md) | [jvm]<br>fun [toSortParameter](to-sort-parameter.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

## Inheritors

| Name |
|---|
| [PNAsc](-p-n-asc/index.md) |
| [PNDesc](-p-n-desc/index.md) |
