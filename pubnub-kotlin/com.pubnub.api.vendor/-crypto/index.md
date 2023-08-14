//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.vendor](../index.md)/[Crypto](index.md)

# Crypto

[jvm]\
open class [Crypto](index.md)

## Constructors

| | |
|---|---|
| [Crypto](-crypto.md) | [jvm]<br>open fun [Crypto](-crypto.md)(cipherKey: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)) |
| [Crypto](-crypto.md) | [jvm]<br>open fun [Crypto](-crypto.md)(cipherKey: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html), customInitializationVector: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)) |
| [Crypto](-crypto.md) | [jvm]<br>open fun [Crypto](-crypto.md)(cipherKey: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html), dynamicIV: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |

## Functions

| Name | Summary |
|---|---|
| [decrypt](decrypt.md) | [jvm]<br>open fun [decrypt](decrypt.md)(cipher_text: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)<br>open fun [decrypt](decrypt.md)(cipher_text: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html), flags: [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [encrypt](encrypt.md) | [jvm]<br>open fun [encrypt](encrypt.md)(input: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)<br>open fun [encrypt](encrypt.md)(input: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html), flags: [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [hexEncode](hex-encode.md) | [jvm]<br>open fun [hexEncode](hex-encode.md)(input: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt;): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt; |
| [hexStringToByteArray](hex-string-to-byte-array.md) | [jvm]<br>open fun [hexStringToByteArray](hex-string-to-byte-array.md)(s: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt; |
| [initCiphers](init-ciphers.md) | [jvm]<br>open fun [initCiphers](init-ciphers.md)() |
| [md5](md5.md) | [jvm]<br>open fun [md5](md5.md)(input: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt;<br>Get MD5 |
| [newCryptoError](new-crypto-error.md) | [jvm]<br>open fun [newCryptoError](new-crypto-error.md)(code: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), exception: [Exception](https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html)): [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md) |
| [sha256](sha256.md) | [jvm]<br>open fun [sha256](sha256.md)(input: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt;): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt;<br>Get SHA256 |
