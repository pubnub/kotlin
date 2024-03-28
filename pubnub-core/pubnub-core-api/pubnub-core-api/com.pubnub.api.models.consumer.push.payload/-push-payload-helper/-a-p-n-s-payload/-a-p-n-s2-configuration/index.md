//[pubnub-core-api](../../../../../index.md)/[com.pubnub.api.models.consumer.push.payload](../../../index.md)/[PushPayloadHelper](../../index.md)/[APNSPayload](../index.md)/[APNS2Configuration](index.md)

# APNS2Configuration

[jvm]\
class [APNS2Configuration](index.md) : [PushPayloadSerializer](../../../-push-payload-serializer/index.md)

## Constructors

| | |
|---|---|
| [APNS2Configuration](-a-p-n-s2-configuration.md) | [jvm]<br>constructor() |

## Types

| Name | Summary |
|---|---|
| [Target](-target/index.md) | [jvm]<br>class [Target](-target/index.md) : [PushPayloadSerializer](../../../-push-payload-serializer/index.md) |

## Properties

| Name | Summary |
|---|---|
| [collapseId](collapse-id.md) | [jvm]<br>var [collapseId](collapse-id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [expiration](expiration.md) | [jvm]<br>var [expiration](expiration.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [targets](targets.md) | [jvm]<br>var [targets](targets.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PushPayloadHelper.APNSPayload.APNS2Configuration.Target](-target/index.md)&gt;? |
| [version](version.md) | [jvm]<br>var [version](version.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |

## Functions

| Name | Summary |
|---|---|
| [toMap](to-map.md) | [jvm]<br>open override fun [toMap](to-map.md)(): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; |
