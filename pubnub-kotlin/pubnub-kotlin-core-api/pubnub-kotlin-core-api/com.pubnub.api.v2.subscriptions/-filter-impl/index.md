//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.v2.subscriptions](../index.md)/[FilterImpl](index.md)

# FilterImpl

[common]\
class [FilterImpl](index.md) : [SubscriptionOptions](../-subscription-options/index.md)

## Properties

| Name | Summary |
|---|---|
| [allOptions](../-subscription-options/all-options.md) | [common]<br>open val [allOptions](../-subscription-options/all-options.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[SubscriptionOptions](../-subscription-options/index.md)&gt; |
| [predicate](predicate.md) | [common]<br>val [predicate](predicate.md): ([PNEvent](../../com.pubnub.api.models.consumer.pubsub/-p-n-event/index.md)) -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

## Functions

| Name | Summary |
|---|---|
| [plus](../-subscription-options/plus.md) | [common]<br>open operator fun [plus](../-subscription-options/plus.md)(options: [SubscriptionOptions](../-subscription-options/index.md)): [SubscriptionOptions](../-subscription-options/index.md)<br>Combine multiple options, for example: |
