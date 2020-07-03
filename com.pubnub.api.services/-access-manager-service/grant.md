---
title: AccessManagerService.grant - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.services](../index.html) / [AccessManagerService](index.html) / [grant](./grant.html)

# grant

`@GET("/v2/auth/grant/sub-key/{subKey}") abstract fun grant(@Path("subKey") subKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, @QueryMap options: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.html)`<`[`AccessManagerGrantPayload`](../../com.pubnub.api.models.server.access_manager/-access-manager-grant-payload/index.html)`>>`