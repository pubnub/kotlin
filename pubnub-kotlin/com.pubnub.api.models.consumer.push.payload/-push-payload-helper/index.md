//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.consumer.push.payload](../index.md)/[PushPayloadHelper](index.md)

# PushPayloadHelper

[jvm]\
class [PushPayloadHelper](index.md)

## Constructors

| | |
|---|---|
| [PushPayloadHelper](-push-payload-helper.md) | [jvm]<br>fun [PushPayloadHelper](-push-payload-helper.md)() |

## Types

| Name | Summary |
|---|---|
| [APNSPayload](-a-p-n-s-payload/index.md) | [jvm]<br>class [APNSPayload](-a-p-n-s-payload/index.md) : [PushPayloadSerializer](../-push-payload-serializer/index.md) |
| [FCMPayload](-f-c-m-payload/index.md) | [jvm]<br>class [FCMPayload](-f-c-m-payload/index.md) : [PushPayloadSerializer](../-push-payload-serializer/index.md) |
| [MPNSPayload](-m-p-n-s-payload/index.md) | [jvm]<br>class [MPNSPayload](-m-p-n-s-payload/index.md) : [PushPayloadSerializer](../-push-payload-serializer/index.md) |

## Functions

| Name | Summary |
|---|---|
| [build](build.md) | [jvm]<br>fun [build](build.md)(): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; |

## Properties

| Name | Summary |
|---|---|
| [apnsPayload](apns-payload.md) | [jvm]<br>var [apnsPayload](apns-payload.md): [PushPayloadHelper.APNSPayload](-a-p-n-s-payload/index.md)? = null |
| [commonPayload](common-payload.md) | [jvm]<br>var [commonPayload](common-payload.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;? = null |
| [fcmPayload](fcm-payload.md) | [jvm]<br>var [fcmPayload](fcm-payload.md): [PushPayloadHelper.FCMPayload](-f-c-m-payload/index.md)? = null |
| [mpnsPayload](mpns-payload.md) | [jvm]<br>var [mpnsPayload](mpns-payload.md): [PushPayloadHelper.MPNSPayload](-m-p-n-s-payload/index.md)? = null |
