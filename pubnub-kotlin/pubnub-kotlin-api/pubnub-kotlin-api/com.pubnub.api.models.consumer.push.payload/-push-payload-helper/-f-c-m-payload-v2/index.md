//[pubnub-kotlin-api](../../../../index.md)/[com.pubnub.api.models.consumer.push.payload](../../index.md)/[PushPayloadHelper](../index.md)/[FCMPayloadV2](index.md)

# FCMPayloadV2

[common]\
class [FCMPayloadV2](index.md) : [PushPayloadSerializer](../../-push-payload-serializer/index.md)

## Constructors

| | |
|---|---|
| [FCMPayloadV2](-f-c-m-payload-v2.md) | [common]<br>constructor() |

## Types

| Name | Summary |
|---|---|
| [AndroidConfig](-android-config/index.md) | [common]<br>class [AndroidConfig](-android-config/index.md) : [PushPayloadSerializer](../../-push-payload-serializer/index.md) |
| [ApnsConfig](-apns-config/index.md) | [common]<br>class [ApnsConfig](-apns-config/index.md) : [PushPayloadSerializer](../../-push-payload-serializer/index.md) |
| [FcmOptions](-fcm-options/index.md) | [common]<br>class [FcmOptions](-fcm-options/index.md) : [PushPayloadSerializer](../../-push-payload-serializer/index.md) |
| [Notification](-notification/index.md) | [common]<br>class [Notification](-notification/index.md) : [PushPayloadSerializer](../../-push-payload-serializer/index.md) |
| [WebpushConfig](-webpush-config/index.md) | [common]<br>class [WebpushConfig](-webpush-config/index.md) : [PushPayloadSerializer](../../-push-payload-serializer/index.md) |

## Properties

| Name | Summary |
|---|---|
| [android](android.md) | [common]<br>var [android](android.md): [PushPayloadHelper.FCMPayloadV2.AndroidConfig](-android-config/index.md)? |
| [apns](apns.md) | [common]<br>var [apns](apns.md): [PushPayloadHelper.FCMPayloadV2.ApnsConfig](-apns-config/index.md)? |
| [data](data.md) | [common]<br>var [data](data.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;? |
| [fcmOptions](fcm-options.md) | [common]<br>var [fcmOptions](fcm-options.md): [PushPayloadHelper.FCMPayloadV2.FcmOptions](-fcm-options/index.md)? |
| [notification](notification.md) | [common]<br>var [notification](notification.md): [PushPayloadHelper.FCMPayloadV2.Notification](-notification/index.md)? |
| [webpush](webpush.md) | [common]<br>var [webpush](webpush.md): [PushPayloadHelper.FCMPayloadV2.WebpushConfig](-webpush-config/index.md)? |

## Functions

| Name | Summary |
|---|---|
| [toMap](to-map.md) | [common]<br>open override fun [toMap](to-map.md)(): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; |
