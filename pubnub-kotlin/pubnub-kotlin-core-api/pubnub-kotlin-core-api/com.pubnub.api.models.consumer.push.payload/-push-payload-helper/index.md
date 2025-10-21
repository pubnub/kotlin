//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.models.consumer.push.payload](../index.md)/[PushPayloadHelper](index.md)

# PushPayloadHelper

[common]\
class [PushPayloadHelper](index.md)

## Constructors

| | |
|---|---|
| [PushPayloadHelper](-push-payload-helper.md) | [common]<br>constructor() |

## Types

| Name | Summary |
|---|---|
| [APNSPayload](-a-p-n-s-payload/index.md) | [common]<br>class [APNSPayload](-a-p-n-s-payload/index.md) : [PushPayloadSerializer](../-push-payload-serializer/index.md) |
| [FCMPayload](-f-c-m-payload/index.md) | [common]<br>class [~~FCMPayload~~](-f-c-m-payload/index.md) : [PushPayloadSerializer](../-push-payload-serializer/index.md) |
| [FCMPayloadV2](-f-c-m-payload-v2/index.md) | [common]<br>class [FCMPayloadV2](-f-c-m-payload-v2/index.md) : [PushPayloadSerializer](../-push-payload-serializer/index.md) |

## Properties

| Name | Summary |
|---|---|
| [apnsPayload](apns-payload.md) | [common]<br>var [apnsPayload](apns-payload.md): [PushPayloadHelper.APNSPayload](-a-p-n-s-payload/index.md)? |
| [commonPayload](common-payload.md) | [common]<br>var [commonPayload](common-payload.md): [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt;? |
| [fcmPayload](fcm-payload.md) | [common]<br>var [~~fcmPayload~~](fcm-payload.md): [PushPayloadHelper.FCMPayload](-f-c-m-payload/index.md)? |
| [fcmPayloadV2](fcm-payload-v2.md) | [common]<br>var [fcmPayloadV2](fcm-payload-v2.md): [PushPayloadHelper.FCMPayloadV2](-f-c-m-payload-v2/index.md)? |

## Functions

| Name | Summary |
|---|---|
| [build](build.md) | [common]<br>fun [build](build.md)(): [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt; |
