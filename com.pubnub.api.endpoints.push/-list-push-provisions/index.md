[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.push](../index.md) / [ListPushProvisions](./index.md)

# ListPushProvisions

`class ListPushProvisions : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, `[`PNPushListProvisionsResult`](../../com.pubnub.api.models.consumer.push/-p-n-push-list-provisions-result/index.md)`>`

**See Also**

[PubNub.auditPushChannelProvisions](../../com.pubnub.api/-pub-nub/audit-push-channel-provisions.md)

### Properties

| Name | Summary |
|---|---|
| [deviceId](device-id.md) | `val deviceId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [environment](environment.md) | `val environment: `[`PNPushEnvironment`](../../com.pubnub.api.enums/-p-n-push-environment/index.md) |
| [pushType](push-type.md) | `val pushType: `[`PNPushType`](../../com.pubnub.api.enums/-p-n-push-type/index.md) |
| [topic](topic.md) | `val topic: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>>): `[`PNPushListProvisionsResult`](../../com.pubnub.api.models.consumer.push/-p-n-push-list-provisions-result/index.md) |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>>` |
| [operationType](operation-type.md) | `fun operationType(): PNPushNotificationEnabledChannelsOperation` |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
