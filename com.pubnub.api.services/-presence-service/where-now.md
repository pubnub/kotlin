---
title: PresenceService.whereNow - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.services](../index.html) / [PresenceService](index.html) / [whereNow](./where-now.html)

# whereNow

`@GET("v2/presence/sub-key/{subKey}/uuid/{uuid}") abstract fun whereNow(@Path("subKey") subKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, @Path("uuid") uuid: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, @QueryMap options: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.html)`<`[`WhereNowPayload`](../../com.pubnub.api.models.server.presence/-where-now-payload/index.html)`>>`