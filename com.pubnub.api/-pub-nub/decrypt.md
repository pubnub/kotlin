[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNub](index.md) / [decrypt](./decrypt.md)

# decrypt

`fun decrypt(inputString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)

Perform Cryptographic decryption of an input string using cipher key provided by [PNConfiguration.cipherKey](../-p-n-configuration/cipher-key.md).

### Parameters

`inputString` - String to be encrypted.

### Exceptions

`PubNubException` - throws exception in case of failed encryption.

**Return**
String containing the encryption of `inputString` using `cipherKey`.

`fun decrypt(inputString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, cipherKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)

Perform Cryptographic decryption of an input string using a cipher key.

### Parameters

`inputString` - String to be encrypted.

`cipherKey` - cipher key to be used for encryption.

### Exceptions

`PubNubException` - throws exception in case of failed encryption.

**Return**
String containing the encryption of `inputString` using `cipherKey`.

