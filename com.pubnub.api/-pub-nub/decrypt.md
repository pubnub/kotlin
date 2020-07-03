---
title: PubNub.decrypt - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api](../index.html) / [PubNub](index.html) / [decrypt](./decrypt.html)

# decrypt

`fun decrypt(inputString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)

Perform Cryptographic decryption of an input string using cipher key provided by PNConfiguration

### Parameters

`inputString` - String to be encrypted

**Return**
String containing the encryption of inputString using cipherKey

`fun decrypt(inputString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, cipherKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)

Perform Cryptographic decryption of an input string using the cipher key

### Parameters

`inputString` - String to be encrypted

`cipherKey` - cipher key to be used for encryption

### Exceptions

`PubNubException` - throws exception in case of failed encryption

**Return**
String containing the encryption of inputString using cipherKey

