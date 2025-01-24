//[pubnub-kotlin-core-api](../../../../../index.md)/[com.pubnub.api.models.consumer.push.payload](../../../index.md)/[PushPayloadHelper](../../index.md)/[FCMPayloadV2](../index.md)/[WebpushConfig](index.md)

# WebpushConfig

[common]\
class [WebpushConfig](index.md) : [PushPayloadSerializer](../../../-push-payload-serializer/index.md)

## Constructors

| | |
|---|---|
| [WebpushConfig](-webpush-config.md) | [common]<br>constructor() |

## Types

| Name | Summary |
|---|---|
| [WebpushFcmOptions](-webpush-fcm-options/index.md) | [common]<br>class [WebpushFcmOptions](-webpush-fcm-options/index.md) : [PushPayloadSerializer](../../../-push-payload-serializer/index.md) |

## Properties

| Name | Summary |
|---|---|
| [data](data.md) | [common]<br>var [data](data.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt;? |
| [fcmOptions](fcm-options.md) | [common]<br>var [fcmOptions](fcm-options.md): [PushPayloadHelper.FCMPayloadV2.WebpushConfig.WebpushFcmOptions](-webpush-fcm-options/index.md)? |
| [headers](headers.md) | [common]<br>var [headers](headers.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt;? |
| [notification](notification.md) | [common]<br>var [notification](notification.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)&gt;? |

## Functions

| Name | Summary |
|---|---|
| [toMap](to-map.md) | [common]<br>open override fun [toMap](to-map.md)(): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)&gt; |
