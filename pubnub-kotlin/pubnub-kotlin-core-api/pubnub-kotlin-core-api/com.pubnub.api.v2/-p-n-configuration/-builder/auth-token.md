//[pubnub-kotlin-core-api](../../../../index.md)/[com.pubnub.api.v2](../../index.md)/[PNConfiguration](../index.md)/[Builder](index.md)/[authToken](auth-token.md)

# authToken

[jvm]\
abstract var [authToken](auth-token.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?

Authentication token for the PubNub client. This token is required on the client side when Access Manager (PAM) is enabled for PubNub keys. It can be generated using the PubNub.grantToken method, which should be executed on the server side with a PubNub instance initialized using the secret key.
