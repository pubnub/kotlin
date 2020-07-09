[pubnub-kotlin](../../../index.md) / [com.pubnub.api.models.consumer.push.payload](../../index.md) / [PushPayloadHelper](../index.md) / [APNSPayload](./index.md)

# APNSPayload

`class APNSPayload : `[`PushPayloadSerializer`](../../-push-payload-serializer/index.md)

### Types

| Name | Summary |
|---|---|
| [APNS2Configuration](-a-p-n-s2-configuration/index.md) | `class APNS2Configuration : `[`PushPayloadSerializer`](../../-push-payload-serializer/index.md) |
| [APS](-a-p-s/index.md) | `class APS : `[`PushPayloadSerializer`](../../-push-payload-serializer/index.md) |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `APNSPayload()` |

### Properties

| Name | Summary |
|---|---|
| [apns2Configurations](apns2-configurations.md) | `var apns2Configurations: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<APNS2Configuration>?` |
| [aps](aps.md) | `var aps: APS?` |
| [custom](custom.md) | `var custom: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>?` |

### Functions

| Name | Summary |
|---|---|
| [toMap](to-map.md) | `fun toMap(): `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>` |
