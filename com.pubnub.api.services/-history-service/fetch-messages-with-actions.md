---
title: HistoryService.fetchMessagesWithActions - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.services](../index.html) / [HistoryService](index.html) / [fetchMessagesWithActions](./fetch-messages-with-actions.html)

# fetchMessagesWithActions

`@GET("v3/history-with-actions/sub-key/{subKey}/channel/{channel}") abstract fun fetchMessagesWithActions(@Path("subKey") subKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, @Path("channel") channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, @QueryMap options: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`FetchMessagesEnvelope`](../../com.pubnub.api.models.server/-fetch-messages-envelope/index.html)`>`