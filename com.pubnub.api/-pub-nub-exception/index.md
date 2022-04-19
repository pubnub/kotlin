[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNubException](./index.md)

# PubNubException

`data class PubNubException : `[`Exception`](https://docs.oracle.com/javase/6/docs/api/java/lang/Exception.html)

Custom exception wrapper for errors occurred during execution or processing of a PubNub API operation.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Custom exception wrapper for errors occurred during execution or processing of a PubNub API operation.`PubNubException(errorMessage: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, pubnubError: `[`PubNubError`](../-pub-nub-error/index.md)`? = null, jso: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, statusCode: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0, affectedCall: Call<*>? = null)` |

### Properties

| Name | Summary |
|---|---|
| [affectedCall](affected-call.md) | A reference to the affected call. Useful for calling [retry](../-endpoint/retry.md).`val affectedCall: Call<*>?` |
| [errorMessage](error-message.md) | The error message received from the server, if any.`val errorMessage: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [jso](jso.md) | The error json received from the server, if any.`val jso: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [pubnubError](pubnub-error.md) | The appropriate matching PubNub error.`val pubnubError: `[`PubNubError`](../-pub-nub-error/index.md)`?` |
| [statusCode](status-code.md) | HTTP status code.`val statusCode: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
