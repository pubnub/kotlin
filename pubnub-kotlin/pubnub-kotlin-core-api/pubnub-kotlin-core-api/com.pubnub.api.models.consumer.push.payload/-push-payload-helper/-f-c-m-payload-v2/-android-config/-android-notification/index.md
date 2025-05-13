//[pubnub-kotlin-core-api](../../../../../../index.md)/[com.pubnub.api.models.consumer.push.payload](../../../../index.md)/[PushPayloadHelper](../../../index.md)/[FCMPayloadV2](../../index.md)/[AndroidConfig](../index.md)/[AndroidNotification](index.md)

# AndroidNotification

[common]\
class [AndroidNotification](index.md) : [PushPayloadSerializer](../../../../-push-payload-serializer/index.md)

## Constructors

| | |
|---|---|
| [AndroidNotification](-android-notification.md) | [common]<br>constructor() |

## Types

| Name | Summary |
|---|---|
| [LightSettings](-light-settings/index.md) | [common]<br>class [LightSettings](-light-settings/index.md) : [PushPayloadSerializer](../../../../-push-payload-serializer/index.md) |
| [NotificationPriority](-notification-priority/index.md) | [common]<br>enum [NotificationPriority](-notification-priority/index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[PushPayloadHelper.FCMPayloadV2.AndroidConfig.AndroidNotification.NotificationPriority](-notification-priority/index.md)&gt; |
| [Visibility](-visibility/index.md) | [common]<br>enum [Visibility](-visibility/index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[PushPayloadHelper.FCMPayloadV2.AndroidConfig.AndroidNotification.Visibility](-visibility/index.md)&gt; |

## Properties

| Name | Summary |
|---|---|
| [body](body.md) | [common]<br>var [body](body.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [bodyLocArgs](body-loc-args.md) | [common]<br>var [bodyLocArgs](body-loc-args.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;? |
| [bodyLocKey](body-loc-key.md) | [common]<br>var [bodyLocKey](body-loc-key.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [channelId](channel-id.md) | [common]<br>var [channelId](channel-id.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [clickAction](click-action.md) | [common]<br>var [clickAction](click-action.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [color](color.md) | [common]<br>var [color](color.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [defaultLightSettings](default-light-settings.md) | [common]<br>var [defaultLightSettings](default-light-settings.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)? |
| [defaultSound](default-sound.md) | [common]<br>var [defaultSound](default-sound.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)? |
| [defaultVibrateTimings](default-vibrate-timings.md) | [common]<br>var [defaultVibrateTimings](default-vibrate-timings.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)? |
| [eventTime](event-time.md) | [common]<br>var [eventTime](event-time.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [icon](icon.md) | [common]<br>var [icon](icon.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [image](image.md) | [common]<br>var [image](image.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [lightSettings](light-settings.md) | [common]<br>var [lightSettings](light-settings.md): [PushPayloadHelper.FCMPayloadV2.AndroidConfig.AndroidNotification.LightSettings](-light-settings/index.md)? |
| [localOnly](local-only.md) | [common]<br>var [localOnly](local-only.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)? |
| [notificationCount](notification-count.md) | [common]<br>var [notificationCount](notification-count.md): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)? |
| [notificationPriority](notification-priority.md) | [common]<br>var [notificationPriority](notification-priority.md): [PushPayloadHelper.FCMPayloadV2.AndroidConfig.AndroidNotification.NotificationPriority](-notification-priority/index.md) |
| [sound](sound.md) | [common]<br>var [sound](sound.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [sticky](sticky.md) | [common]<br>var [sticky](sticky.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [tag](tag.md) | [common]<br>var [tag](tag.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [ticker](ticker.md) | [common]<br>var [ticker](ticker.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [title](title.md) | [common]<br>var [title](title.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [titleLocArgs](title-loc-args.md) | [common]<br>var [titleLocArgs](title-loc-args.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;? |
| [titleLocKey](title-loc-key.md) | [common]<br>var [titleLocKey](title-loc-key.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [vibrateTimings](vibrate-timings.md) | [common]<br>var [vibrateTimings](vibrate-timings.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;? |
| [visibility](visibility.md) | [common]<br>var [visibility](visibility.md): [PushPayloadHelper.FCMPayloadV2.AndroidConfig.AndroidNotification.Visibility](-visibility/index.md)? |

## Functions

| Name | Summary |
|---|---|
| [toMap](to-map.md) | [common]<br>open override fun [toMap](to-map.md)(): [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt; |
