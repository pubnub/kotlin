//[pubnub-kotlin-core-api](../../../../index.md)/[com.pubnub.api.models.consumer.push.payload](../../index.md)/[PushPayloadHelper](../index.md)/[APNSPayload](index.md)

# APNSPayload

[common]\
class [APNSPayload](index.md) : [PushPayloadSerializer](../../-push-payload-serializer/index.md)

## Constructors

| | |
|---|---|
| [APNSPayload](-a-p-n-s-payload.md) | [common]<br>constructor() |

## Types

| Name | Summary |
|---|---|
| [APNS2Configuration](-a-p-n-s2-configuration/index.md) | [common]<br>class [APNS2Configuration](-a-p-n-s2-configuration/index.md) : [PushPayloadSerializer](../../-push-payload-serializer/index.md) |
| [APS](-a-p-s/index.md) | [common]<br>class [APS](-a-p-s/index.md) : [PushPayloadSerializer](../../-push-payload-serializer/index.md) |

## Properties

| Name | Summary |
|---|---|
| [apns2Configurations](apns2-configurations.md) | [common]<br>var [apns2Configurations](apns2-configurations.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[PushPayloadHelper.APNSPayload.APNS2Configuration](-a-p-n-s2-configuration/index.md)&gt;? |
| [aps](aps.md) | [common]<br>var [aps](aps.md): [PushPayloadHelper.APNSPayload.APS](-a-p-s/index.md)? |
| [custom](custom.md) | [common]<br>var [custom](custom.md): [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt;? |

## Functions

| Name | Summary |
|---|---|
| [toMap](to-map.md) | [common]<br>open override fun [toMap](to-map.md)(): [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt; |
