//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.subscriptions](../index.md)/[SubscriptionOptions](index.md)

# SubscriptionOptions

open class [SubscriptionOptions](index.md)

SubscriptionOptions is a mechanism used for supplying optional modifiers for subscriptions.

The options currently available in the PubNub client are:

- 
   [SubscriptionOptions.filter](-companion/filter.md)
- 
   [SubscriptionOptions.receivePresenceEvents](-companion/receive-presence-events.md)

#### Inheritors

| |
|---|
| [EmptyOptions](../-empty-options/index.md) |
| [FilterImpl](../-filter-impl/index.md) |
| [ReceivePresenceEventsImpl](../-receive-presence-events-impl/index.md) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [allOptions](all-options.md) | [common]<br>open val [allOptions](all-options.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[SubscriptionOptions](index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [plus](plus.md) | [common]<br>open operator fun [plus](plus.md)(options: [SubscriptionOptions](index.md)): [SubscriptionOptions](index.md)<br>Combine multiple options, for example: |
