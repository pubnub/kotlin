//[pubnub-kotlin-core-api](../../../../index.md)/[com.pubnub.api.models.consumer.push.payload](../../index.md)/[PushPayloadHelper](../index.md)/[FCMPayload](index.md)

# FCMPayload

[common]\
class [FCMPayload](index.md) : [PushPayloadSerializer](../../-push-payload-serializer/index.md)

## Constructors

| | |
|---|---|
| [FCMPayload](-f-c-m-payload.md) | [common]<br>constructor() |

## Types

| Name | Summary |
|---|---|
| [Notification](-notification/index.md) | [common]<br>class [Notification](-notification/index.md) : [PushPayloadSerializer](../../-push-payload-serializer/index.md) |

## Properties

| Name | Summary |
|---|---|
| [custom](custom.md) | [common]<br>var [custom](custom.md): [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt;? |
| [data](data.md) | [common]<br>var [data](data.md): [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt;? |
| [notification](notification.md) | [common]<br>var [notification](notification.md): [PushPayloadHelper.FCMPayload.Notification](-notification/index.md)? |

## Functions

| Name | Summary |
|---|---|
| [toMap](to-map.md) | [common]<br>open override fun [toMap](to-map.md)(): [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt; |
