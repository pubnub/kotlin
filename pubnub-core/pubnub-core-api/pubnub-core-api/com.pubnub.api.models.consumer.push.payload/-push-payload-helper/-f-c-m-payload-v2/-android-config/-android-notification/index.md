//[pubnub-core-api](../../../../../../index.md)/[com.pubnub.api.models.consumer.push.payload](../../../../index.md)/[PushPayloadHelper](../../../index.md)/[FCMPayloadV2](../../index.md)/[AndroidConfig](../index.md)/[AndroidNotification](index.md)

# AndroidNotification

[jvm]\
class [AndroidNotification](index.md) : [PushPayloadSerializer](../../../../-push-payload-serializer/index.md)

## Constructors

| | |
|---|---|
| [AndroidNotification](-android-notification.md) | [jvm]<br>constructor() |

## Types

| Name | Summary |
|---|---|
| [LightSettings](-light-settings/index.md) | [jvm]<br>class [LightSettings](-light-settings/index.md) : [PushPayloadSerializer](../../../../-push-payload-serializer/index.md) |
| [NotificationPriority](-notification-priority/index.md) | [jvm]<br>enum [NotificationPriority](-notification-priority/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[PushPayloadHelper.FCMPayloadV2.AndroidConfig.AndroidNotification.NotificationPriority](-notification-priority/index.md)&gt; |
| [Visibility](-visibility/index.md) | [jvm]<br>enum [Visibility](-visibility/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[PushPayloadHelper.FCMPayloadV2.AndroidConfig.AndroidNotification.Visibility](-visibility/index.md)&gt; |

## Properties

| Name | Summary |
|---|---|
| [body](body.md) | [jvm]<br>var [body](body.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [bodyLocArgs](body-loc-args.md) | [jvm]<br>var [bodyLocArgs](body-loc-args.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;? |
| [bodyLocKey](body-loc-key.md) | [jvm]<br>var [bodyLocKey](body-loc-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [channelId](channel-id.md) | [jvm]<br>var [channelId](channel-id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [clickAction](click-action.md) | [jvm]<br>var [clickAction](click-action.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [color](color.md) | [jvm]<br>var [color](color.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [defaultLightSettings](default-light-settings.md) | [jvm]<br>var [defaultLightSettings](default-light-settings.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? |
| [defaultSound](default-sound.md) | [jvm]<br>var [defaultSound](default-sound.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? |
| [defaultVibrateTimings](default-vibrate-timings.md) | [jvm]<br>var [defaultVibrateTimings](default-vibrate-timings.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? |
| [eventTime](event-time.md) | [jvm]<br>var [eventTime](event-time.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [icon](icon.md) | [jvm]<br>var [icon](icon.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [image](image.md) | [jvm]<br>var [image](image.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [lightSettings](light-settings.md) | [jvm]<br>var [lightSettings](light-settings.md): [PushPayloadHelper.FCMPayloadV2.AndroidConfig.AndroidNotification.LightSettings](-light-settings/index.md)? |
| [localOnly](local-only.md) | [jvm]<br>var [localOnly](local-only.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? |
| [notificationCount](notification-count.md) | [jvm]<br>var [notificationCount](notification-count.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? |
| [notificationPriority](notification-priority.md) | [jvm]<br>var [notificationPriority](notification-priority.md): [PushPayloadHelper.FCMPayloadV2.AndroidConfig.AndroidNotification.NotificationPriority](-notification-priority/index.md) |
| [sound](sound.md) | [jvm]<br>var [sound](sound.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [sticky](sticky.md) | [jvm]<br>var [sticky](sticky.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [tag](tag.md) | [jvm]<br>var [tag](tag.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [ticker](ticker.md) | [jvm]<br>var [ticker](ticker.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [title](title.md) | [jvm]<br>var [title](title.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [titleLocArgs](title-loc-args.md) | [jvm]<br>var [titleLocArgs](title-loc-args.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;? |
| [titleLocKey](title-loc-key.md) | [jvm]<br>var [titleLocKey](title-loc-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [vibrateTimings](vibrate-timings.md) | [jvm]<br>var [vibrateTimings](vibrate-timings.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;? |
| [visibility](visibility.md) | [jvm]<br>var [visibility](visibility.md): [PushPayloadHelper.FCMPayloadV2.AndroidConfig.AndroidNotification.Visibility](-visibility/index.md)? |

## Functions

| Name | Summary |
|---|---|
| [toMap](to-map.md) | [jvm]<br>open override fun [toMap](to-map.md)(): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; |
