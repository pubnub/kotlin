//[pubnub-kotlin-api](../../index.md)/[com.pubnub.api.endpoints.access](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [Grant](-grant/index.md) | [jvm]<br>interface [Grant](-grant/index.md) : [Endpoint](../com.pubnub.api/-endpoint/index.md)&lt;[PNAccessManagerGrantResult](../com.pubnub.api.models.consumer.access_manager/-p-n-access-manager-grant-result/index.md)&gt; |
| [GrantToken](-grant-token/index.md) | [common, ios, jvm]<br>[common]<br>expect interface [GrantToken](-grant-token/index.md) : [PNFuture](../com.pubnub.kmp/-p-n-future/index.md)&lt;[PNGrantTokenResult](../com.pubnub.api.models.consumer.access_manager.v3/-p-n-grant-token-result/index.md)&gt; <br>[ios]<br>actual interface [GrantToken](-grant-token/index.md)<br>[jvm]<br>actual interface [GrantToken](-grant-token/index.md) : [Endpoint](../com.pubnub.api/-endpoint/index.md)&lt;[PNGrantTokenResult](../com.pubnub.api.models.consumer.access_manager.v3/-p-n-grant-token-result/index.md)&gt; |
| [RevokeToken](-revoke-token/index.md) | [common, ios, jvm]<br>[common]<br>expect interface [RevokeToken](-revoke-token/index.md) : [PNFuture](../com.pubnub.kmp/-p-n-future/index.md)&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt; <br>[ios]<br>actual interface [RevokeToken](-revoke-token/index.md)<br>[jvm]<br>actual interface [RevokeToken](-revoke-token/index.md) : [Endpoint](../com.pubnub.api/-endpoint/index.md)&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt; |
