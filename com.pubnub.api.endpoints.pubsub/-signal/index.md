---
title: Signal - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.endpoints.pubsub](../index.html) / [Signal](./index.html)

# Signal

`class Signal : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.html)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>, `[`PNPublishResult`](../../com.pubnub.api.models.consumer/-p-n-publish-result/index.html)`>`

### Constructors

| [&lt;init&gt;](-init-.html) | `Signal(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.html)`)` |

### Properties

| [channel](channel.html) | `lateinit var channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [message](message.html) | `lateinit var message: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |

### Functions

| [createResponse](create-response.html) | `fun createResponse(input: Response<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>): `[`PNPublishResult`](../../com.pubnub.api.models.consumer/-p-n-publish-result/index.html)`?` |
| [doWork](do-work.html) | `fun doWork(queryParams: `[`HashMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>` |
| [getAffectedChannels](get-affected-channels.html) | `fun getAffectedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [isPubKeyRequired](is-pub-key-required.html) | `fun isPubKeyRequired(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [operationType](operation-type.html) | `fun operationType(): PNSignalOperation` |
| [validateParams](validate-params.html) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

