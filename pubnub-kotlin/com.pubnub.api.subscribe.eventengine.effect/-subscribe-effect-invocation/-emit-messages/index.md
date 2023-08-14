//[pubnub-kotlin](../../../../index.md)/[com.pubnub.api.subscribe.eventengine.effect](../../index.md)/[SubscribeEffectInvocation](../index.md)/[EmitMessages](index.md)

# EmitMessages

[jvm]\
data class [EmitMessages](index.md)(val messages: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PNEvent](../../../com.pubnub.api.models.consumer.pubsub/-p-n-event/index.md)&gt;) : [SubscribeEffectInvocation](../index.md)

## Constructors

| | |
|---|---|
| [EmitMessages](-emit-messages.md) | [jvm]<br>fun [EmitMessages](-emit-messages.md)(messages: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PNEvent](../../../com.pubnub.api.models.consumer.pubsub/-p-n-event/index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [id](../id.md) | [jvm]<br>open override val [id](../id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [messages](messages.md) | [jvm]<br>val [messages](messages.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PNEvent](../../../com.pubnub.api.models.consumer.pubsub/-p-n-event/index.md)&gt; |
| [type](../type.md) | [jvm]<br>open override val [type](../type.md): [EffectInvocationType](../../../com.pubnub.api.eventengine/-effect-invocation-type/index.md) |
