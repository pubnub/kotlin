[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.push.payload](../index.md) / [PushPayloadHelper](./index.md)

# PushPayloadHelper

`class PushPayloadHelper`

### Types

| Name | Summary |
|---|---|
| [APNSPayload](-a-p-n-s-payload/index.md) | `class APNSPayload : `[`PushPayloadSerializer`](../-push-payload-serializer/index.md) |
| [FCMPayload](-f-c-m-payload/index.md) | `class FCMPayload : `[`PushPayloadSerializer`](../-push-payload-serializer/index.md) |
| [MPNSPayload](-m-p-n-s-payload/index.md) | `class MPNSPayload : `[`PushPayloadSerializer`](../-push-payload-serializer/index.md) |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PushPayloadHelper()` |

### Properties

| Name | Summary |
|---|---|
| [apnsPayload](apns-payload.md) | `var apnsPayload: APNSPayload?` |
| [commonPayload](common-payload.md) | `var commonPayload: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>?` |
| [fcmPayload](fcm-payload.md) | `var fcmPayload: FCMPayload?` |
| [mpnsPayload](mpns-payload.md) | `var mpnsPayload: MPNSPayload?` |

### Functions

| Name | Summary |
|---|---|
| [build](build.md) | `fun build(): `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>` |
