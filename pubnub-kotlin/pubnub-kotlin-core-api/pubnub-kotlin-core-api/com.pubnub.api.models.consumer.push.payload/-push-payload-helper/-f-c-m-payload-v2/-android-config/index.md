//[pubnub-kotlin-core-api](../../../../../index.md)/[com.pubnub.api.models.consumer.push.payload](../../../index.md)/[PushPayloadHelper](../../index.md)/[FCMPayloadV2](../index.md)/[AndroidConfig](index.md)

# AndroidConfig

[common]\
class [AndroidConfig](index.md) : [PushPayloadSerializer](../../../-push-payload-serializer/index.md)

## Constructors

| | |
|---|---|
| [AndroidConfig](-android-config.md) | [common]<br>constructor() |

## Types

| Name | Summary |
|---|---|
| [AndroidFcmOptions](-android-fcm-options/index.md) | [common]<br>class [AndroidFcmOptions](-android-fcm-options/index.md) : [PushPayloadSerializer](../../../-push-payload-serializer/index.md) |
| [AndroidMessagePriority](-android-message-priority/index.md) | [common]<br>enum [AndroidMessagePriority](-android-message-priority/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[PushPayloadHelper.FCMPayloadV2.AndroidConfig.AndroidMessagePriority](-android-message-priority/index.md)&gt; |
| [AndroidNotification](-android-notification/index.md) | [common]<br>class [AndroidNotification](-android-notification/index.md) : [PushPayloadSerializer](../../../-push-payload-serializer/index.md) |

## Properties

| Name | Summary |
|---|---|
| [collapseKey](collapse-key.md) | [common]<br>var [collapseKey](collapse-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? |
| [data](data.md) | [common]<br>var [data](data.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)&gt;? |
| [directBootOk](direct-boot-ok.md) | [common]<br>var [directBootOk](direct-boot-ok.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [fcmOptions](fcm-options.md) | [common]<br>var [fcmOptions](fcm-options.md): [PushPayloadHelper.FCMPayloadV2.AndroidConfig.AndroidFcmOptions](-android-fcm-options/index.md)? |
| [notification](notification.md) | [common]<br>var [notification](notification.md): [PushPayloadHelper.FCMPayloadV2.AndroidConfig.AndroidNotification](-android-notification/index.md)? |
| [priority](priority.md) | [common]<br>var [priority](priority.md): [PushPayloadHelper.FCMPayloadV2.AndroidConfig.AndroidMessagePriority](-android-message-priority/index.md) |
| [restrictedPackageName](restricted-package-name.md) | [common]<br>var [restrictedPackageName](restricted-package-name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? |
| [ttl](ttl.md) | [common]<br>var [ttl](ttl.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? |

## Functions

| Name | Summary |
|---|---|
| [toMap](to-map.md) | [common]<br>open override fun [toMap](to-map.md)(): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)&gt; |
