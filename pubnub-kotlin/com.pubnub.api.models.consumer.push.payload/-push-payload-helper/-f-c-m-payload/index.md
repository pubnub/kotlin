//[pubnub-kotlin](../../../../index.md)/[com.pubnub.api.models.consumer.push.payload](../../index.md)/[PushPayloadHelper](../index.md)/[FCMPayload](index.md)

# FCMPayload

[jvm]\
class [FCMPayload](index.md) : [PushPayloadSerializer](../../-push-payload-serializer/index.md)

## Constructors

| | |
|---|---|
| [FCMPayload](-f-c-m-payload.md) | [jvm]<br>fun [FCMPayload](-f-c-m-payload.md)() |

## Types

| Name | Summary |
|---|---|
| [Notification](-notification/index.md) | [jvm]<br>class [Notification](-notification/index.md) : [PushPayloadSerializer](../../-push-payload-serializer/index.md) |

## Functions

| Name | Summary |
|---|---|
| [toMap](to-map.md) | [jvm]<br>open override fun [toMap](to-map.md)(): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; |

## Properties

| Name | Summary |
|---|---|
| [custom](custom.md) | [jvm]<br>var [custom](custom.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;? = null |
| [data](data.md) | [jvm]<br>var [data](data.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;? = null |
| [notification](notification.md) | [jvm]<br>var [notification](notification.md): [PushPayloadHelper.FCMPayload.Notification](-notification/index.md)? = null |
