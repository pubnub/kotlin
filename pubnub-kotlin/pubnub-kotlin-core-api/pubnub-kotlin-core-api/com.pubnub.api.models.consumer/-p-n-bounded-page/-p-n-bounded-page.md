//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.models.consumer](../index.md)/[PNBoundedPage](index.md)/[PNBoundedPage](-p-n-bounded-page.md)

# PNBoundedPage

[common]\
constructor(start: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null, end: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null, limit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null)

#### Parameters

common

| | |
|---|---|
| start | Timetoken denoting the start of the range requested     (return values will be less than start). |
| end | Timetoken denoting the end of the range requested     (return values will be greater than or equal to end). |
| limit | Specifies the number of items to return in response. |
