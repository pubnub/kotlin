---
title: HistoryService.fetchMessages - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.services](../index.html) / [HistoryService](index.html) / [fetchMessages](./fetch-messages.html)

# fetchMessages

`@GET("v3/history/sub-key/{subKey}/channel/{channels}") abstract fun fetchMessages(@Path("subKey") subKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, @Path("channels") channels: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, @QueryMap options: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`FetchMessagesEnvelope`](../../com.pubnub.api.models.server/-fetch-messages-envelope/index.html)`>`