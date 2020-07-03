---
title: PubNub.encrypt - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api](../index.html) / [PubNub](index.html) / [encrypt](./encrypt.html)

# encrypt

`fun encrypt(inputString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)

Perform Cryptographic encryption of an input string and the cipher key provided by PNConfiguration

### Parameters

`inputString` - String to be encrypted

**Return**
String containing the encryption of inputString using cipherKey

`fun encrypt(inputString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, cipherKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)

Perform Cryptographic encryption of an input string and the cipher key.

### Parameters

`inputString` - String to be encrypted

`cipherKey` - cipher key to be used for encryption

### Exceptions

`PubNubException` - throws exception in case of failed encryption

**Return**
String containing the encryption of inputString using cipherKey

