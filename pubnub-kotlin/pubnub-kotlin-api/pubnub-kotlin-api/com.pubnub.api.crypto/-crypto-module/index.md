//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.crypto](../index.md)/[CryptoModule](index.md)

# CryptoModule

[jvm]\
interface [CryptoModule](index.md)

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [decrypt](decrypt.md) | [jvm]<br>abstract fun [decrypt](decrypt.md)(encryptedData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html) |
| [decryptStream](decrypt-stream.md) | [jvm]<br>abstract fun [decryptStream](decrypt-stream.md)(encryptedData: [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html)): [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html) |
| [encrypt](encrypt.md) | [jvm]<br>abstract fun [encrypt](encrypt.md)(data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html) |
| [encryptStream](encrypt-stream.md) | [jvm]<br>abstract fun [encryptStream](encrypt-stream.md)(stream: [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html)): [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html) |
