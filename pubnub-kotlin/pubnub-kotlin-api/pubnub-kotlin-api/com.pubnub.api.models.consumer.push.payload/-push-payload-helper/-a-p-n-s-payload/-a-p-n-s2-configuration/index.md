//[pubnub-kotlin-api](../../../../../index.md)/[com.pubnub.api.models.consumer.push.payload](../../../index.md)/[PushPayloadHelper](../../index.md)/[APNSPayload](../index.md)/[APNS2Configuration](index.md)

# APNS2Configuration

[common]\
class [APNS2Configuration](index.md) : [PushPayloadSerializer](../../../-push-payload-serializer/index.md)

## Constructors

| | |
|---|---|
| [APNS2Configuration](-a-p-n-s2-configuration.md) | [common]<br>constructor() |

## Types

| Name | Summary |
|---|---|
| [APNS2AuthMethod](-a-p-n-s2-auth-method/index.md) | [common]<br>enum [APNS2AuthMethod](-a-p-n-s2-auth-method/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[PushPayloadHelper.APNSPayload.APNS2Configuration.APNS2AuthMethod](-a-p-n-s2-auth-method/index.md)&gt; |
| [Target](-target/index.md) | [common]<br>class [Target](-target/index.md) : [PushPayloadSerializer](../../../-push-payload-serializer/index.md) |

## Properties

| Name | Summary |
|---|---|
| [authMethod](auth-method.md) | [common]<br>var [authMethod](auth-method.md): [PushPayloadHelper.APNSPayload.APNS2Configuration.APNS2AuthMethod](-a-p-n-s2-auth-method/index.md)? |
| [collapseId](collapse-id.md) | [common]<br>var [collapseId](collapse-id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [expiration](expiration.md) | [common]<br>var [expiration](expiration.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [targets](targets.md) | [common]<br>var [targets](targets.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PushPayloadHelper.APNSPayload.APNS2Configuration.Target](-target/index.md)&gt;? |
| [version](version.md) | [common]<br>var [version](version.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |

## Functions

| Name | Summary |
|---|---|
| [toMap](to-map.md) | [common]<br>open override fun [toMap](to-map.md)(): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; |
