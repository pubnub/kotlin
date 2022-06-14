[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.objects](../index.md) / [PNSortKey](./index.md)

# PNSortKey

`sealed class PNSortKey<T : `[`SortField`](../-sort-field/index.md)`>`

### Types

| Name | Summary |
|---|---|
| [PNAsc](-p-n-asc/index.md) | `class PNAsc<T : `[`SortField`](../-sort-field/index.md)`> : `[`PNSortKey`](./index.md)`<T>` |
| [PNDesc](-p-n-desc/index.md) | `class PNDesc<T : `[`SortField`](../-sort-field/index.md)`> : `[`PNSortKey`](./index.md)`<T>` |

### Functions

| Name | Summary |
|---|---|
| [toSortParameter](to-sort-parameter.md) | `fun toSortParameter(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Companion Object Functions

| Name | Summary |
|---|---|
| [asc](asc.md) | `fun asc(key: `[`PNKey`](../-p-n-key/index.md)`): `[`PNSortKey`](./index.md)`<`[`PNKey`](../-p-n-key/index.md)`>` |
| [desc](desc.md) | `fun desc(key: `[`PNKey`](../-p-n-key/index.md)`): `[`PNSortKey`](./index.md)`<`[`PNKey`](../-p-n-key/index.md)`>` |
