---
title: Crypto - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.vendor](../index.html) / [Crypto](./index.html)

# Crypto

`open class Crypto`

### Constructors

| [&lt;init&gt;](-init-.html) | `Crypto(cipherKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!)`<br>`Crypto(cipherKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!, customInitializationVector: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!)` |

### Functions

| [decrypt](decrypt.html) | Decrypt`open fun decrypt(cipher_text: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!` |
| [encrypt](encrypt.html) | `open fun encrypt(input: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!` |
| [hexEncode](hex-encode.html) | `open static fun hexEncode(input: `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!): `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!` |
| [hexStringToByteArray](hex-string-to-byte-array.html) | `open static fun hexStringToByteArray(s: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!): `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!` |
| [initCiphers](init-ciphers.html) | `open fun initCiphers(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [md5](md5.html) | Get MD5`open static fun md5(input: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!): `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!` |
| [newCryptoError](new-crypto-error.html) | `open static fun newCryptoError(code: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, exception: `[`Exception`](https://docs.oracle.com/javase/6/docs/api/java/lang/Exception.html)`!): `[`PubNubException`](../../com.pubnub.api/-pub-nub-exception/index.html)`!` |
| [sha256](sha256.html) | Get SHA256`open static fun sha256(input: `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!): `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!` |

