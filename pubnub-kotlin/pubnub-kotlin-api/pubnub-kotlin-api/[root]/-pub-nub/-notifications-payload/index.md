//[pubnub-kotlin-api](../../../../index.md)/[[root]](../../index.md)/[PubNub](../index.md)/[NotificationsPayload](index.md)

# NotificationsPayload

[js]\
interface [NotificationsPayload](index.md)

## Properties

| Name | Summary |
|---|---|
| [apns](apns.md) | [js]<br>abstract var [apns](apns.md): [PubNub.APNSNotificationPayload](../-a-p-n-s-notification-payload/index.md) |
| [badge](badge.md) | [js]<br>abstract var [badge](badge.md): [Number](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-number/index.html)? |
| [body](body.md) | [js]<br>abstract var [body](body.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [debugging](debugging.md) | [js]<br>abstract var [debugging](debugging.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [fcm](fcm.md) | [js]<br>abstract var [fcm](fcm.md): [PubNub.FCMNotificationPayload](../-f-c-m-notification-payload/index.md) |
| [payload](payload.md) | [js]<br>abstract var [payload](payload.md): [PubNub.T$37](../-t$37/index.md) |
| [sound](sound.md) | [js]<br>abstract var [sound](sound.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [subtitle](subtitle.md) | [js]<br>abstract var [subtitle](subtitle.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [title](title.md) | [js]<br>abstract var [title](title.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |

## Functions

| Name | Summary |
|---|---|
| [buildPayload](build-payload.md) | [js]<br>abstract fun [buildPayload](build-payload.md)(platforms: [Array](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-array/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)? |
