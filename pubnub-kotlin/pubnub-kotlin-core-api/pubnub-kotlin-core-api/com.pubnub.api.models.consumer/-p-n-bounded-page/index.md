//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.models.consumer](../index.md)/[PNBoundedPage](index.md)

# PNBoundedPage

data class [PNBoundedPage](index.md)(val start: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)? = null, val end: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)? = null, val limit: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)? = null)

The paging object used for pagination

#### Parameters

common

| | |
|---|---|
| start | Timetoken denoting the start of the range requested     (return values will be less than start). |
| end | Timetoken denoting the end of the range requested     (return values will be greater than or equal to end). |
| limit | Specifies the number of items to return in response. |

## Constructors

| | |
|---|---|
| [PNBoundedPage](-p-n-bounded-page.md) | [common]<br>constructor(start: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)? = null, end: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)? = null, limit: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)? = null) |

## Properties

| Name | Summary |
|---|---|
| [end](end.md) | [common]<br>val [end](end.md): [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)? = null |
| [limit](limit.md) | [common]<br>val [limit](limit.md): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)? = null |
| [start](start.md) | [common]<br>val [start](start.md): [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)? = null |
