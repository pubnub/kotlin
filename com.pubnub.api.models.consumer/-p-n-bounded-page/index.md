[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer](../index.md) / [PNBoundedPage](./index.md)

# PNBoundedPage

`data class PNBoundedPage`

The paging object used for pagination

### Parameters

`start` - Timetoken denoting the start of the range requested
    (return values will be less than start).

`end` - Timetoken denoting the end of the range requested
    (return values will be greater than or equal to end).

`limit` - Specifies the number of items to return in response.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | The paging object used for pagination`PNBoundedPage(start: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`? = null, end: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`? = null, limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [end](end.md) | Timetoken denoting the end of the range requested     (return values will be greater than or equal to end).`val end: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |
| [limit](limit.md) | Specifies the number of items to return in response.`val limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [start](start.md) | Timetoken denoting the start of the range requested     (return values will be less than start).`val start: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |
