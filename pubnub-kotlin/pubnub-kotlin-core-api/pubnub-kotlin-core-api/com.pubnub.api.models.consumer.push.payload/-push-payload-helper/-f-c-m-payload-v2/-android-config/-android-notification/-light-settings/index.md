//[pubnub-kotlin-core-api](../../../../../../../index.md)/[com.pubnub.api.models.consumer.push.payload](../../../../../index.md)/[PushPayloadHelper](../../../../index.md)/[FCMPayloadV2](../../../index.md)/[AndroidConfig](../../index.md)/[AndroidNotification](../index.md)/[LightSettings](index.md)

# LightSettings

[common]\
class [LightSettings](index.md) : [PushPayloadSerializer](../../../../../-push-payload-serializer/index.md)

## Constructors

| | |
|---|---|
| [LightSettings](-light-settings.md) | [common]<br>constructor() |

## Types

| Name | Summary |
|---|---|
| [Color](-color/index.md) | [common]<br>class [Color](-color/index.md)(val red: [Float](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-float/index.html), val green: [Float](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-float/index.html), val blue: [Float](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-float/index.html), val alpha: [Float](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-float/index.html)) : [PushPayloadSerializer](../../../../../-push-payload-serializer/index.md) |

## Properties

| Name | Summary |
|---|---|
| [color](color.md) | [common]<br>var [color](color.md): [PushPayloadHelper.FCMPayloadV2.AndroidConfig.AndroidNotification.LightSettings.Color](-color/index.md)? |
| [lightOffDuration](light-off-duration.md) | [common]<br>var [lightOffDuration](light-off-duration.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [lightOnDuration](light-on-duration.md) | [common]<br>var [lightOnDuration](light-on-duration.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |

## Functions

| Name | Summary |
|---|---|
| [toMap](to-map.md) | [common]<br>open override fun [toMap](to-map.md)(): [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt; |
