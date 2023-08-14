//[pubnub-kotlin](../../../../index.md)/[com.pubnub.api.models.consumer.push.payload](../../index.md)/[PushPayloadHelper](../index.md)/[APNSPayload](index.md)

# APNSPayload

[jvm]\
class [APNSPayload](index.md) : [PushPayloadSerializer](../../-push-payload-serializer/index.md)

## Constructors

| | |
|---|---|
| [APNSPayload](-a-p-n-s-payload.md) | [jvm]<br>fun [APNSPayload](-a-p-n-s-payload.md)() |

## Types

| Name | Summary |
|---|---|
| [APNS2Configuration](-a-p-n-s2-configuration/index.md) | [jvm]<br>class [APNS2Configuration](-a-p-n-s2-configuration/index.md) : [PushPayloadSerializer](../../-push-payload-serializer/index.md) |
| [APS](-a-p-s/index.md) | [jvm]<br>class [APS](-a-p-s/index.md) : [PushPayloadSerializer](../../-push-payload-serializer/index.md) |

## Functions

| Name | Summary |
|---|---|
| [toMap](to-map.md) | [jvm]<br>open override fun [toMap](to-map.md)(): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; |

## Properties

| Name | Summary |
|---|---|
| [apns2Configurations](apns2-configurations.md) | [jvm]<br>var [apns2Configurations](apns2-configurations.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PushPayloadHelper.APNSPayload.APNS2Configuration](-a-p-n-s2-configuration/index.md)&gt;? = null |
| [aps](aps.md) | [jvm]<br>var [aps](aps.md): [PushPayloadHelper.APNSPayload.APS](-a-p-s/index.md)? = null |
| [custom](custom.md) | [jvm]<br>var [custom](custom.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;? = null |
