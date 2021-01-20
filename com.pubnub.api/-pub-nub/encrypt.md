[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNub](index.md) / [encrypt](./encrypt.md)

# encrypt

`fun encrypt(inputString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, cipherKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = configuration.cipherKey): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)

Perform Cryptographic encryption of an input string and a cipher key.

### Parameters

`inputString` - String to be encrypted.

`cipherKey` - Cipher key to be used for encryption. Default is [PNConfiguration.cipherKey](../-p-n-configuration/cipher-key.md)

### Exceptions

`PubNubException` - Throws exception in case of failed encryption.

**Return**
String containing the encryption of `inputString` using `cipherKey`.

