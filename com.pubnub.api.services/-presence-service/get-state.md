---
title: PresenceService.getState - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.services](../index.html) / [PresenceService](index.html) / [getState](./get-state.html)

# getState

`@GET("v2/presence/sub-key/{subKey}/channel/{channel}/uuid/{uuid}") abstract fun getState(@Path("subKey") subKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, @Path("channel") channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, @Path("uuid") uuid: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, @QueryMap options: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.html)`<JsonElement>>`