[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.files](../index.md) / [GetFileUrl](./index.md)

# GetFileUrl

`class GetFileUrl : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<ResponseBody, `[`PNFileUrlResult`](../../com.pubnub.api.models.consumer.files/-p-n-file-url-result/index.md)`>`

**See Also**

[PubNub.getFileUrl](../../com.pubnub.api/-pub-nub/get-file-url.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `GetFileUrl(channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, fileName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, fileId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, pubNub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.md)`)` |

### Functions

| Name | Summary |
|---|---|
| [async](async.md) | Executes the call asynchronously. This function does not block the thread.`fun async(callback: (result: `[`PNFileUrlResult`](../../com.pubnub.api.models.consumer.files/-p-n-file-url-result/index.md)`?, status: `[`PNStatus`](../../com.pubnub.api.models.consumer/-p-n-status/index.md)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [createResponse](create-response.md) | `fun createResponse(input: Response<ResponseBody>): `[`PNFileUrlResult`](../../com.pubnub.api.models.consumer.files/-p-n-file-url-result/index.md) |
| [doWork](do-work.md) | `fun doWork(queryParams: <ERROR CLASS><`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<ResponseBody>` |
| [getAffectedChannelGroups](get-affected-channel-groups.md) | `fun getAffectedChannelGroups(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [getAffectedChannels](get-affected-channels.md) | `fun getAffectedChannels(): <ERROR CLASS>` |
| [isAuthRequired](is-auth-required.md) | `fun isAuthRequired(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isPubKeyRequired](is-pub-key-required.md) | `fun isPubKeyRequired(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isSubKeyRequired](is-sub-key-required.md) | `fun isSubKeyRequired(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [operationType](operation-type.md) | `fun operationType(): `[`PNOperationType`](../../com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](retry.md) | `fun retry(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [sync](sync.md) | Executes the call synchronously. This function blocks the thread.`fun sync(): `[`PNFileUrlResult`](../../com.pubnub.api.models.consumer.files/-p-n-file-url-result/index.md)`?` |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
