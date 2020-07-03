---
title: MessageActionService.addMessageAction - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.services](../index.html) / [MessageActionService](index.html) / [addMessageAction](./add-message-action.html)

# addMessageAction

`@POST("v1/message-actions/{subKey}/channel/{channel}/message/{messageTimetoken}") @Headers(["Content-Type: application/json; charset=UTF-8"]) abstract fun addMessageAction(@Path("subKey") subKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, @Path("channel") channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, @Path("messageTimetoken") messageTimetoken: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, @Body body: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, @QueryMap options: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`EntityEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-envelope/index.html)`<`[`PNMessageAction`](../../com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.html)`>>`